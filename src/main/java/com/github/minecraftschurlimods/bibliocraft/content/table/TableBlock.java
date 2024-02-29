package com.github.minecraftschurlimods.bibliocraft.content.table;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Locale;

@SuppressWarnings("deprecation")
public class TableBlock extends BCBlock {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);
    private static final VoxelShape NONE_SHAPE = ShapeUtil.combine(
            Shapes.box(0.0625, 0.875, 0, 0.9375, 1, 1),
            Shapes.box(0, 0.875, 0.0625, 0.0625, 1, 0.9375),
            Shapes.box(0.9375, 0.875, 0.0625, 1, 1, 0.9375),
            Shapes.box(0.4375, 0.0625, 0.4375, 0.5625, 0.875, 0.5625),
            Shapes.box(0.0625, 0, 0.4375, 0.3125, 0.09375, 0.5625),
            Shapes.box(0.1875, 0.09375, 0.4375, 0.4375, 0.1875, 0.5625),
            Shapes.box(0.6875, 0, 0.4375, 0.9375, 0.09375, 0.5625),
            Shapes.box(0.5625, 0.09375, 0.4375, 0.8125, 0.1875, 0.5625),
            Shapes.box(0.4375, 0, 0.0625, 0.5625, 0.09375, 0.3125),
            Shapes.box(0.4375, 0.09375, 0.1875, 0.5625, 0.1875, 0.4375),
            Shapes.box(0.4375, 0, 0.6875, 0.5625, 0.09375, 0.9375),
            Shapes.box(0.4375, 0.09375, 0.5625, 0.5625, 0.1875, 0.8125));
    private static final VoxelShape ONE_SHAPE_NORTH = ShapeUtil.combine(
            Shapes.box(0, 0.875, 0, 1, 1, 0.9375),
            Shapes.box(0.0625, 0.875, 0.9375, 0.9375, 1, 1),
            Shapes.box(0.1875, 0.1875, 0.6875, 0.3125, 0.875, 0.8125),
            Shapes.box(0.21875, 0.125, 0.71875, 0.28125, 0.1875, 0.78125),
            Shapes.box(0.1875, 0, 0.6875, 0.3125, 0.125, 0.8125),
            Shapes.box(0.6875, 0.1875, 0.6875, 0.8125, 0.875, 0.8125),
            Shapes.box(0.71875, 0.125, 0.71875, 0.78125, 0.1875, 0.78125),
            Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.125, 0.8125));
    private static final VoxelShape ONE_SHAPE_EAST = ShapeUtil.rotate(ONE_SHAPE_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape ONE_SHAPE_SOUTH = ShapeUtil.rotate(ONE_SHAPE_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape ONE_SHAPE_WEST = ShapeUtil.rotate(ONE_SHAPE_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape STRAIGHT_SHAPE = Shapes.box(0, 0.875, 0, 1, 1, 1);
    private static final VoxelShape CURVE_SHAPE_NORTH = ShapeUtil.combine(
            Shapes.box(0, 0.875, 0, 1, 1, 0.9375),
            Shapes.box(0, 0.875, 0.9375, 0.9375, 1, 1),
            Shapes.box(0.6875, 0.1875, 0.6875, 0.8125, 0.875, 0.8125),
            Shapes.box(0.71875, 0.125, 0.71875, 0.78125, 0.1875, 0.78125),
            Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.125, 0.8125));
    private static final VoxelShape CURVE_SHAPE_EAST = ShapeUtil.rotate(CURVE_SHAPE_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape CURVE_SHAPE_SOUTH = ShapeUtil.rotate(CURVE_SHAPE_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape CURVE_SHAPE_WEST = ShapeUtil.rotate(CURVE_SHAPE_NORTH, Rotation.COUNTERCLOCKWISE_90);

    public TableBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(TYPE, Type.NONE));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case NONE -> NONE_SHAPE;
            case ONE -> switch (state.getValue(FACING)) {
                case SOUTH -> ONE_SHAPE_SOUTH;
                case WEST -> ONE_SHAPE_WEST;
                case EAST -> ONE_SHAPE_EAST;
                default -> ONE_SHAPE_NORTH;
            };
            case STRAIGHT, THREE, ALL -> STRAIGHT_SHAPE;
            case CURVE -> switch (state.getValue(FACING)) {
                case SOUTH -> CURVE_SHAPE_SOUTH;
                case WEST -> CURVE_SHAPE_WEST;
                case EAST -> CURVE_SHAPE_EAST;
                default -> CURVE_SHAPE_NORTH;
            };
        };
    }

    public enum Type implements StringRepresentable {
        NONE,
        ONE,
        STRAIGHT,
        CURVE,
        THREE,
        ALL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
