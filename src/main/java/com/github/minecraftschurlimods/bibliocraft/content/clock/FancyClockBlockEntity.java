package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FancyClockBlockEntity extends BlockEntity {
    public FancyClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_CLOCK.get(), pos, state);
    }
}
