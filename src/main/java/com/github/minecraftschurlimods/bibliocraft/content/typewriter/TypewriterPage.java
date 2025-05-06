package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;

public record TypewriterPage(List<String> lines, int line) {
    public static final int MAX_LINE_LENGTH = 16;
    public static final int MAX_LINES = 16;
    public static final Codec<TypewriterPage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.string(0, MAX_LINE_LENGTH).listOf(MAX_LINES, MAX_LINES).fieldOf("lines").forGetter(TypewriterPage::lines),
            ExtraCodecs.intRange(0, MAX_LINES).optionalFieldOf("line", 0).forGetter(TypewriterPage::line)
    ).apply(inst, TypewriterPage::new));
    public static final StreamCodec<ByteBuf, TypewriterPage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), TypewriterPage::lines,
            ByteBufCodecs.VAR_INT, TypewriterPage::line,
            TypewriterPage::new);
    public static final TypewriterPage DEFAULT = new TypewriterPage();

    public TypewriterPage() {
        this(BCUtil.extend(new ArrayList<>(MAX_LINES), MAX_LINES, ""), 0);
    }

    public TypewriterPage copy() {
        return new TypewriterPage(new ArrayList<>(lines), line);
    }

    public TypewriterPage withLine(int line) {
        return new TypewriterPage(lines, line);
    }
}
