package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public record FormattedLine(String text, Style style, int size, Mode mode, Alignment alignment) {
    public static final Codec<FormattedLine> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("text").forGetter(FormattedLine::text),
            Style.Serializer.CODEC.fieldOf("style").forGetter(FormattedLine::style),
            Codec.INT.fieldOf("size").forGetter(FormattedLine::size),
            Mode.CODEC.fieldOf("mode").forGetter(FormattedLine::mode),
            Alignment.CODEC.fieldOf("alignment").forGetter(FormattedLine::alignment)
    ).apply(inst, FormattedLine::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FormattedLine> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, FormattedLine::text,
            Style.Serializer.TRUSTED_STREAM_CODEC, FormattedLine::style,
            ByteBufCodecs.INT, FormattedLine::size,
            Mode.STREAM_CODEC, FormattedLine::mode,
            Alignment.STREAM_CODEC, FormattedLine::alignment,
            FormattedLine::new);

    public static final FormattedLine DEFAULT = new FormattedLine("", Style.EMPTY, 10, Mode.NORMAL, Alignment.LEFT);

    public FormattedLine withText(String text) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withStyle(Style style) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withSize(int size) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withMode(Mode mode) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public FormattedLine withAlignment(Alignment alignment) {
        return new FormattedLine(text, style, size, mode, alignment);
    }

    public enum Mode implements StringRepresentable {
        NORMAL, SHADOW, GLOWING;
        public static final Codec<Mode> CODEC = BCUtil.enumCodec(Mode::values);
        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = BCUtil.enumStreamCodec(Mode::values, Mode::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum Alignment implements StringRepresentable {
        LEFT, CENTER, RIGHT;
        public static final Codec<Alignment> CODEC = BCUtil.enumCodec(Alignment::values);
        public static final StreamCodec<ByteBuf, Alignment> STREAM_CODEC = BCUtil.enumStreamCodec(Alignment::values, Alignment::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
