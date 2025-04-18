package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.WrittenBookContent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record WrittenBigBookContent(List<List<FormattedLine>> pages, String title, String author, int generation, int currentPage) {
    public static final Codec<WrittenBigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.sizeLimitedListOf(256).listOf().fieldOf("pages").forGetter(WrittenBigBookContent::pages),
            Codec.string(0, WrittenBookContent.TITLE_MAX_LENGTH).fieldOf("title").forGetter(WrittenBigBookContent::title),
            Codec.STRING.fieldOf("author").forGetter(WrittenBigBookContent::author),
            ExtraCodecs.intRange(0, WrittenBookContent.MAX_GENERATION).optionalFieldOf("generation", 0).forGetter(WrittenBigBookContent::generation),
            ExtraCodecs.intRange(0, 255).optionalFieldOf("current_page", 0).forGetter(WrittenBigBookContent::currentPage)
    ).apply(inst, WrittenBigBookContent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBigBookContent> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), WrittenBigBookContent::pages,
            ByteBufCodecs.stringUtf8(WrittenBookContent.TITLE_MAX_LENGTH), WrittenBigBookContent::title,
            ByteBufCodecs.STRING_UTF8, WrittenBigBookContent::author,
            ByteBufCodecs.VAR_INT, WrittenBigBookContent::generation,
            ByteBufCodecs.VAR_INT, WrittenBigBookContent::currentPage,
            WrittenBigBookContent::new);
    public static final WrittenBigBookContent DEFAULT = new WrittenBigBookContent(List.of(), "", "", 0, 0);

    @Nullable
    public WrittenBigBookContent tryCraftCopy() {
        return generation >= WrittenBookContent.MAX_CRAFTABLE_GENERATION ? null : new WrittenBigBookContent(pages, title, author, generation + 1, currentPage);
    }
}
