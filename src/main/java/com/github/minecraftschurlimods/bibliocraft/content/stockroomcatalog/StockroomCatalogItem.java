package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.SequencedMap;

public class StockroomCatalogItem extends Item {
    private static final Comparator<StockroomCatalogItemEntry> COMPARE_NAME = Comparator.comparing(e -> e.item().getDisplayName().getString());
    private static final Comparator<StockroomCatalogItemEntry> COMPARE_COUNT = Comparator.comparingInt(StockroomCatalogItemEntry::count);

    public StockroomCatalogItem(Properties properties) {
        super(properties.component(DataComponents.MAX_STACK_SIZE, 1).component(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT));
    }

    @SuppressWarnings("deprecation")
    public static List<BlockPos> calculatePositions(ItemStack stack, Level level, Player player, StockroomCatalogSorting.Container containerSorting) {
        Comparator<BlockPos> COMPARE_DISTANCE = Comparator.comparingDouble(e -> player.position().distanceTo(BCUtil.toVec3(e)));
        Comparator<BlockPos> COMPARE_ALPHABETICAL = Comparator.comparing(e -> BCUtil.getNameAtPos(level, e).getString());
        return stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT)
                .positions()
                .stream()
                .filter(e -> e.dimension() == level.dimension())
                .map(GlobalPos::pos)
                .filter(level::hasChunkAt)
                .filter(e -> level.getCapability(Capabilities.Item.BLOCK, e, null) != null)
                .sorted(switch (containerSorting) {
                    case ALPHABETICAL_ASC, DISTANCE_ASC -> COMPARE_DISTANCE;
                    case ALPHABETICAL_DESC, DISTANCE_DESC -> BCUtil.reverseComparator(COMPARE_DISTANCE);
                })
                .sorted(switch (containerSorting) {
                    case ALPHABETICAL_ASC -> COMPARE_ALPHABETICAL;
                    case ALPHABETICAL_DESC -> BCUtil.reverseComparator(COMPARE_ALPHABETICAL);
                    default -> Comparator.comparingInt($ -> 0);
                })
                .toList();
    }

    public static List<StockroomCatalogItemEntry> calculateItems(List<BlockPos> positions, Level level, StockroomCatalogSorting.Item itemSorting) {
        SequencedMap<ItemStack, StockroomCatalogItemEntry> tempItems = new LinkedHashMap<>();
        for (BlockPos pos : positions) {
            ResourceHandler<ItemResource> cap = level.getCapability(Capabilities.Item.BLOCK, pos, null);
            if (cap == null) continue;
            IItemHandler handler = IItemHandler.of(cap);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack originalStack = handler.getStackInSlot(i);
                if (originalStack.isEmpty()) continue;
                ItemStack stack = originalStack.copy();
                int count = stack.getCount();
                stack.setCount(1);
                Optional<ItemStack> optional = tempItems
                        .keySet()
                        .stream()
                        .filter(e -> ItemStack.isSameItemSameComponents(e, stack))
                        .findFirst();
                StockroomCatalogItemEntry entry = optional
                        .map(itemStack -> tempItems.get(itemStack).add(count))
                        .orElseGet(() -> new StockroomCatalogItemEntry(originalStack));
                tempItems.put(optional.orElse(stack), entry.add(pos));
            }
        }
        return tempItems.sequencedValues()
                .stream()
                .sorted(switch (itemSorting) {
                    case ALPHABETICAL_ASC -> COMPARE_COUNT;
                    case ALPHABETICAL_DESC -> BCUtil.reverseComparator(COMPARE_COUNT);
                    case COUNT_ASC -> COMPARE_NAME;
                    case COUNT_DESC -> BCUtil.reverseComparator(COMPARE_NAME);
                })
                .sorted(switch (itemSorting) {
                    case ALPHABETICAL_ASC -> COMPARE_NAME;
                    case ALPHABETICAL_DESC -> BCUtil.reverseComparator(COMPARE_NAME);
                    case COUNT_ASC -> COMPARE_COUNT;
                    case COUNT_DESC -> BCUtil.reverseComparator(COMPARE_COUNT);
                })
                .toList();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && context.isSecondaryUseActive()) {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = context.getItemInHand();
            StockroomCatalogContent list = stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT);
            boolean hasNeighbor = state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE;
            GlobalPos neighborPos = hasNeighbor ? new GlobalPos(level.dimension(), pos.offset(ChestBlock.getConnectedDirection(state).getUnitVec3i())) : null;
            boolean hasPositionAtNeighbor = hasNeighbor && list.positions().contains(neighborPos);
            GlobalPos globalPos = hasPositionAtNeighbor ? neighborPos : new GlobalPos(level.dimension(), pos);
            if (list.positions().contains(globalPos)) {
                stack.update(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT, component -> component.remove(globalPos));
                player.displayClientMessage(Component.translatable(Translations.STOCKROOM_CATALOG_REMOVE_CONTAINER_KEY, BCUtil.getNameAtPos(level, pos)), true);
                return InteractionResult.SUCCESS;
            }
            ResourceHandler<ItemResource> cap = level.getCapability(Capabilities.Item.BLOCK, pos, context.getClickedFace());
            if (cap != null) {
                stack.update(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT, component -> component.add(globalPos));
                player.displayClientMessage(Component.translatable(Translations.STOCKROOM_CATALOG_ADD_CONTAINER_KEY, BCUtil.getNameAtPos(level, pos)), true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientUtil.openStockroomCatalogScreen(stack, player, hand);
        }
        return InteractionResult.SUCCESS;
    }
}
