package at.minecraftschurli.mods.bibliocraft.content.deskbell;

import at.minecraftschurli.mods.bibliocraft.init.BCSoundEvents;
import at.minecraftschurli.mods.bibliocraft.util.ShapeUtil;
import at.minecraftschurli.mods.bibliocraft.util.block.BCWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DeskBellBlock extends BCWaterloggedBlock {
    private static final VoxelShape SHAPE = ShapeUtil.combine(
            Shapes.box(0.375, 0, 0.375, 0.625, 0.125, 0.625),
            Shapes.box(0.40625, 0.125, 0.40625, 0.59375, 0.15625, 0.59375),
            Shapes.box(0.40625, 0, 0.34375, 0.59375, 0.09375, 0.375),
            Shapes.box(0.40625, 0, 0.625, 0.59375, 0.09375, 0.65625),
            Shapes.box(0.34375, 0, 0.40625, 0.375, 0.09375, 0.59375),
            Shapes.box(0.625, 0, 0.40625, 0.65625, 0.09375, 0.59375),
            Shapes.box(0.484375, 0.15625, 0.484375, 0.515625, 0.171875, 0.515625),
            Shapes.box(0.46875, 0.171875, 0.46875, 0.53125, 0.203125, 0.53125));

    public DeskBellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!level.isClientSide() && level.hasNeighborSignal(pos.below())) {
            playSound(level, pos);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        playSound(level, pos);
        return InteractionResult.SUCCESS;
    }

    private void playSound(Level level, BlockPos pos) {
        level.playSound(null, pos, BCSoundEvents.DESK_BELL.value(), SoundSource.BLOCKS, 1, 1);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos relativePos = pos.below();
        BlockState relativeState = level.getBlockState(relativePos);
        return relativeState.isFaceSturdy(level, relativePos, Direction.UP, SupportType.CENTER);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }
}
