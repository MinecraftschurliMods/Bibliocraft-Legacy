package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record BigBookContent(List<List<FormattedLine>> pages) {
    public static final Codec<BigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.sizeLimitedListOf(256).listOf().fieldOf("pages").forGetter(BigBookContent::pages)
    ).apply(inst, BigBookContent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookContent> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), BigBookContent::pages,
            BigBookContent::new);
}
