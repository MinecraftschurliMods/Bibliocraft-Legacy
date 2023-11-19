package com.github.minecraftschurlimods.bibliocraft.handler;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;

public final class EventHandler {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus neoBus = NeoForge.EVENT_BUS;
    }
}
