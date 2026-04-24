package com.github.minecraftschurlimods.bibliocraft.api.lockandkey;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

/// Holds the behaviors of the Lock and Key item for different block entities. Get via [BibliocraftApi#getLockAndKeyBehaviors()].
/// Register a new behavior by subscribing to [RegisterLockAndKeyBehaviorEvent] and calling `register(...)` on it.
/// Make sure to put this call behind a `ModList.isLoaded("bibliocraft")` check, and in a separate class, to prevent accidental classloading (like you would with client classes).
public interface LockAndKeyBehaviors {
    /// @param blockEntity The [BlockEntity] to get the behavior for.
    /// @param <T>         The type of the [BlockEntity].
    /// @return The [LockAndKeyBehavior] associated with the given [BlockEntity], or null if no such [LockAndKeyBehavior] has been registered.
    @Nullable
    <T extends BlockEntity> LockAndKeyBehavior<T> get(T blockEntity);
}
