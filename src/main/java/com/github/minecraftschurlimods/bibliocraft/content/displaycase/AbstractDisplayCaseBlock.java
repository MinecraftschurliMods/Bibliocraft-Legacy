package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.util.block.BCInteractibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDisplayCaseBlock extends BCInteractibleBlock {
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!canAccessFromDirection(state, hit.getDirection())) return InteractionResult.PASS;
        if (player.isSecondaryUseActive()) {
            level.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }
        if (!state.getValue(OPEN)) {
            level.setBlock(pos, state.setValue(OPEN, true), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }
        if (player.getItemInHand(hand).isEmpty() && level.getBlockEntity(pos) instanceof DisplayCaseBlockEntity dcbe && dcbe.getItem(0).isEmpty()) {
            level.setBlock(pos, state.setValue(OPEN, false), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayCaseBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }
}
