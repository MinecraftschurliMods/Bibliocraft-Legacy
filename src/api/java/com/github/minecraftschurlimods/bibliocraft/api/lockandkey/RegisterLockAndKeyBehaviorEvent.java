package com.github.minecraftschurlimods.bibliocraft.api.lockandkey;

import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/// Register your own [LockAndKeyBehavior]s here.
///
/// This event is not cancelable. This event is fired on the mod event bus.
public class RegisterLockAndKeyBehaviorEvent extends Event implements IModBusEvent {
    private final Map<Class<? extends BlockEntity>, LockAndKeyBehavior<? extends BlockEntity>> values;

    @ApiStatus.Internal
    public RegisterLockAndKeyBehaviorEvent(Map<Class<? extends BlockEntity>, LockAndKeyBehavior<? extends BlockEntity>> values) {
        this.values = values;
    }

    /// Registers a new [LockAndKeyBehavior].
    ///
    /// @param clazz      The class of the [BlockEntity] to register the behavior for.
    /// @param lockGetter The getter for the [BlockEntity]'s [LockCode].
    /// @param lockSetter The setter for the [BlockEntity]'s [LockCode].
    /// @param nameGetter The getter for the [BlockEntity]'s display name.
    /// @param <T>        The type of the [BlockEntity].
    public <T extends BlockEntity> void register(Class<T> clazz, Function<T, LockCode> lockGetter, BiConsumer<T, LockCode> lockSetter, Function<T, Component> nameGetter) {
        values.put(clazz, new LockAndKeyBehavior.Simple<>(lockGetter, lockSetter, nameGetter));
    }
}
