package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record WrittenBigBookContent(List<List<FormattedLine>> pages, String title, String author, int generation) {
    public static final Codec<WrittenBigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.listOf().listOf().fieldOf("pages").forGetter(WrittenBigBookContent::pages),
            Codec.string(0, 32).fieldOf("title").forGetter(WrittenBigBookContent::title),
            Codec.STRING.fieldOf("author").forGetter(WrittenBigBookContent::author),
            ExtraCodecs.intRange(0, 3).optionalFieldOf("generation", 0).forGetter(WrittenBigBookContent::generation)
    ).apply(inst, WrittenBigBookContent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBigBookContent> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), WrittenBigBookContent::pages,
            ByteBufCodecs.stringUtf8(32), WrittenBigBookContent::title,
            ByteBufCodecs.STRING_UTF8, WrittenBigBookContent::author,
            ByteBufCodecs.VAR_INT, WrittenBigBookContent::generation,
            WrittenBigBookContent::new);
}
