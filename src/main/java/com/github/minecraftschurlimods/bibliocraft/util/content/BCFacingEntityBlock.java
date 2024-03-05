package com.github.minecraftschurlimods.bibliocraft.util.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract superclass for entity blocks in this mod.
 */
@SuppressWarnings({"deprecation", "DuplicatedCode"})
public abstract class BCFacingEntityBlock extends BCFacingBlock implements BCBaseEntityBlock {
    public BCFacingEntityBlock(Properties properties) {
        super(properties);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        BCBaseEntityBlock.super.setPlacedBy(level, pos, state, entity, stack, () -> super.setPlacedBy(level, pos, state, entity, stack));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag) {
        BCBaseEntityBlock.super.onRemove(state, level, pos, newState, flag, this, () -> super.onRemove(state, level, pos, newState, flag));
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        return BCBaseEntityBlock.super.triggerEvent(state, level, pos, id, param, () -> super.triggerEvent(state, level, pos, id, param));
    }
}
