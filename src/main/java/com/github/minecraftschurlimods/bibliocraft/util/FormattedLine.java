package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record FormattedLine(String text, Style style, int size, Mode mode, Alignment alignment) {
    public static final int MIN_SIZE = 5;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 35;
    public static final Codec<FormattedLine> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.optionalFieldOf("text", "").forGetter(FormattedLine::text),
            Style.Serializer.CODEC.optionalFieldOf("style", Style.EMPTY).forGetter(FormattedLine::style),
            ExtraCodecs.intRange(MIN_SIZE, MAX_SIZE).optionalFieldOf("size", DEFAULT_SIZE).forGetter(FormattedLine::size),
            Mode.CODEC.optionalFieldOf("mode", Mode.NORMAL).forGetter(FormattedLine::mode),
            Alignment.CODEC.optionalFieldOf("alignment", Alignment.LEFT).forGetter(FormattedLine::alignment)
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

    public enum Mode implements StringRepresentableEnum {
        NORMAL, SHADOW, GLOWING;
        public static final Codec<Mode> CODEC = CodecUtil.enumCodec(Mode::values);
        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = CodecUtil.enumStreamCodec(Mode::values, Mode::ordinal);

        public String getTranslationKey() {
            return "gui." + BibliocraftApi.MOD_ID + ".formatted_line.mode." + getSerializedName();
        }
    }

    public enum Alignment implements StringRepresentableEnum {
        LEFT, CENTER, RIGHT;
        public static final Codec<Alignment> CODEC = CodecUtil.enumCodec(Alignment::values);
        public static final StreamCodec<ByteBuf, Alignment> STREAM_CODEC = CodecUtil.enumStreamCodec(Alignment::values, Alignment::ordinal);

        public String getTranslationKey() {
            return "gui." + BibliocraftApi.MOD_ID + ".formatted_line.alignment." + getSerializedName();
        }
    }
}
