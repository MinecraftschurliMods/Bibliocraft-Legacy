package at.minecraftschurli.mods.bibliocraft.content.label;

import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.ShapeUtil;
import at.minecraftschurli.mods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.stream.IntStream;

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
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof LabelBlockEntity label) return Math.min(15, IntStream.range(0, 3)
                .map(e -> label.getItemHandler().getResource(e).getItem() instanceof BlockItem blockItem
                        ? blockItem.getBlock() instanceof LabelBlock
                        ? 0
                        : blockItem.getBlock().defaultBlockState().getLightEmission(level, pos)
                        : 0)
                .sum());
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.isSecondaryUseActive()) {
            BlockPos newPos = pos.offset(state.getValue(FACING).getOpposite().getUnitVec3i());
            return level.getBlockState(newPos).useWithoutItem(level, player, hit.withPosition(newPos));
        }
        BCUtil.openBEMenu(player, level, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return WallTorchBlock.canSurvive(level, pos, state.getValue(FACING));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }
}
