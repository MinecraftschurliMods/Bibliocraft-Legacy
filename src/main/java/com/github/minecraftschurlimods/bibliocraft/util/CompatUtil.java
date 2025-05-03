package com.github.minecraftschurlimods.bibliocraft.util;

import net.neoforged.fml.ModList;

/**
 * Contains various compatibility hooks.
 */
public final class CompatUtil {
    /**
     * @return Whether Bibliocraft should register its own config screen or not.
     */
    public static boolean hasConfigScreen() {
        return !ModList.get().isLoaded("configured");
    }

    /**
     * @return Whether soul candles are expected to be present in the game.
     */
    public static boolean hasSoulCandles() {
        return ModList.get().isLoaded("buzzier_bees");
    }
}
