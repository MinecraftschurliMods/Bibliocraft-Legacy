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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.Objects;

public final class ClientHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(BCMenus.BOOKCASE.get(), BCMenuScreens.Bookcase::new);
                MenuScreens.register(BCMenus.FANCY_ARMOR_STAND.get(), BCMenuScreens.FancyArmorStand::new);
                MenuScreens.register(BCMenus.LABEL.get(), BCMenuScreens.Label::new);
                MenuScreens.register(BCMenus.POTION_SHELF.get(), BCMenuScreens.PotionShelf::new);
                MenuScreens.register(BCMenus.SHELF.get(), BCMenuScreens.Shelf::new);
                MenuScreens.register(BCMenus.TOOL_RACK.get(), BCMenuScreens.ToolRack::new);
            });
        }

        @SubscribeEvent
        static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(new ResourceLocation(Bibliocraft.MOD_ID, "bookcase"), BookcaseGeometryLoader.INSTANCE);
        }

        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BCEntities.FANCY_ARMOR_STAND.get(), ArmorStandRenderer::new);
            event.registerBlockEntityRenderer(BCBlockEntities.DISPLAY_CASE.get(), DisplayCaseBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.FANCY_ARMOR_STAND.get(), FancyArmorStandBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.LABEL.get(), LabelBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(), PotionShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(), ShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.SWORD_PEDESTAL.get(), SwordPedestalBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(), ToolRackBER::new);
        }

        @SubscribeEvent
        static void registerColorHandlersBlock(RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> tintIndex == 0 && Objects.requireNonNull(level).getBlockEntity(Objects.requireNonNull(pos)) instanceof SwordPedestalBlockEntity spbe ? spbe.getColor() : -1, BCBlocks.SWORD_PEDESTAL.get());
        }

        @SubscribeEvent
        static void registerColorHandlersItem(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> tintIndex == 0 ? BCUtil.getNBTColor(stack, DyeColor.GREEN.getTextColor()) : -1, BCItems.SWORD_PEDESTAL.get());
        }
    }
}
