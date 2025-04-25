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

    /**
     * @return Whether ender candles are expected to be present in the game.
     */
    public static boolean hasEnderCandles() {
        return ModList.get().isLoaded("buzzier_bees") && ModList.get().isLoaded("endergetic");
    }

    /**
     * @return Whether cupric candles are expected to be present in the game.
     */
    public static boolean hasCupricCandles() {
        return ModList.get().isLoaded("buzzier_bees") && ModList.get().isLoaded("caverns_and_chasms");
    }
}
