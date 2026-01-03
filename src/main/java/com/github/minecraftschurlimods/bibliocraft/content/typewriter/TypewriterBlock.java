package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

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
    public static final IntegerProperty PAPER = IntegerProperty.create("paper", 0, 7);

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
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof TypewriterBlockEntity typewriter) {
            ItemStack item = typewriter.getItem(TypewriterBlockEntity.OUTPUT);
            if (!item.isEmpty()) {
                player.addItem(typewriter.takeOutput());
                return InteractionResult.SUCCESS;
            } else if (typewriter.getItem(TypewriterBlockEntity.INPUT).isEmpty()) {
                player.displayClientMessage(Translations.TYPEWRITER_NO_PAPER, true);
                return InteractionResult.SUCCESS;
            } else if (level.isClientSide()) {
                ClientUtil.openTypewriterScreen(pos);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof TypewriterBlockEntity typewriter && stack.is(BCTags.Items.TYPEWRITER_PAPER))
            return typewriter.insertPaper(stack) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TypewriterBlockEntity(pos, state);
    }
}
