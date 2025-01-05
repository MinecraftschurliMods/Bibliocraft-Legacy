package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class GrandfatherClockBlock extends AbstractClockBlock {
    private static final VoxelShape X_SHAPE_TOP = ShapeUtil.combine(
            Shapes.box(0.3125, 0, 0.25, 0.6875, 0.875, 0.75),
            Shapes.box(0.3125, 0.875, 0.25, 0.375, 0.9375, 0.3125),
            Shapes.box(0.3125, 0.875, 0.4375, 0.375, 0.9375, 0.5625),
            Shapes.box(0.3125, 0.875, 0.6875, 0.375, 0.9375, 0.75),
            Shapes.box(0.625, 0.875, 0.25, 0.6875, 0.9375, 0.3125),
            Shapes.box(0.625, 0.875, 0.4375, 0.6875, 0.9375, 0.5625),
            Shapes.box(0.625, 0.875, 0.6875, 0.6875, 0.9375, 0.75));
    private static final VoxelShape Z_SHAPE_TOP = ShapeUtil.rotate(X_SHAPE_TOP, Rotation.CLOCKWISE_90);
    private static final VoxelShape X_SHAPE_BOTTOM = ShapeUtil.combine(
            Shapes.box(0.3125, 0.0625, 0.25, 0.6875, 1, 0.75),
            Shapes.box(0.3125, 0, 0.25, 0.4375, 0.0625, 0.375),
            Shapes.box(0.3125, 0, 0.625, 0.4375, 0.0625, 0.75),
            Shapes.box(0.5625, 0, 0.25, 0.6875, 0.0625, 0.375),
            Shapes.box(0.5625, 0, 0.625, 0.6875, 0.0625, 0.75));
    private static final VoxelShape Z_SHAPE_BOTTOM = ShapeUtil.rotate(X_SHAPE_BOTTOM, Rotation.CLOCKWISE_90);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public GrandfatherClockBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_TOP : Z_SHAPE_TOP;
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_BOTTOM : Z_SHAPE_BOTTOM;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF, DoubleBlockHalf.LOWER) : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER || blockstate.is(this);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            //Copy of protected method DoublePlantBlock#preventCreativeDropFromBottomPart
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos newPos = pos.below();
                BlockState newState = level.getBlockState(newPos);
                if (newState.is(state.getBlock()) && newState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    level.setBlock(newPos, newState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, newPos, Block.getId(newState));
                }
            } else {
                BlockPos newPos = pos.above();
                BlockState newState = level.getBlockState(newPos);
                if (newState.is(state.getBlock()) && newState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    level.setBlock(newPos, newState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, newPos, Block.getId(newState));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? new ClockBlockEntity(pos, state) : null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? super.getTicker(level, state, blockEntityType) : null;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (level.getBlockState(pos.above()).getBlock() instanceof GrandfatherClockBlock) {
                pos = pos.above();
            } else return InteractionResult.PASS;
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }
}
