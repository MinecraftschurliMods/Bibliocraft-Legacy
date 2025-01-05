package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
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
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClockBlockEntity extends BlockEntity {
    private static final String TICK_SOUND_KEY = "tick";
    private static final String TRIGGERS_KEY = "triggers";
    private final List<ClockTrigger> triggers = new ArrayList<>();
    private final Multimap<Integer, ClockTrigger> triggersMap = HashMultimap.create();
    public boolean tickSound = true;

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
        if (blockEntity.tickSound && time % 20 == 0) {
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
        addTriggers(packet.triggers());
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), packet);
        }
    }

    private void addTrigger(ClockTrigger trigger) {
        triggers.add(trigger);
        triggers.sort(ClockTrigger::compareTo);
        triggersMap.put(trigger.getInGameTime(), trigger);
    }

    private void addTriggers(Collection<ClockTrigger> triggers) {
        this.triggers.clear();
        this.triggersMap.clear();
        for (ClockTrigger trigger : triggers) {
            this.triggers.add(trigger);
            this.triggersMap.put(trigger.getInGameTime(), trigger);
        }
        this.triggers.sort(ClockTrigger::compareTo);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tickSound = tag.getBoolean(TICK_SOUND_KEY);
        addTriggers(BCUtil.decodeNbt(ClockTrigger.LIST_CODEC, tag.getCompound(TRIGGERS_KEY)));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean(TICK_SOUND_KEY, tickSound);
        tag.put(TRIGGERS_KEY, BCUtil.encodeNbt(ClockTrigger.LIST_CODEC, triggers));
    }
}
