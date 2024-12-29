package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClockBlockEntity extends BlockEntity {
    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.CLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClockBlockEntity blockEntity) {
        if (level.getDayTime() % 20 != 0) return;
        level.playSound(null, pos, level.getDayTime() % 40 == 0 ? BCSoundEvents.CLOCK_TICK.value() : BCSoundEvents.CLOCK_TOCK.value(), SoundSource.BLOCKS, 1, 1);
    }
}
