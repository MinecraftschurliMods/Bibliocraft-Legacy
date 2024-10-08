package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record LockAndKeyBehavior<T extends BlockEntity>(Function<T, LockCode> lockGetter, BiConsumer<T, LockCode> lockSetter, Function<T, Component> nameGetter) {
}
