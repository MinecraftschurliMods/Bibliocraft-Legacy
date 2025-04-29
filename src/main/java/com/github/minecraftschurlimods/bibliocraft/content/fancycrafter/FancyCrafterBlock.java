package com.github.minecraftschurlimods.bibliocraft.content.fancycrafter;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FancyCrafterBlock extends BCFacingEntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0, 0, 0, 1, 0.25, 1),
            Shapes.box(0, 0.25, 0.0625, 1, 0.625, 1),
            Shapes.box(0, 0.25, 0, 0.125, 0.625, 0.0625),
            Shapes.box(0.875, 0.25, 0, 1, 0.625, 0.0625),
            Shapes.box(0, 0.625, 0, 1, 0.875, 1),
            Shapes.box(0.0625, 0.875, 0.0625, 0.9375, 0.9375, 0.9375),
            Shapes.box(0, 0.9375, 0, 1, 1, 1));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public FancyCrafterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FancyCrafterBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean flag) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof FancyCrafterBlockEntity crafter) {
                for (int i = 0; i < 9; i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), crafter.getItem(i));
                }
                for (int i = 10; i < crafter.getContainerSize(); i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), crafter.getItem(i));
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            if (state.hasBlockEntity()) {
                level.removeBlockEntity(pos);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != BCBlockEntities.FANCY_CRAFTER.get() || !state.getValue(POWERED))
            return null;
        return (l, p, s, b) -> FancyCrafterBlockEntity.tick(l, p, s, (FancyCrafterBlockEntity) b);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        level.setBlock(pos, state.setValue(POWERED, level.hasNeighborSignal(pos)), UPDATE_CLIENTS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return BCUtil.nonNull(super.getStateForPlacement(context)).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (state.getValue(POWERED)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof FancyCrafterBlockEntity blockEntity ? blockEntity.getRedstoneSignal() : 0;
    }
}
