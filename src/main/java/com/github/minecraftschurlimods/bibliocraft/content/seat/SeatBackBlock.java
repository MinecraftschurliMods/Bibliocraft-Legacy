package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SeatBackBlock extends BCFacingBlock {
    public static final EnumProperty<SeatBackType> TYPE = EnumProperty.create("type", SeatBackType.class);
    private static final VoxelShape SHAPE_SMALL_NORTH = Shapes.box(0.125, 0, 0.8125, 0.875, 0.296875, 0.9375);
    private static final VoxelShape SHAPE_SMALL_EAST = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_SMALL_SOUTH = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_SMALL_WEST = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_RAISED_NORTH = ShapeUtil.combine(
            Shapes.box(0.1875, 0, 0.75, 0.3125, 0.5, 0.96875),
            Shapes.box(0.6875, 0, 0.75, 0.8125, 0.5, 0.96875),
            Shapes.box(0.125, 0.25, 0.71875, 0.875, 0.625, 0.84375));
    private static final VoxelShape SHAPE_RAISED_EAST = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_RAISED_SOUTH = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_RAISED_WEST = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_FLAT_NORTH = Shapes.box(0.125, 0, 0.8125, 0.875, 0.625, 0.9375);
    private static final VoxelShape SHAPE_FLAT_EAST = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_FLAT_SOUTH = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_FLAT_WEST = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_TALL_NORTH = ShapeUtil.combine(
            Shapes.box(0.125, 0, 0.8125, 0.875, 0.6875, 0.9375),
            Shapes.box(0.625, 0.6875, 0.8125, 0.6875, 0.8125, 0.9375),
            Shapes.box(0.3125, 0.6875, 0.8125, 0.375, 0.8125, 0.9375),
            Shapes.box(0.3125, 0.8125, 0.8125, 0.6875, 0.875, 0.9375));
    private static final VoxelShape SHAPE_TALL_EAST = ShapeUtil.rotate(SHAPE_TALL_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_TALL_SOUTH = ShapeUtil.rotate(SHAPE_TALL_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_TALL_WEST = ShapeUtil.rotate(SHAPE_TALL_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_FANCY_NORTH = ShapeUtil.combine(
            Shapes.box(0.125, 0, 0.8125, 0.875, 0.8125, 0.9375),
            Shapes.box(0.25, 0.8125, 0.8125, 0.75, 0.84375, 0.9375),
            Shapes.box(0.375, 0.84375, 0.8125, 0.625, 0.875, 0.9375),
            Shapes.box(0.875, 0, 0.0625, 1, 0.03125, 0.4375),
            Shapes.box(0.875, 0.03125, 0.1875, 1, 0.0625, 0.3125),
            Shapes.box(0.875, 0, 0.6875, 1, 0.0625, 0.75),
            Shapes.box(0.875, 0, 0.75, 1, 0.8125, 0.9375),
            Shapes.box(0.875, 0.375, 0.71875, 1, 0.75, 0.75),
            Shapes.box(0.875, 0.5, 0.6875, 1, 0.625, 0.71875),
            Shapes.box(0, 0, 0.0625, 0.125, 0.03125, 0.4375),
            Shapes.box(0, 0.03125, 0.1875, 0.125, 0.0625, 0.3125),
            Shapes.box(0, 0, 0.6875, 0.125, 0.0625, 0.75),
            Shapes.box(0, 0, 0.75, 0.125, 0.8125, 0.9375),
            Shapes.box(0, 0.375, 0.71875, 0.125, 0.75, 0.75),
            Shapes.box(0, 0.5, 0.6875, 0.125, 0.625, 0.71875));
    private static final VoxelShape SHAPE_FANCY_EAST = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_FANCY_SOUTH = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_FANCY_WEST = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.COUNTERCLOCKWISE_90);
    public static final MapCodec<SeatBackBlock> CODEC = simpleCodec(SeatBackBlock::new);

    public SeatBackBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(TYPE, SeatBackType.SMALL));
    }

    @Override
    protected MapCodec<SeatBackBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() instanceof SeatBlock;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(SeatBackBlock.TYPE)) {
            case SMALL -> switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_SMALL_NORTH;
                case EAST -> SHAPE_SMALL_EAST;
                case SOUTH -> SHAPE_SMALL_SOUTH;
                case WEST -> SHAPE_SMALL_WEST;
                default -> Shapes.empty();
            };
            case RAISED -> switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_RAISED_NORTH;
                case EAST -> SHAPE_RAISED_EAST;
                case SOUTH -> SHAPE_RAISED_SOUTH;
                case WEST -> SHAPE_RAISED_WEST;
                default -> Shapes.empty();
            };
            case FLAT -> switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_FLAT_NORTH;
                case EAST -> SHAPE_FLAT_EAST;
                case SOUTH -> SHAPE_FLAT_SOUTH;
                case WEST -> SHAPE_FLAT_WEST;
                default -> Shapes.empty();
            };
            case TALL -> switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_TALL_NORTH;
                case EAST -> SHAPE_TALL_EAST;
                case SOUTH -> SHAPE_TALL_SOUTH;
                case WEST -> SHAPE_TALL_WEST;
                default -> Shapes.empty();
            };
            case FANCY -> switch (state.getValue(FACING)) {
                case NORTH -> SHAPE_FANCY_NORTH;
                case EAST -> SHAPE_FANCY_EAST;
                case SOUTH -> SHAPE_FANCY_SOUTH;
                case WEST -> SHAPE_FANCY_WEST;
                default -> Shapes.empty();
            };
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(SeatBackItem.BLOCK_MAP.get(this).get(state.getValue(TYPE)).get());
    }
}
