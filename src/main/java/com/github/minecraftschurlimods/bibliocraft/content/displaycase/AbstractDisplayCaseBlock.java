package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingInteractibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDisplayCaseBlock extends BCFacingInteractibleBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public AbstractDisplayCaseBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(OPEN, false));
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        return 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!canAccessFromDirection(state, hit.getDirection())) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (player.isSecondaryUseActive()) {
            level.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), Block.UPDATE_ALL);
            return ItemInteractionResult.SUCCESS;
        }
        if (!state.getValue(OPEN)) {
            level.setBlock(pos, state.setValue(OPEN, true), Block.UPDATE_ALL);
            return ItemInteractionResult.SUCCESS;
        }
        if (player.getItemInHand(hand).isEmpty() && level.getBlockEntity(pos) instanceof DisplayCaseBlockEntity dcbe && dcbe.getItem(0).isEmpty()) {
            level.setBlock(pos, state.setValue(OPEN, false), Block.UPDATE_ALL);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayCaseBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }
}
