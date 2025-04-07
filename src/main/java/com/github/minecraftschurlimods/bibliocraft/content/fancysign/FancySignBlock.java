package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Objects;

public class FancySignBlock extends AbstractFancySignBlock {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    private static final VoxelShape Z_SHAPE = ShapeUtil.combine(
            Shapes.box(0.9375, 0.1875, 0.4375, 1, 0.8125, 0.5625),
            Shapes.box(0, 0.1875, 0.4375, 0.0625, 0.8125, 0.5625),
            Shapes.box(0.0625, 0.75, 0.4375, 0.9375, 0.8125, 0.5625),
            Shapes.box(0.0625, 0.1875, 0.4375, 0.9375, 0.25, 0.5625),
            Shapes.box(0.0625, 0.25, 0.46875, 0.9375, 0.75, 0.53125),
            Shapes.box(0.625, 0, 0.4375, 0.75, 0.0625, 0.5625),
            Shapes.box(0.65625, 0.0625, 0.46875, 0.71875, 0.1875, 0.53125),
            Shapes.box(0.25, 0, 0.4375, 0.375, 0.0625, 0.5625),
            Shapes.box(0.28125, 0.0625, 0.46875, 0.34375, 0.1875, 0.53125));
    private static final VoxelShape X_SHAPE = ShapeUtil.rotate(Z_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape Z_SHAPE_HANGING = ShapeUtil.combine(
            Shapes.box(0.9375, 0.1875, 0.4375, 1, 0.8125, 0.5625),
            Shapes.box(0, 0.1875, 0.4375, 0.0625, 0.8125, 0.5625),
            Shapes.box(0.0625, 0.75, 0.4375, 0.9375, 0.8125, 0.5625),
            Shapes.box(0.0625, 0.1875, 0.4375, 0.9375, 0.25, 0.5625),
            Shapes.box(0.0625, 0.25, 0.46875, 0.9375, 0.75, 0.53125),
            Shapes.box(0.625, 0.9375, 0.4375, 0.75, 1, 0.5625),
            Shapes.box(0.65625, 0.8125, 0.46875, 0.71875, 0.9375, 0.53125),
            Shapes.box(0.25, 0.9375, 0.4375, 0.375, 1, 0.5625),
            Shapes.box(0.28125, 0.8125, 0.46875, 0.34375, 0.9375, 0.53125));
    private static final VoxelShape X_SHAPE_HANGING = ShapeUtil.rotate(Z_SHAPE_HANGING, Rotation.CLOCKWISE_90);

    public FancySignBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(HANGING, false).setValue(UPSIDE_DOWN, false).setValue(WAXED, false).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HANGING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(HANGING, context.getClickedFace() == Direction.DOWN);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HANGING)) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_HANGING : Z_SHAPE_HANGING;
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(WAXED) || player.isSecondaryUseActive()) return InteractionResult.PASS;
        Direction direction = hit.getDirection();
        if (level.isClientSide() && direction.getAxis() == state.getValue(FACING).getAxis()) {
            ClientUtil.openFancySignScreen(pos, direction == state.getValue(FACING));
        }
        return InteractionResult.SUCCESS;
    }
}
