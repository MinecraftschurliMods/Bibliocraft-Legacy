package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TypewriterBlock extends BCFacingEntityBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.125, 0, 0.125, 0.875, 0.125, 0.875),
            Shapes.box(0.125, 0.125, 0.21875, 0.875, 0.15625, 0.875),
            Shapes.box(0.125, 0.15625, 0.3125, 0.875, 0.1875, 0.875),
            Shapes.box(0.125, 0.1875, 0.40625, 0.875, 0.21875, 0.875),
            Shapes.box(0.125, 0.21875, 0.5, 0.25, 0.375, 0.6875),
            Shapes.box(0.75, 0.21875, 0.5, 0.875, 0.375, 0.6875),
            Shapes.box(0.125, 0.21875, 0.6875, 0.875, 0.375, 0.875),
            Shapes.box(0.125, 0.375, 0.6875, 0.25, 0.5625, 0.875),
            Shapes.box(0.75, 0.375, 0.6875, 0.875, 0.5625, 0.875),
            Shapes.box(0.25, 0.375, 0.84375, 0.75, 0.5625, 0.875),
            Shapes.box(0.0625, 0.40625, 0.71875, 0.9375, 0.53125, 0.84375));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape NORTH_COLLISION_SHAPE = ShapeUtil.combine(
            Shapes.box(0.125, 0, 0.125, 0.875, 0.15625, 0.875),
            Shapes.box(0.125, 0.15625, 0.3125, 0.875, 0.21875, 0.875),
            Shapes.box(0.125, 0.21875, 0.5, 0.875, 0.375, 0.875),
            Shapes.box(0.0625, 0.375, 0.6875, 0.9375, 0.5625, 0.875));
    private static final VoxelShape EAST_COLLISION_SHAPE = ShapeUtil.rotate(NORTH_COLLISION_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_COLLISION_SHAPE = ShapeUtil.rotate(NORTH_COLLISION_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_COLLISION_SHAPE = ShapeUtil.rotate(NORTH_COLLISION_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    public static final IntegerProperty PAPER = IntegerProperty.create("paper", 0, 8);

    public TypewriterBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(PAPER, 0).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> NORTH_COLLISION_SHAPE;
            case EAST -> EAST_COLLISION_SHAPE;
            case SOUTH -> SOUTH_COLLISION_SHAPE;
            case WEST -> WEST_COLLISION_SHAPE;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PAPER);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TypewriterBlockEntity(pos, state);
    }
}
