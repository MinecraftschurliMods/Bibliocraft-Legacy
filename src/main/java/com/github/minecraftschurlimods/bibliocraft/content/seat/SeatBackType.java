package com.github.minecraftschurlimods.bibliocraft.content.seat;

import net.minecraft.util.StringRepresentable;

public enum SeatBackType implements StringRepresentable {
    SMALL,
    RAISED,
    FLAT,
    TALL,
    FANCY;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
