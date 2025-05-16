package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum CheckboxState implements StringRepresentableEnum {
    EMPTY, CHECK, X;
    public static final Codec<CheckboxState> CODEC = CodecUtil.enumCodec(CheckboxState::values);
    public static final StreamCodec<ByteBuf, CheckboxState> STREAM_CODEC = CodecUtil.enumStreamCodec(CheckboxState::values, CheckboxState::ordinal);
}
