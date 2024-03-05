package com.github.minecraftschurlimods.bibliocraft.content.label;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class LabelBlock extends BCFacingEntityBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.1875, 0.0625, 0.96875, 0.8125, 0.4375, 1),
            Shapes.box(0.78125, 0.0625, 0.9375, 0.8125, 0.4375, 0.96875),
            Shapes.box(0.1875, 0.0625, 0.9375, 0.21875, 0.4375, 0.96875),
            Shapes.box(0.21875, 0.0625, 0.9375, 0.78125, 0.09375, 0.96875),
            Shapes.box(0.21875, 0.40625, 0.9375, 0.78125, 0.4375, 0.96875));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public LabelBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LabelBlockEntity(pos, state);
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
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof LabelBlockEntity label) return Math.min(15, IntStream.range(0, 3)
                .map(e -> label.getItem(e).getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState().getLightEmission(level, pos) : 0)
                .sum());
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isSecondaryUseActive() || !player.getItemInHand(hand).isEmpty()) {
            BlockPos newPos = pos.offset(state.getValue(FACING).getOpposite().getNormal());
            return level.getBlockState(newPos).use(level, player, hand, hit.withPosition(newPos));
        }
        return BCUtil.openBEMenu(player, level, pos);
    }
}
