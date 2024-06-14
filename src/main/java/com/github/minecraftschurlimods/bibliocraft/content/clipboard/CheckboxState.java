package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Locale;

public enum CheckboxState implements StringRepresentable {
    EMPTY,
    CHECK,
    CROSS;

    public static final Codec<CheckboxState> CODEC = StringRepresentable.fromEnum(CheckboxState::values);
    public static final StreamCodec<FriendlyByteBuf, CheckboxState> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(CheckboxState.class);

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public CheckboxState next() {
        return switch (this) {
            case EMPTY -> CheckboxState.CHECK;
            case CHECK -> CheckboxState.CROSS;
            case CROSS -> CheckboxState.EMPTY;
        };
    }
}
