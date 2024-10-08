package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the behaviors of the Lock and Key item for different block entities. Get via {@link BibliocraftApi#getLockAndKeyBehaviors()}.
 * Register a new behavior by subscribing to {@link RegisterLockAndKeyBehaviorEvent} and calling {@code register(...)} on it.
 * Make sure to put this call behind a {@code ModList.isLoaded("bibliocraft")} check, and in a separate class, to prevent accidental classloading (like you would with client classes).
 */
public interface LockAndKeyBehaviors {
    /**
     * @param blockEntity The {@link BlockEntity} to get the behavior for.
     * @return The {@link LockAndKeyBehavior} associated with the given {@link BlockEntity}, or null if no such {@link LockAndKeyBehavior} has been registered.
     * @param <T> The type of the {@link BlockEntity}.
     */
    @Nullable
    <T extends BlockEntity> LockAndKeyBehavior<T> get(T blockEntity);
}
