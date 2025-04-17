package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record FancySignContent(List<FormattedLine> lines) {
    public static final Codec<FancySignContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.listOf().fieldOf("lines").forGetter(FancySignContent::lines)
    ).apply(inst, FancySignContent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FancySignContent> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()), FancySignContent::lines,
            FancySignContent::new);

    public static FancySignContent withSize(int size) {
        return new FancySignContent(BCUtil.extend(new ArrayList<>(), size, FormattedLine.DEFAULT));
    }
}
