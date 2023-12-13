package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Bibliocraft.MOD_ID)
public final class Bibliocraft {
    public static final String MOD_ID = "bibliocraft";
    public static final Logger LOGGER = LogManager.getLogger();

    public Bibliocraft(IEventBus modBus) {
        BCRegistries.init(modBus);
    }
}
