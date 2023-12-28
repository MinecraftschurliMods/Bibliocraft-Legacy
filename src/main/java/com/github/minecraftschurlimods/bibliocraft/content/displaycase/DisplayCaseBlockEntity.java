package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayCaseBlockEntity extends BCBlockEntity {
    /**
     * @param pos   The position of this BE.
     * @param state The state of this BE.
     */
    public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.DISPLAY_CASE.get(), 1, pos, state);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
