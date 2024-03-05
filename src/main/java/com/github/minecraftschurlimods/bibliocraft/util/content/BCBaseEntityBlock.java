package com.github.minecraftschurlimods.bibliocraft.util.content;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Superinterface for reducing code duplication across {@link BCEntityBlock} and {@link BCFacingEntityBlock}.
 */
@SuppressWarnings("unused")
public interface BCBaseEntityBlock extends EntityBlock {
    default void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack, Runnable supermethod) {
        supermethod.run();
        if (level.getBlockEntity(pos) instanceof BCMenuBlockEntity blockEntity && stack.hasCustomHoverName()) {
            blockEntity.setCustomName(stack.getHoverName());
        }
    }

    default void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag, Block self, Runnable supermethod) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, self);
            }
            supermethod.run();
        }
    }

    default InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return player.isSecondaryUseActive() ? InteractionResult.PASS : BCUtil.openBEMenu(player, level, pos);
    }

    default boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param, Runnable supermethod) {
        supermethod.run();
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity != null && blockentity.triggerEvent(id, param);
    }

    @Nullable
    default MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }
}
