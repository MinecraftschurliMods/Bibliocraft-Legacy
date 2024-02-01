package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SeatBlockEntity extends BCBlockEntity {
    /**
     * @param pos   The position of this BE.
     * @param state The state of this BE.
     */
    public SeatBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SEAT.get(), 1, pos, state);
    }
}
