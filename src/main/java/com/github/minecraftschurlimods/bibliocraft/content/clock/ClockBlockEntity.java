package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClockBlockEntity extends BlockEntity {
    private final List<ClockTrigger> triggers = new ArrayList<>();
    private final Map<Integer, ClockTrigger> triggersMap = new HashMap<>();

    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.CLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClockBlockEntity blockEntity) {
        if (state.getValue(AbstractClockBlock.POWERED)) {
            setPowered(level, pos, false);
        }
        int time = (int) (level.getDayTime() % 24000);
        if (blockEntity.triggersMap.containsKey(time)) {
            ClockTrigger trigger = blockEntity.triggersMap.get(time);
            if (trigger.sound()) {
                level.playSound(null, pos, BCSoundEvents.CLOCK_CHIME.value(), SoundSource.BLOCKS, 1, 1);
            }
            if (trigger.redstone()) {
                setPowered(level, pos, true);
            }
        }
        if (time % 20 == 0) {
            level.playSound(null, pos, time % 40 == 0 ? BCSoundEvents.CLOCK_TICK.value() : BCSoundEvents.CLOCK_TOCK.value(), SoundSource.BLOCKS, 1, 1);
        }
    }

    private static void setPowered(Level level, BlockPos pos, boolean powered) {
        level.setBlock(pos, level.getBlockState(pos).setValue(AbstractClockBlock.POWERED, powered), Block.UPDATE_ALL);
        if (level.getBlockState(pos.below()).getBlock() instanceof GrandfatherClockBlock) {
            level.setBlock(pos.below(), level.getBlockState(pos.below()).setValue(AbstractClockBlock.POWERED, powered), Block.UPDATE_ALL);
        }
    }
}
