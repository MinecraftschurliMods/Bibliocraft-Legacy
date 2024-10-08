package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.LockAndKeyBehavior;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.LockAndKeyBehaviors;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.RegisterLockAndKeyBehaviorEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class LockAndKeyBehaviorsImpl implements LockAndKeyBehaviors {
    private final Map<Class<? extends BlockEntity>, LockAndKeyBehavior<? extends BlockEntity>> values = new HashMap<>();
    private boolean loaded = false;

    @ApiStatus.Internal
    public void register() {
        ModLoader.postEvent(new RegisterLockAndKeyBehaviorEvent(values));
        loaded = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T extends BlockEntity> LockAndKeyBehavior<T> get(T blockEntity) {
        if (!loaded)
            throw new IllegalStateException("Tried to access LockAndKeyBehaviors#get() before registration was done!");
        return (LockAndKeyBehavior<T>) values.keySet()
                .stream()
                .filter(e -> e.isAssignableFrom(blockEntity.getClass()))
                .map(values::get)
                .findAny()
                .orElse(null);
    }
}
