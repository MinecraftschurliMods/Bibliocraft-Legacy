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

public record FormattedLine(String text, Style style, int size, boolean shadow, boolean glowing, Alignment alignment) {
    public static final Codec<FormattedLine> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("text").forGetter(FormattedLine::text),
            Style.Serializer.CODEC.fieldOf("style").forGetter(FormattedLine::style),
            Codec.INT.fieldOf("size").forGetter(FormattedLine::size),
            Codec.BOOL.fieldOf("shadow").forGetter(FormattedLine::shadow),
            Codec.BOOL.fieldOf("glowing").forGetter(FormattedLine::glowing),
            Alignment.CODEC.fieldOf("alignment").forGetter(FormattedLine::alignment)
    ).apply(inst, FormattedLine::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FormattedLine> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, FormattedLine::text,
            Style.Serializer.TRUSTED_STREAM_CODEC, FormattedLine::style,
            ByteBufCodecs.INT, FormattedLine::size,
            ByteBufCodecs.BOOL, FormattedLine::shadow,
            ByteBufCodecs.BOOL, FormattedLine::glowing,
            Alignment.STREAM_CODEC, FormattedLine::alignment,
            FormattedLine::new);

    public static final FormattedLine DEFAULT = new FormattedLine("", Style.EMPTY, 10, false, false, Alignment.LEFT);

    public FormattedLine withText(String text) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public FormattedLine withStyle(Style style) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public FormattedLine withSize(int size) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public FormattedLine withShadow(boolean shadow) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public FormattedLine withGlowing(boolean glowing) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public FormattedLine withAlignment(Alignment alignment) {
        return new FormattedLine(text, style, size, shadow, glowing, alignment);
    }

    public enum Alignment implements StringRepresentable {
        LEFT, CENTER, RIGHT;

        public static final Codec<Alignment> CODEC = BCUtil.enumCodec(Alignment::values);
        public static final StreamCodec<ByteBuf, Alignment> STREAM_CODEC = BCUtil.enumStreamCodec(Alignment::values, Alignment::ordinal);

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
