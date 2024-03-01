package com.github.minecraftschurlimods.bibliocraft.content.table;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

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

    @Nullable
    public static DyeColor getCarpetColor(ItemStack stack) {
        return stack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof WoolCarpetBlock carpet ? carpet.getColor() : null;
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return getNewState(level, pos, Objects.requireNonNull(super.getStateForPlacement(context)));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return getNewState(level, pos, defaultBlockState());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof TableBlockEntity table)) return super.use(state, level, pos, player, hand, hit);
        Direction direction = hit.getDirection();
        if (direction == Direction.DOWN) return super.use(state, level, pos, player, hand, hit);
        ItemStack stack = player.getItemInHand(hand);
        boolean useCarpet = direction != Direction.UP && (getCarpetColor(stack) != null || (stack.isEmpty() && !table.getItem(1).isEmpty()));
        ItemStack originalStack = table.getItem(useCarpet ? 1 : 0);
        if (ItemStack.isSameItem(stack, originalStack)) return InteractionResult.FAIL;
        table.setItem(useCarpet ? 1 : 0, stack.copyWithCount(1));
        stack.shrink(1);
        if (!originalStack.isEmpty()) {
            if (stack.isEmpty()) {
                player.setItemInHand(hand, originalStack);
            } else {
                player.getInventory().add(originalStack);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private BlockState getNewState(LevelAccessor level, BlockPos pos, BlockState state) {
        boolean north = level.getBlockState(pos.north()).getBlock() instanceof TableBlock;
        boolean east = level.getBlockState(pos.east()).getBlock() instanceof TableBlock;
        boolean south = level.getBlockState(pos.south()).getBlock() instanceof TableBlock;
        boolean west = level.getBlockState(pos.west()).getBlock() instanceof TableBlock;
        if (north && east && south && west) return state.setValue(TYPE, Type.ALL);
        if (north && south && west) return state.setValue(TYPE, Type.THREE).setValue(FACING, Direction.WEST);
        if (east && west && north) return state.setValue(TYPE, Type.THREE).setValue(FACING, Direction.NORTH);
        if (south && north && east) return state.setValue(TYPE, Type.THREE).setValue(FACING, Direction.EAST);
        if (west && east && south) return state.setValue(TYPE, Type.THREE).setValue(FACING, Direction.SOUTH);
        if (north && south) return state.setValue(TYPE, Type.STRAIGHT).setValue(FACING, Direction.NORTH);
        if (east && west) return state.setValue(TYPE, Type.STRAIGHT).setValue(FACING, Direction.EAST);
        if (north && east) return state.setValue(TYPE, Type.CURVE).setValue(FACING, Direction.EAST);
        if (east && south) return state.setValue(TYPE, Type.CURVE).setValue(FACING, Direction.SOUTH);
        if (south && west) return state.setValue(TYPE, Type.CURVE).setValue(FACING, Direction.WEST);
        if (west && north) return state.setValue(TYPE, Type.CURVE).setValue(FACING, Direction.NORTH);
        if (north) return state.setValue(TYPE, Type.ONE).setValue(FACING, Direction.NORTH);
        if (east) return state.setValue(TYPE, Type.ONE).setValue(FACING, Direction.EAST);
        if (south) return state.setValue(TYPE, Type.ONE).setValue(FACING, Direction.SOUTH);
        if (west) return state.setValue(TYPE, Type.ONE).setValue(FACING, Direction.WEST);
        return state;
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TableBlockEntity) {
            blockEntity.requestModelDataUpdate();
        }
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
