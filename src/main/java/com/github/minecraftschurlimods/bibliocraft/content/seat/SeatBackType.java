package com.github.minecraftschurlimods.bibliocraft.content.seat;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum SeatBackType implements StringRepresentable {
    SMALL,
    RAISED,
    FLAT,
    TALL,
    FANCY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
