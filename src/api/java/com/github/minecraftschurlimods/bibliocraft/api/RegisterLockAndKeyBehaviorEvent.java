package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RegisterLockAndKeyBehaviorEvent extends Event implements IModBusEvent {
    private final Map<Class<? extends BlockEntity>, LockAndKeyBehavior<? extends BlockEntity>> values;

    public RegisterLockAndKeyBehaviorEvent(Map<Class<? extends BlockEntity>, LockAndKeyBehavior<? extends BlockEntity>> values) {
        this.values = values;
    }
    
    public <T extends BlockEntity> void register(Class<T> clazz, Function<T, LockCode> lockGetter, BiConsumer<T, LockCode> lockSetter, Function<T, Component> nameGetter) {
        values.put(clazz, new LockAndKeyBehavior<>(lockGetter, lockSetter, nameGetter));
    }
}
