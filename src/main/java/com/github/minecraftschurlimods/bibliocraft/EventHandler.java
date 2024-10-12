package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.RegisterLockAndKeyBehaviorEvent;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.RegisterBibliocraftWoodTypesEvent;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.LockAndKeyBehaviorsImpl;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogRequestListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Objects;

public final class EventHandler {
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        private static void constructMod(FMLConstructModEvent event) {
            ((BibliocraftWoodTypeRegistryImpl) BibliocraftApi.getWoodTypeRegistry()).register();
            BCRegistries.init(Objects.requireNonNull(ModList.get().getModContainerById(BibliocraftApi.MOD_ID).orElseThrow().getEventBus()));
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        private static void commonSetup(FMLCommonSetupEvent event) {
            ((LockAndKeyBehaviorsImpl) BibliocraftApi.getLockAndKeyBehaviors()).register();
        }

        @SubscribeEvent
        private static void entityAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(BCEntities.FANCY_ARMOR_STAND.get(), LivingEntity.createLivingAttributes().build());
        }

        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.BOOKCASE.get(),          BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.COOKIE_JAR.get(),        BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DINNER_PLATE.get(),      BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DISC_RACK.get(),         BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.DISPLAY_CASE.get(),      BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.FANCY_ARMOR_STAND.get(), BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.LABEL.get(),             BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.POTION_SHELF.get(),      BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.SHELF.get(),             BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.SWORD_PEDESTAL.get(),    BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.TABLE.get(),             BCBlockEntity::getCapability);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BCBlockEntities.TOOL_RACK.get(),         BCBlockEntity::getCapability);
        }

        @SubscribeEvent
        private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
            event.registrar(BibliocraftApi.MOD_ID)
                    .playToServer(ClipboardSyncPacket.TYPE,               ClipboardSyncPacket.STREAM_CODEC,               ClipboardSyncPacket::handle)
                    .playToServer(StockroomCatalogSyncPacket.TYPE,        StockroomCatalogSyncPacket.STREAM_CODEC,        StockroomCatalogSyncPacket::handle)
                    .playToServer(StockroomCatalogRequestListPacket.TYPE, StockroomCatalogRequestListPacket.STREAM_CODEC, StockroomCatalogRequestListPacket::handle)
                    .playToClient(StockroomCatalogListPacket.TYPE,        StockroomCatalogListPacket.STREAM_CODEC,        StockroomCatalogListPacket::handle);
        }

        @SubscribeEvent
        private static void registerLockAndKeyBehaviors(RegisterLockAndKeyBehaviorEvent event) {
            event.register(BaseContainerBlockEntity.class, be -> be.lockKey, (be, lock) -> be.lockKey = lock,    BaseContainerBlockEntity::getDisplayName);
            event.register(BeaconBlockEntity.class,        be -> be.lockKey, (be, lock) -> be.lockKey = lock,    BeaconBlockEntity::getDisplayName);
            event.register(BCBlockEntity.class,            BCBlockEntity::getLockKey, BCBlockEntity::setLockKey, be -> BCUtil.getNameAtPos(Objects.requireNonNull(be.getLevel()), be.getBlockPos()));
        }
        
        @SubscribeEvent
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
            event.register(BCUtil.mcLoc(woodType.name()), woodType, () -> BlockBehaviour.Properties.ofFullCopy(planks), BCUtil.mcLoc("block/" + woodType.name() + "_planks"), family);
        }
    }
}
