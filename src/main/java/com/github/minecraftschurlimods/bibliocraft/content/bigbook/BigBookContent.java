package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record BigBookContent(List<List<FormattedLine>> pages, int currentPage) {
    public static final Codec<BigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.sizeLimitedListOf(256).listOf().fieldOf("pages").forGetter(BigBookContent::pages),
            ExtraCodecs.intRange(0, 255).optionalFieldOf("current_page", 0).forGetter(BigBookContent::currentPage)
    ).apply(inst, BigBookContent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookContent> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), BigBookContent::pages,
            ByteBufCodecs.VAR_INT, BigBookContent::currentPage,
            BigBookContent::new);
}
