package com.github.minecraftschurlimods.bibliocraft.content.deskbell;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCWaterloggedBlock;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
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
import org.jetbrains.annotations.Nullable;

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
            Shapes.box(0.46875, 0.171875, 0.46875, 0.53125, 0.203125, 0.53125)
    );
    public static final MapCodec<DeskBellBlock> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SoundEvent.CODEC.fieldOf("ding_sound").forGetter(b -> b.ding),
            propertiesCodec()
    ).apply(inst, DeskBellBlock::new));
    private final Holder<SoundEvent> ding;

    public DeskBellBlock(Holder<SoundEvent> ding, Properties properties) {
        super(properties);
        this.ding = ding;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide() && level.hasNeighborSignal(pos.below())) {
            ding(level, pos, null);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        ding(level, pos, player);
        return InteractionResult.SUCCESS;
    }

    private void ding(Level level, BlockPos pos, @Nullable Player player) {
        level.playSound(player, pos, ding.value(), SoundSource.BLOCKS);
    }
}
