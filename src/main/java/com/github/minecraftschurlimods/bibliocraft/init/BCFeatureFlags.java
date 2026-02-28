package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public interface BCFeatureFlags {
    /**
     * Feature flag for work-in-progress features.
     */
    FeatureFlag WIP = FeatureFlags.REGISTRY.getFlag(BCUtil.bcLoc("wip"));
    FeatureFlagSet WORK_IN_PROGRESS = FeatureFlagSet.of(WIP);
}
