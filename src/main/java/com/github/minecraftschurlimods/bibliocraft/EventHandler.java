package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.RegisterLockAndKeyBehaviorEvent;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.RegisterBibliocraftWoodTypesEvent;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.LockAndKeyBehaviorsImpl;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookSignPacket;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.SetBigBookPageInLecternPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogRequestListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternPacket;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.TakeLecternBookPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public final class EventHandler {
    @ApiStatus.Internal
    public static void init(IEventBus modBus) {
        modBus.addListener(EventPriority.LOWEST, FMLConstructModEvent.class, EventHandler::constructMod);
        modBus.addListener(EventPriority.LOWEST, FMLCommonSetupEvent.class,  EventHandler::commonSetup);
        modBus.addListener(EntityAttributeCreationEvent.class,      EventHandler::entityAttributeCreation);
        modBus.addListener(RegisterCapabilitiesEvent.class,         EventHandler::registerCapabilities);
        modBus.addListener(RegisterPayloadHandlersEvent.class,      EventHandler::registerPayloadHandlers);
        modBus.addListener(RegisterLockAndKeyBehaviorEvent.class,   EventHandler::registerLockAndKeyBehaviors);
        modBus.addListener(RegisterBibliocraftWoodTypesEvent.class, EventHandler::registerBibliocraftWoodTypes);
        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickBlock.class, EventHandler::rightClickBlock);
    }

    private static void constructMod(FMLConstructModEvent event) {
        ((BibliocraftWoodTypeRegistryImpl) BibliocraftApi.getWoodTypeRegistry()).register();
        BCRegistries.init(Objects.requireNonNull(ModList.get().getModContainerById(BibliocraftApi.MOD_ID).orElseThrow().getEventBus()));
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        ((LockAndKeyBehaviorsImpl) BibliocraftApi.getLockAndKeyBehaviors()).register();
    }

    private static void entityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(BCEntities.FANCY_ARMOR_STAND.get(), LivingEntity.createLivingAttributes().build());
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.BOOKCASE.get(),          BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.COOKIE_JAR.get(),        BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DINNER_PLATE.get(),      BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DISC_RACK.get(),         BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DISPLAY_CASE.get(),      BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.FANCY_ARMOR_STAND.get(), BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.FANCY_CRAFTER.get(),     BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.LABEL.get(),             BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.POTION_SHELF.get(),      BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.SHELF.get(),             BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.SWORD_PEDESTAL.get(),    BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.TABLE.get(),             BCBlockEntity::getCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.TOOL_RACK.get(),         BCBlockEntity::getCapability);
    }

    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar(BibliocraftApi.MOD_ID)
                .playToServer(BigBookSignPacket.TYPE,                 BigBookSignPacket.STREAM_CODEC,                 BigBookSignPacket::handle)
                .playToServer(BigBookSyncPacket.TYPE,                 BigBookSyncPacket.STREAM_CODEC,                 BigBookSyncPacket::handle)
                .playToServer(ClipboardSyncPacket.TYPE,               ClipboardSyncPacket.STREAM_CODEC,               ClipboardSyncPacket::handle)
                .playBidirectional(ClockSyncPacket.TYPE,              ClockSyncPacket.STREAM_CODEC,                   ClockSyncPacket::handle)
                .playToServer(FancySignSyncPacket.TYPE,               FancySignSyncPacket.STREAM_CODEC,               FancySignSyncPacket::handle)
                .playToClient(OpenBookInLecternPacket.TYPE,           OpenBookInLecternPacket.STREAM_CODEC,           OpenBookInLecternPacket::handle)
                .playToServer(SetBigBookPageInLecternPacket.TYPE,     SetBigBookPageInLecternPacket.STREAM_CODEC,     SetBigBookPageInLecternPacket::handle)
                .playToServer(StockroomCatalogSyncPacket.TYPE,        StockroomCatalogSyncPacket.STREAM_CODEC,        StockroomCatalogSyncPacket::handle)
                .playToServer(StockroomCatalogRequestListPacket.TYPE, StockroomCatalogRequestListPacket.STREAM_CODEC, StockroomCatalogRequestListPacket::handle)
                .playToClient(StockroomCatalogListPacket.TYPE,        StockroomCatalogListPacket.STREAM_CODEC,        StockroomCatalogListPacket::handle)
                .playToServer(TakeLecternBookPacket.TYPE,             TakeLecternBookPacket.STREAM_CODEC,             TakeLecternBookPacket::handle);
    }

    private static void registerLockAndKeyBehaviors(RegisterLockAndKeyBehaviorEvent event) {
        event.register(BaseContainerBlockEntity.class, be -> be.lockKey, (be, lock) -> be.lockKey = lock,    BaseContainerBlockEntity::getDisplayName);
        event.register(BeaconBlockEntity.class,        be -> be.lockKey, (be, lock) -> be.lockKey = lock,    BeaconBlockEntity::getDisplayName);
        event.register(BCBlockEntity.class,            BCBlockEntity::getLockKey, BCBlockEntity::setLockKey, BCUtil::getNameForBE);
    }

    private static void registerBibliocraftWoodTypes(RegisterBibliocraftWoodTypesEvent event) {
        registerVanilla(event, WoodType.OAK,      Blocks.OAK_PLANKS,      BlockFamilies.OAK_PLANKS);
        registerVanilla(event, WoodType.SPRUCE,   Blocks.SPRUCE_PLANKS,   BlockFamilies.SPRUCE_PLANKS);
        registerVanilla(event, WoodType.BIRCH,    Blocks.BIRCH_PLANKS,    BlockFamilies.BIRCH_PLANKS);
        registerVanilla(event, WoodType.JUNGLE,   Blocks.JUNGLE_PLANKS,   BlockFamilies.JUNGLE_PLANKS);
        registerVanilla(event, WoodType.ACACIA,   Blocks.ACACIA_PLANKS,   BlockFamilies.ACACIA_PLANKS);
        registerVanilla(event, WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS, BlockFamilies.DARK_OAK_PLANKS);
        registerVanilla(event, WoodType.CRIMSON,  Blocks.CRIMSON_PLANKS,  BlockFamilies.CRIMSON_PLANKS);
        registerVanilla(event, WoodType.WARPED,   Blocks.WARPED_PLANKS,   BlockFamilies.WARPED_PLANKS);
        registerVanilla(event, WoodType.MANGROVE, Blocks.MANGROVE_PLANKS, BlockFamilies.MANGROVE_PLANKS);
        registerVanilla(event, WoodType.BAMBOO,   Blocks.BAMBOO_PLANKS,   BlockFamilies.BAMBOO_PLANKS);
        registerVanilla(event, WoodType.CHERRY,   Blocks.CHERRY_PLANKS,   BlockFamilies.CHERRY_PLANKS);
    }

    /**
     * Private helper for registering the vanilla variants.
     */
    private static void registerVanilla(RegisterBibliocraftWoodTypesEvent event, WoodType woodType, Block planks, BlockFamily family) {
        event.register(BCUtil.mcLoc(woodType.name()), woodType, () -> BlockBehaviour.Properties.ofFullCopy(planks), BCUtil.mcLoc("block/" + woodType.name() + "_planks"), () -> family);
    }

    @SuppressWarnings("DataFlowIssue")
    private static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        ItemStack book = lectern.getBook();
        if (book.isEmpty()) {
            // this workaround is needed to set the page count correctly
            // if it can be added to vanilla/neo somehow, this can be scrapped
            ItemStack stack = player.getItemInHand(event.getHand());
            if (!stack.is(ItemTags.LECTERN_BOOKS)) return;
            if (!stack.has(BCDataComponents.BIG_BOOK_CONTENT) && !stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT) && !stack.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) return;
            lectern.setBook(stack.consumeAndReturn(1, player));
            LecternBlock.resetBookState(player, level, pos, state, true);
            level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1f, 1f);
            if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
                lectern.pageCount = stack.get(BCDataComponents.BIG_BOOK_CONTENT).pages().size();
            } else if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
                lectern.pageCount = stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT).pages().size();
            } else if (stack.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) {
                lectern.pageCount = 1;
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        } else if (book.has(BCDataComponents.BIG_BOOK_CONTENT) || book.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT) || book.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) {
            if (player.isSecondaryUseActive()) {
                LecternUtil.takeLecternBook(player, level, pos);
            } else if (!level.isClientSide() && player instanceof ServerPlayer sp) {
                PacketDistributor.sendToPlayer(sp, new OpenBookInLecternPacket(pos, book));
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
