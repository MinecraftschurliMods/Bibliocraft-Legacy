package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the behaviors of the Lock and Key item for different block entities. Register a new behavior
 */
public interface LockAndKeyBehaviors {
    @Nullable
    <T extends BlockEntity> LockAndKeyBehavior<T> get(T blockEntity);
}
