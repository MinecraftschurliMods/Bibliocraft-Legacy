package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FancyLampBlock extends AbstractFancyLightBlock {
    private static final VoxelShape NORTH_STANDING_SHAPE = ShapeUtil.combine(
            Shapes.box(0.3125, 0, 0.34375, 0.6875, 0.0625, 0.71875),
            Shapes.box(0.375, 0.0625, 0.40625, 0.625, 0.125, 0.65625),
            Shapes.box(0.4375, 0.125, 0.46875, 0.5625, 0.25, 0.59375),
            Shapes.box(0.46875, 0.25, 0.5, 0.53125, 0.89375, 0.75),
            Shapes.box(0.125, 0.71875, 0.0625, 0.875, 0.9375, 0.5),
            Shapes.box(0.125, 0.71875, 0, 0.875, 0.875, 0.0625),
            Shapes.box(0.125, 0.65625, 0, 0.875, 0.71875, 0.4375),
            Shapes.box(0.125, 0.59375, 0, 0.875, 0.65625, 0.21875));
    private static final VoxelShape EAST_STANDING_SHAPE = ShapeUtil.rotate(NORTH_STANDING_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_STANDING_SHAPE = ShapeUtil.rotate(NORTH_STANDING_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_STANDING_SHAPE = ShapeUtil.rotate(NORTH_STANDING_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape NORTH_HANGING_SHAPE = ShapeUtil.combine(
            Shapes.box(0.3125, 0.9375, 0.3125, 0.6875, 1, 0.6875),
            Shapes.box(0.375, 0.875, 0.375, 0.625, 0.9375, 0.625),
            Shapes.box(0.4375, 0.75, 0.4375, 0.5625, 0.875, 0.5625),
            Shapes.box(0.46875, 0.28125, 0.46875, 0.53125, 0.75, 0.53125),
            Shapes.box(0.125, 0.1875, 0.0625, 0.875, 0.40625, 0.5),
            Shapes.box(0.125, 0.1875, 0, 0.875, 0.34375, 0.0625),
            Shapes.box(0.125, 0.125, 0, 0.875, 0.1875, 0.4375),
            Shapes.box(0.125, 0.0625, 0, 0.875, 0.125, 0.21875),
            Shapes.box(0.125, 0.1875, 0.5, 0.875, 0.40625, 0.9375),
            Shapes.box(0.125, 0.1875, 0.9375, 0.875, 0.34375, 1),
            Shapes.box(0.125, 0.125, 0.5625, 0.875, 0.1875, 1),
            Shapes.box(0.125, 0.0625, 0.78125, 0.875, 0.125, 1));
    private static final VoxelShape EAST_HANGING_SHAPE = ShapeUtil.rotate(NORTH_HANGING_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_HANGING_SHAPE = ShapeUtil.rotate(NORTH_HANGING_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_HANGING_SHAPE = ShapeUtil.rotate(NORTH_HANGING_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape NORTH_WALL_SHAPE = ShapeUtil.combine(
            Shapes.box(0.4375, 0.09375, 0.9375, 0.5625, 0.34375, 1),
            Shapes.box(0.46875, 0.14375, 0.875, 0.53125, 0.26875, 0.9375),
            Shapes.box(0.46875, 0.2375, 0.5, 0.53125, 0.3625, 0.9375),
            Shapes.box(0.125, 0.1875, 0.0625, 0.875, 0.40625, 0.5),
            Shapes.box(0.125, 0.1875, 0, 0.875, 0.34375, 0.0625),
            Shapes.box(0.125, 0.125, 0, 0.875, 0.1875, 0.4375),
            Shapes.box(0.125, 0.0625, 0, 0.875, 0.125, 0.21875));
    private static final VoxelShape EAST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public FancyLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Type type = state.getValue(TYPE);
        Direction facing = state.getValue(FACING);
        return switch (type) {
            case STANDING -> switch (facing) {
                case EAST -> EAST_STANDING_SHAPE;
                case SOUTH -> SOUTH_STANDING_SHAPE;
                case WEST -> WEST_STANDING_SHAPE;
                default -> NORTH_STANDING_SHAPE;
            };
            case HANGING -> switch (facing) {
                case EAST -> EAST_HANGING_SHAPE;
                case SOUTH -> SOUTH_HANGING_SHAPE;
                case WEST -> WEST_HANGING_SHAPE;
                default -> NORTH_HANGING_SHAPE;
            };
            case WALL -> switch (facing) {
                case EAST -> EAST_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                default -> NORTH_WALL_SHAPE;
            };
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return BCUtil.nonNull(super.getStateForPlacement(context)).setValue(LIT, !context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.isClientSide()) return;
        boolean lit = state.getValue(LIT);
        if (lit != level.hasNeighborSignal(pos)) return;
        if (lit) {
            level.setBlock(pos, state.cycle(LIT), Block.UPDATE_CLIENTS);
        } else {
            level.scheduleTick(pos, this, 4);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(LIT), Block.UPDATE_CLIENTS);
        }
    }
}
