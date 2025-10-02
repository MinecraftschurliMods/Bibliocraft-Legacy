package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
}
