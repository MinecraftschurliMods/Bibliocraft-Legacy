package com.github.minecraftschurlimods.bibliocraft.handler;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.neoforged.fml.common.Mod;

public final class EventHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Bibliocraft.MOD_ID)
    public static final class NeoBus {
    }
}
