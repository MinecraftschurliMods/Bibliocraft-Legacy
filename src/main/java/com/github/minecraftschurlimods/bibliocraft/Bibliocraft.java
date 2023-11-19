package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.handler.EventHandler;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Bibliocraft.MOD_ID)
public final class Bibliocraft {
    public static final String MOD_ID = "bibliocraft";
    public static final Logger LOGGER = LogManager.getLogger();

    public Bibliocraft() {
        BCRegistries.init();
        EventHandler.init();
    }
}
