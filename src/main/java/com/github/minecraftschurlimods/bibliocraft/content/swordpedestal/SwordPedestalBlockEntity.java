package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalBlockEntity extends BCBlockEntity {
    public static final String COLOR_KEY = DyeableLeatherItem.TAG_COLOR;
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
        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        }
    }

    @Override
    protected void loadTag(CompoundTag tag) {
        if (tag.contains(COLOR_KEY)) {
            setColor(tag.getInt(COLOR_KEY));
        }
        super.loadTag(tag);
    }

    @Override
    protected void saveTag(CompoundTag tag) {
        tag.putInt(COLOR_KEY, color);
        super.saveTag(tag);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.SWORD_PEDESTAL_SWORDS);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
