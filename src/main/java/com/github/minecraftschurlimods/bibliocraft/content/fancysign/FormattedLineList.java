package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record FormattedLineList(List<FormattedLine> lines) {
    public static final Codec<FormattedLineList> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedLine.CODEC.listOf().fieldOf("lines").forGetter(FormattedLineList::lines)
    ).apply(inst, FormattedLineList::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FormattedLineList> STREAM_CODEC = StreamCodec.composite(
            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()), FormattedLineList::lines,
            FormattedLineList::new);

    public static FormattedLineList withSize(int size) {
        return new FormattedLineList(BCUtil.extend(new ArrayList<>(), size, FormattedLine.DEFAULT));
    }

    public FormattedLineList update(int index, Function<FormattedLine, FormattedLine> function) {
        List<FormattedLine> newLines = new ArrayList<>(lines);
        newLines.set(index, function.apply(lines.get(index)));
        return new FormattedLineList(newLines);
    }
}
