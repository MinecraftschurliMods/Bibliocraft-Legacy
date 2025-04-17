package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TakeLecternBookPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<TakeLecternBookPacket> TYPE = new Type<>(BCUtil.modLoc("take_lectern_book"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
