package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public SeatEntity(Level level) {
        this(BCEntities.SEAT.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        BlockPos pos = blockPosition();
        BlockState state = level().getBlockState(pos);
        if (level().isClientSide()) return;
        if (!(state.getBlock() instanceof SeatBlock)) {
            getPassengers().forEach(Entity::stopRiding);
            discard();
        } else if (getPassengers().isEmpty()) {
            discard();
            if (state.getBlock() instanceof SeatBlock) {
                level().setBlockAndUpdate(pos, state.setValue(SeatBlock.OCCUPIED, false));
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }
}
