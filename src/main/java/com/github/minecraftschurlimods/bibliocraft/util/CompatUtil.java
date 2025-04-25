package com.github.minecraftschurlimods.bibliocraft.util;

import net.neoforged.fml.ModList;

/**
 * Contains various compatibility hooks.
 */
public final class CompatUtil {
    /**
     * @return Whether soul candles are expected to be present in the game.
     */
    public static boolean hasSoulCandles() {
        return ModList.get().isLoaded("buzzier_bees");
    }
}
