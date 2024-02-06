package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCSimpleBlock;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class SeatBlock extends BCWaterloggedBlock {
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    private static final VoxelShape SHAPE = ShapeUtil.combine(
            Shapes.box(0.0625, 0.625, 0.0625, 0.9375, 0.8125, 0.9375),
            Shapes.box(0.125, 0.8125, 0.125, 0.875, 0.875, 0.875),
            Shapes.box(0.1875, 0, 0.1875, 0.3125, 0.625, 0.3125),
            Shapes.box(0.6875, 0, 0.1875, 0.8125, 0.625, 0.3125),
            Shapes.box(0.6875, 0, 0.6875, 0.8125, 0.625, 0.8125),
            Shapes.box(0.1875, 0, 0.6875, 0.3125, 0.625, 0.8125),
            Shapes.box(0.21875, 0.28125, 0.3125, 0.28125, 0.34375, 0.6875),
            Shapes.box(0.71875, 0.28125, 0.3125, 0.78125, 0.34375, 0.6875),
            Shapes.box(0.3125, 0.28125, 0.21875, 0.6875, 0.34375, 0.28125),
            Shapes.box(0.3125, 0.28125, 0.71875, 0.6875, 0.34375, 0.78125));
    private static final VoxelShape SHAPE_SMALL_NORTH = ShapeUtil.combine(SHAPE,
            Shapes.box(0.1875, 0.8125, 0.84375, 0.8125, 0.9375, 0.90625),
            Shapes.box(0.125, 0.921875, 0.8125, 0.875, 1, 0.9375));
    private static final VoxelShape SHAPE_SMALL_EAST = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_SMALL_SOUTH = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_SMALL_WEST = ShapeUtil.rotate(SHAPE_SMALL_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_RAISED_NORTH = ShapeUtil.combine(SHAPE,
            Shapes.box(0.1875, 0.8125, 0.75, 0.3125, 1, 0.875),
            Shapes.box(0.6875, 0.8125, 0.75, 0.8125, 1, 0.875));
    private static final VoxelShape SHAPE_RAISED_EAST = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_RAISED_SOUTH = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_RAISED_WEST = ShapeUtil.rotate(SHAPE_RAISED_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_FLAT_NORTH = ShapeUtil.combine(SHAPE,
            Shapes.box(0.125, 0.8125, 0.8125, 0.875, 1, 0.9375));
    private static final VoxelShape SHAPE_FLAT_EAST = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_FLAT_SOUTH = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_FLAT_WEST = ShapeUtil.rotate(SHAPE_FLAT_NORTH, Rotation.COUNTERCLOCKWISE_90);
    private static final VoxelShape SHAPE_FANCY_NORTH = ShapeUtil.combine(SHAPE_FLAT_NORTH,
            Shapes.box(0.875, 0.6875, 0.125, 1, 1, 0.9375),
            Shapes.box(0.875, 0.875, 0, 1, 1, 0.125),
            Shapes.box(0.875, 0.75, 0.0625, 1, 0.875, 0.125),
            Shapes.box(0, 0.6875, 0.125, 0.125, 1, 0.9375),
            Shapes.box(0, 0.875, 0, 0.125, 1, 0.125),
            Shapes.box(0, 0.75, 0.0625, 0.125, 0.875, 0.125));
    private static final VoxelShape SHAPE_FANCY_EAST = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.CLOCKWISE_90);
    private static final VoxelShape SHAPE_FANCY_SOUTH = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.CLOCKWISE_180);
    private static final VoxelShape SHAPE_FANCY_WEST = ShapeUtil.rotate(SHAPE_FANCY_NORTH, Rotation.COUNTERCLOCKWISE_90);

    public SeatBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(WATERLOGGED, false).setValue(OCCUPIED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(OCCUPIED) || player.getVehicle() != null || !(level.getBlockState(pos.above()).isAir() || level.getBlockState(pos.above()).getBlock() instanceof SeatBackBlock))
            return super.use(state, level, pos, player, hand, hit);
        if (!level.isClientSide()) {
            SeatEntity entity = new SeatEntity(level);
            entity.setPos(Vec3.atBottomCenterOf(pos));
            level.addFreshEntity(entity);
            player.startRiding(entity);
            level.setBlockAndUpdate(pos, state.setValue(OCCUPIED, true));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(OCCUPIED) ? 15 : 0;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockState back = level.getBlockState(pos.above());
        if (!(back.getBlock() instanceof SeatBackBlock)) return SHAPE;
        return switch (back.getValue(SeatBackBlock.TYPE)) {
            case SMALL -> switch (back.getValue(BCSimpleBlock.FACING)) {
                case NORTH -> SHAPE_SMALL_NORTH;
                case EAST -> SHAPE_SMALL_EAST;
                case SOUTH -> SHAPE_SMALL_SOUTH;
                case WEST -> SHAPE_SMALL_WEST;
                default -> SHAPE;
            };
            case RAISED -> switch (back.getValue(BCSimpleBlock.FACING)) {
                case NORTH -> SHAPE_RAISED_NORTH;
                case EAST -> SHAPE_RAISED_EAST;
                case SOUTH -> SHAPE_RAISED_SOUTH;
                case WEST -> SHAPE_RAISED_WEST;
                default -> SHAPE;
            };
            case FLAT, TALL -> switch (back.getValue(BCSimpleBlock.FACING)) {
                case NORTH -> SHAPE_FLAT_NORTH;
                case EAST -> SHAPE_FLAT_EAST;
                case SOUTH -> SHAPE_FLAT_SOUTH;
                case WEST -> SHAPE_FLAT_WEST;
                default -> SHAPE;
            };
            case FANCY -> switch (back.getValue(BCSimpleBlock.FACING)) {
                case NORTH -> SHAPE_FANCY_NORTH;
                case EAST -> SHAPE_FANCY_EAST;
                case SOUTH -> SHAPE_FANCY_SOUTH;
                case WEST -> SHAPE_FANCY_WEST;
                default -> SHAPE;
            };
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos above = pos.above();
        BlockState back = level.getBlockState(above);
        if (back.getBlock() instanceof SeatBackBlock) {
            Block.dropResources(back, level, above);
            level.setBlockAndUpdate(above, Blocks.AIR.defaultBlockState());
            level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, above, Block.getId(back));
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OCCUPIED);
    }
}
