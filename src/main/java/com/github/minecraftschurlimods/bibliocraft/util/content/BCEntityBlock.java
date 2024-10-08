package com.github.minecraftschurlimods.bibliocraft.util.content;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract superclass for non-rotatable entity blocks in this mod.
 */
@SuppressWarnings({"DuplicatedCode"})
public abstract class BCEntityBlock extends BCWaterloggedBlock implements EntityBlock {
    public BCEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        if (level.getBlockEntity(pos) instanceof BCMenuBlockEntity blockEntity && stack.has(DataComponents.CUSTOM_NAME)) {
            blockEntity.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, flag);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) return InteractionResult.PASS;
        BCUtil.openBEMenu(player, level, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(id, param);
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }
}
