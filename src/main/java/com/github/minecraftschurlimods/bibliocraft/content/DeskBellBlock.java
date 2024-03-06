package com.github.minecraftschurlimods.bibliocraft.content;

import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
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
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide() && level.hasNeighborSignal(pos.below())) {
            playSound(level, pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        playSound(level, pos);
        return InteractionResult.SUCCESS;
    }

    private void playSound(Level level, BlockPos pos) {
        level.playSound(null, pos, BCSoundEvents.DESK_BELL.get(), SoundSource.BLOCKS, 1, 1);
    }
}
