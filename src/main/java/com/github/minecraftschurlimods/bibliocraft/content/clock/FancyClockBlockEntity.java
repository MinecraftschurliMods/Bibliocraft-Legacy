package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FancyClockBlockEntity extends BlockEntity {
    public FancyClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_CLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FancyClockBlockEntity blockEntity) {
        int rotation = (int) (level.getDayTime() % 20);
        if (rotation != 0) return;
        if (level.getDayTime() % 40 == 0) {
            level.playSound(null, pos, BCSoundEvents.CLOCK_TICK.value(), SoundSource.BLOCKS, 1, 1);
        } else {
            level.playSound(null, pos, BCSoundEvents.CLOCK_TOCK.value(), SoundSource.BLOCKS, 1, 1);
        }
    }
}
