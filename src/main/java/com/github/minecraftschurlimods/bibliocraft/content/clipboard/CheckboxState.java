package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum CheckboxState implements StringRepresentable {
    EMPTY,
    CHECK,
    X;
    public static final Codec<CheckboxState> CODEC = BCUtil.enumCodec(CheckboxState::values);
    public static final StreamCodec<ByteBuf, CheckboxState> STREAM_CODEC = BCUtil.enumStreamCodec(CheckboxState::values, CheckboxState::ordinal);

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
