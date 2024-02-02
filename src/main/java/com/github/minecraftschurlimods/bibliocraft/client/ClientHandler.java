package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DisplayCaseBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.FancyArmorStandBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.LabelBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.PotionShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.SwordPedestalBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ToolRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseGeometryLoader;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreens;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Objects;

public final class ClientHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        private static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(BCMenus.BOOKCASE.get(), BCMenuScreens.Bookcase::new);
            event.register(BCMenus.FANCY_ARMOR_STAND.get(), BCMenuScreens.FancyArmorStand::new);
            event.register(BCMenus.LABEL.get(), BCMenuScreens.Label::new);
            event.register(BCMenus.POTION_SHELF.get(), BCMenuScreens.PotionShelf::new);
            event.register(BCMenus.SHELF.get(), BCMenuScreens.Shelf::new);
            event.register(BCMenus.TOOL_RACK.get(), BCMenuScreens.ToolRack::new);
        }

        @SubscribeEvent
        private static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(new ResourceLocation(Bibliocraft.MOD_ID, "bookcase"), BookcaseGeometryLoader.INSTANCE);
        }

        @SubscribeEvent
        private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BCEntities.FANCY_ARMOR_STAND.get(), ArmorStandRenderer::new);
            event.registerEntityRenderer(BCEntities.SEAT.get(), EmptyEntityRenderer::new);
            event.registerBlockEntityRenderer(BCBlockEntities.DISPLAY_CASE.get(), DisplayCaseBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.FANCY_ARMOR_STAND.get(), FancyArmorStandBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.LABEL.get(), LabelBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(), PotionShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(), ShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.SWORD_PEDESTAL.get(), SwordPedestalBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(), ToolRackBER::new);
        }

        @SubscribeEvent
        private static void registerColorHandlersBlock(RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> tintIndex == 0 && Objects.requireNonNull(level).getBlockEntity(Objects.requireNonNull(pos)) instanceof SwordPedestalBlockEntity spbe ? spbe.getColor() : -1, BCBlocks.SWORD_PEDESTAL.get());
        }

        @SubscribeEvent
        private static void registerColorHandlersItem(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> tintIndex == 0 ? BCUtil.getNBTColor(stack, DyeColor.GREEN.getTextColor()) : -1, BCItems.SWORD_PEDESTAL.get());
        }
    }
}
