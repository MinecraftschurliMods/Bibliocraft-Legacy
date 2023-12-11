package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.client.ber.PotionShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ToolRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseGeometryLoader;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreens;
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
        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(BCMenuTypes.FANCY_ARMOR_STAND.get(), BCMenuScreens.FancyArmorStand::new);
                MenuScreens.register(BCMenuTypes.BOOKCASE.get(), BCMenuScreens.Bookcase::new);
                MenuScreens.register(BCMenuTypes.POTION_SHELF.get(), BCMenuScreens.PotionShelf::new);
                MenuScreens.register(BCMenuTypes.SHELF.get(), BCMenuScreens.Shelf::new);
                MenuScreens.register(BCMenuTypes.TOOL_RACK.get(), BCMenuScreens.ToolRack::new);
            });
        }

        @SubscribeEvent
        static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(new ResourceLocation(Bibliocraft.MOD_ID, "bookcase"), BookcaseGeometryLoader.INSTANCE);
        }

        @SubscribeEvent
        static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(), PotionShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(), ShelfBER::new);
            event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(), ToolRackBER::new);
        }
    }
}
