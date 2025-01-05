package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClockBlockEntity extends BlockEntity {
    private final List<ClockTrigger> triggers = new ArrayList<>();
    private final Multimap<Integer, ClockTrigger> triggersMap = HashMultimap.create();

    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.CLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClockBlockEntity blockEntity) {
        if (state.getValue(AbstractClockBlock.POWERED)) {
            setPowered(level, pos, false);
        }
        int time = (int) (level.getDayTime() % 24000);
        if (blockEntity.triggersMap.containsKey(time)) {
            Collection<ClockTrigger> trigger = blockEntity.triggersMap.get(time);
            if (trigger.stream().anyMatch(ClockTrigger::sound)) {
                level.playSound(null, pos, BCSoundEvents.CLOCK_CHIME.value(), SoundSource.BLOCKS, 1, 1);
            }
            if (trigger.stream().anyMatch(ClockTrigger::redstone)) {
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

    public List<ClockTrigger> getTriggers() {
        return Collections.unmodifiableList(triggers);
    }

    public void setFromPacket(ClockSyncPacket packet) {
        triggers.clear();
        triggersMap.clear();
        for (ClockTrigger trigger : packet.triggers()) {
            triggers.add(trigger);
            triggersMap.put(trigger.getInGameTime(), trigger);
        }
        triggers.sort(ClockTrigger::compareTo);
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), packet);
        }
    }

    private void addTrigger(ClockTrigger trigger) {
        triggers.add(trigger);
        triggers.sort(ClockTrigger::compareTo);
        triggersMap.put(trigger.getInGameTime(), trigger);
    }
}
