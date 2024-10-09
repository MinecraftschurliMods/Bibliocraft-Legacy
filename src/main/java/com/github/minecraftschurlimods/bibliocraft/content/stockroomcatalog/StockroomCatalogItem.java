package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class StockroomCatalogItem extends Item {
    public StockroomCatalogItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && context.isSecondaryUseActive()) {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();
            GlobalPos globalPos = new GlobalPos(level.dimension(), pos);
            ItemStack stack = context.getItemInHand();
            StockroomCatalogContent list = stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT);
            if (list.positions().contains(globalPos)) {
                stack.update(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT, component -> component.remove(globalPos));
                player.displayClientMessage(Component.translatable(Translations.STOCKROOM_CATALOG_REMOVE, level.getBlockEntity(pos) instanceof Nameable nameable ? nameable.getDisplayName() : level.getBlockState(pos).getBlock().getName()), true);
                return InteractionResult.SUCCESS;
            }
            IItemHandler cap = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, context.getClickedFace());
            if (cap != null) {
                stack.update(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT, component -> component.add(globalPos));
                player.displayClientMessage(Component.translatable(Translations.STOCKROOM_CATALOG_ADD, level.getBlockEntity(pos) instanceof Nameable nameable ? nameable.getDisplayName() : level.getBlockState(pos).getBlock().getName()), true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientUtil.openStockroomCatalogScreen(stack);
        }
        return InteractionResultHolder.success(stack);
    }
}
