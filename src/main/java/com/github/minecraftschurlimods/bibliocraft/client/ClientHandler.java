package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BookcaseScreen;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(BCMenuTypes.BOOKCASE.get(), BookcaseScreen::new);
        }
    }
/*
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Bibliocraft.MOD_ID)
    public static final class NeoBus {
    }
*/
}
