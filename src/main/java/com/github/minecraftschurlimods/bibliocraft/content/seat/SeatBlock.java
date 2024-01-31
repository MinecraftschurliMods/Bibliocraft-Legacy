package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SeatBlock extends BCBlock {
    private static final VoxelShape SHAPE = ShapeUtil.combine(
            Shapes.box(0.125, 0.75, 0.125, 0.875, 0.9375, 0.875),
            Shapes.box(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125),
            Shapes.box(0.25, 0, 0.25, 0.375, 0.75, 0.375),
            Shapes.box(0.625, 0, 0.25, 0.75, 0.75, 0.375),
            Shapes.box(0.625, 0, 0.625, 0.75, 0.75, 0.75),
            Shapes.box(0.25, 0, 0.625, 0.375, 0.75, 0.75),
            Shapes.box(0.28125, 0.28125, 0.375, 0.34375, 0.34375, 0.625),
            Shapes.box(0.65625, 0.28125, 0.375, 0.71875, 0.34375, 0.625),
            Shapes.box(0.375, 0.28125, 0.28125, 0.625, 0.34375, 0.34375),
            Shapes.box(0.375, 0.28125, 0.65625, 0.625, 0.34375, 0.71875));
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public SeatBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(OCCUPIED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SeatBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OCCUPIED);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(OCCUPIED) || !level.getBlockState(pos.above()).isAir() || player.getVehicle() != null)
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
}
