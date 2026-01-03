package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ClockBlockEntity extends BlockEntity {
    private static final String TICK_SOUND_KEY = "tick";
    private static final String TRIGGERS_KEY = "triggers";
    private final List<ClockTrigger> triggers = new ArrayList<>();
    private final Multimap<Integer, ClockTrigger> triggersMap = HashMultimap.create();
    private int redstoneTick = 0;
    private boolean tickSound = true;

    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.CLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ClockBlockEntity blockEntity) {
        if (state.getValue(AbstractClockBlock.POWERED)) {
            blockEntity.redstoneTick--;
            if (blockEntity.redstoneTick <= 0) {
                setPowered(level, pos, false);
            }
        }
        int time = (int) (level.getDayTime() % 24000);
        if (blockEntity.triggersMap.containsKey(time)) {
            Collection<ClockTrigger> trigger = blockEntity.triggersMap.get(time);
            if (trigger.stream().anyMatch(ClockTrigger::sound)) {
                level.playSound(null, pos, BCSoundEvents.CLOCK_CHIME.value(), SoundSource.BLOCKS, 1, 1);
            }
            if (trigger.stream().anyMatch(ClockTrigger::redstone)) {
                blockEntity.redstoneTick = 2;
                setPowered(level, pos, true);
            }
        }
        if (blockEntity.getTickSound() && level instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.ADVANCE_TIME) && time % 20 == 0) {
            level.playSound(null, pos, time % 40 == 0 ? BCSoundEvents.CLOCK_TICK.value() : BCSoundEvents.CLOCK_TOCK.value(), SoundSource.BLOCKS, 1, 1);
        }
    }

    private static void setPowered(Level level, BlockPos pos, boolean powered) {
        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(AbstractClockBlock.POWERED)) return;
        level.setBlock(pos, state.setValue(AbstractClockBlock.POWERED, powered), Block.UPDATE_ALL);
        pos = pos.below();
        state = level.getBlockState(pos);
        if (state.getBlock() instanceof GrandfatherClockBlock) {
            level.setBlock(pos, state.setValue(AbstractClockBlock.POWERED, powered), Block.UPDATE_ALL);
        }
    }

    public List<ClockTrigger> getTriggers() {
        return Collections.unmodifiableList(triggers);
    }

    public void setFromPacket(ClockSyncPacket packet) {
        tickSound = packet.tickSound();
        addTriggers(packet.triggers());
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), packet);
        }
    }

    private void addTriggers(Collection<ClockTrigger> triggers) {
        this.triggers.clear();
        this.triggersMap.clear();
        for (ClockTrigger trigger : triggers) {
            this.triggers.add(trigger);
            this.triggersMap.put(trigger.getInGameTime(), trigger);
        }
        this.triggers.sort(ClockTrigger::compareTo);
        setChanged();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        tickSound = input.getBooleanOr(TICK_SOUND_KEY, true);
        input.list(TRIGGERS_KEY, ClockTrigger.CODEC).ifPresent(clockTriggers -> addTriggers(clockTriggers.stream().toList()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean(TICK_SOUND_KEY, tickSound);
        ValueOutput.TypedOutputList<ClockTrigger> list = output.list(TRIGGERS_KEY, ClockTrigger.CODEC);
        for (ClockTrigger trigger : triggers) {
            list.add(trigger);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    public boolean getTickSound() {
        return tickSound;
    }
}
