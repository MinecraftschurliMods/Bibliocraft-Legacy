package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.RegisterBibliocraftWoodTypesEvent;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardItemSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Objects;

public final class EventHandler {
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        private static void constructMod(FMLConstructModEvent event) {
            IEventBus bus = Objects.requireNonNull(ModList.get().getModContainerById(BibliocraftApi.MOD_ID).orElseThrow().getEventBus());
            ((BibliocraftWoodTypeRegistryImpl) BibliocraftApi.getWoodTypeRegistry()).register(bus);
            BCRegistries.init(Objects.requireNonNull(bus));
        }

        @SubscribeEvent
        private static void entityAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(BCEntities.FANCY_ARMOR_STAND.get(), LivingEntity.createLivingAttributes().build());
        }

        @SubscribeEvent
        private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
            event.registrar(BibliocraftApi.MOD_ID)
                    .playToServer(ClipboardItemSyncPacket.TYPE, ClipboardItemSyncPacket.STREAM_CODEC, ClipboardItemSyncPacket::handle);
        }

        @SubscribeEvent
        private static void registerBibliocraftWoodTypes(RegisterBibliocraftWoodTypesEvent event) {
            registerVanilla(event, WoodType.OAK, Blocks.OAK_PLANKS, BlockFamilies.OAK_PLANKS);
            registerVanilla(event, WoodType.SPRUCE, Blocks.SPRUCE_PLANKS, BlockFamilies.SPRUCE_PLANKS);
            registerVanilla(event, WoodType.BIRCH, Blocks.BIRCH_PLANKS, BlockFamilies.BIRCH_PLANKS);
            registerVanilla(event, WoodType.JUNGLE, Blocks.JUNGLE_PLANKS, BlockFamilies.JUNGLE_PLANKS);
            registerVanilla(event, WoodType.ACACIA, Blocks.ACACIA_PLANKS, BlockFamilies.ACACIA_PLANKS);
            registerVanilla(event, WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS, BlockFamilies.DARK_OAK_PLANKS);
            registerVanilla(event, WoodType.CRIMSON, Blocks.CRIMSON_PLANKS, BlockFamilies.CRIMSON_PLANKS);
            registerVanilla(event, WoodType.WARPED, Blocks.WARPED_PLANKS, BlockFamilies.WARPED_PLANKS);
            registerVanilla(event, WoodType.MANGROVE, Blocks.MANGROVE_PLANKS, BlockFamilies.MANGROVE_PLANKS);
            registerVanilla(event, WoodType.BAMBOO, Blocks.BAMBOO_PLANKS, BlockFamilies.BAMBOO_PLANKS);
            registerVanilla(event, WoodType.CHERRY, Blocks.CHERRY_PLANKS, BlockFamilies.CHERRY_PLANKS);
        }

        /**
         * Private helper for registering the vanilla variants.
         */
        private static void registerVanilla(RegisterBibliocraftWoodTypesEvent event, WoodType woodType, Block planks, BlockFamily family) {
            event.register(ResourceLocation.fromNamespaceAndPath("minecraft", woodType.name()), woodType, () -> BlockBehaviour.Properties.ofFullCopy(planks), ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + woodType.name() + "_planks"), family);
        }
    }
}
