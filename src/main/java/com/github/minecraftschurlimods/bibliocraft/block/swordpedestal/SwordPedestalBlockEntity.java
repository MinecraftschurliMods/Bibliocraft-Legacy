package com.github.minecraftschurlimods.bibliocraft.block.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalBlockEntity extends BCBlockEntity {
    private static final String COLOR_KEY = "color";
    private int color = -1;

    /**
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public SwordPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SWORD_PEDESTAL.get(), 1, pos, state);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void loadTag(CompoundTag tag) {
        if (tag.contains(COLOR_KEY)) {
            color = tag.getInt(COLOR_KEY);
        }
        super.loadTag(tag);
    }

    @Override
    protected void saveTag(CompoundTag tag) {
        tag.putInt(COLOR_KEY, color);
        super.saveTag(tag);
    }
}
