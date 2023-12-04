package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.client.ber.PotionShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseGeometryLoader;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreen;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public final class ClientHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        private static final ResourceLocation BOOKCASE_BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/bookcase.png");
        private static final ResourceLocation POTION_SHELF_BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/potion_shelf.png");
        private static final ResourceLocation TOOL_RACK_BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/tool_rack.png");

        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(BCMenuTypes.BOOKCASE.get(), (menu, inv, title) -> new BCMenuScreen<>(menu, inv, title, BOOKCASE_BACKGROUND));
                MenuScreens.register(BCMenuTypes.POTION_SHELF.get(), (menu, inv, title) -> new BCMenuScreen<>(menu, inv, title, POTION_SHELF_BACKGROUND));
                MenuScreens.register(BCMenuTypes.TOOL_RACK.get(), (menu, inv, title) -> new BCMenuScreen<>(menu, inv, title, TOOL_RACK_BACKGROUND));
            });
        }

        @SubscribeEvent
        static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register("bookcase", BookcaseGeometryLoader.INSTANCE);
        }

        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(), PotionShelfBER::new);
        }
    }
/*
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Bibliocraft.MOD_ID)
    public static final class NeoBus {
    }
*/
}
