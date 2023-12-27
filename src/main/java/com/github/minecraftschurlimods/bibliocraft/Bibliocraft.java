package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Bibliocraft.MOD_ID)
public final class Bibliocraft {
    public static final String MOD_ID = "bibliocraft";

    public Bibliocraft(IEventBus modBus) {
        BCRegistries.init(modBus);
    }
}
