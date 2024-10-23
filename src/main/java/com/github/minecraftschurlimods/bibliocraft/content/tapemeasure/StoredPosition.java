package com.github.minecraftschurlimods.bibliocraft.content.tapemeasure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;

public record StoredPosition(BlockPos position) {
    public static final Codec<StoredPosition> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockPos.CODEC.fieldOf("position").forGetter(StoredPosition::position)
    ).apply(inst, StoredPosition::new));
    public static final StreamCodec<ByteBuf, StoredPosition> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, StoredPosition::position,
            StoredPosition::new);
    public static final StoredPosition DEFAULT = new StoredPosition(BlockPos.ZERO);
}
