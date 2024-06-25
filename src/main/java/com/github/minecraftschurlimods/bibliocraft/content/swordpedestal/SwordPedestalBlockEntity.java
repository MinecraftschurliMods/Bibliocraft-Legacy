package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalBlockEntity extends BCBlockEntity {
    public static final String COLOR_KEY = "color";
    private int color = -1;

    public SwordPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SWORD_PEDESTAL.get(), 1, pos, state);
    }

    /**
     * @return The color of this sword pedestal.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of this sword pedestal.
     *
     * @param color The color to set.
     */
    public void setColor(int color) {
        this.color = color;
        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(COLOR_KEY)) {
            setColor(tag.getInt(COLOR_KEY));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(COLOR_KEY, color);
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
