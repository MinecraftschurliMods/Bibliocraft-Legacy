package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Holds all information Bibliocraft needs to make the Lock and Key item work on a block entity.
 * @param lockGetter The getter for the {@link BlockEntity}'s {@link LockCode}.
 * @param lockSetter The setter for the {@link BlockEntity}'s {@link LockCode}.
 * @param nameGetter The getter for the {@link BlockEntity}'s display name.
 * @param <T> The type of the {@link BlockEntity}.
 */
public record LockAndKeyBehavior<T extends BlockEntity>(Function<T, LockCode> lockGetter, BiConsumer<T, LockCode> lockSetter, Function<T, Component> nameGetter) {
}
