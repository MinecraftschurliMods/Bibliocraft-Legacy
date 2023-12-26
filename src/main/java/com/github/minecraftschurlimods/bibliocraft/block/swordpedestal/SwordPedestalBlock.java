package com.github.minecraftschurlimods.bibliocraft.block.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.block.BCInteractibleBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SwordPedestalBlock extends BCInteractibleBlock {
    private static final VoxelShape Z_SHAPE = ShapeUtil.combine(
            Shapes.box(0.9375, 0, 0.25, 1, 0.0625, 0.75),
            Shapes.box(0.875, 0, 0.25, 0.9375, 0.0625, 0.75),
            Shapes.box(0.8125, 0, 0.25, 0.875, 0.125, 0.75),
            Shapes.box(0.875, 0.0625, 0.25, 0.9375, 0.125, 0.75),
            Shapes.box(0.8125, 0.125, 0.25, 0.875, 0.1875, 0.75),
            Shapes.box(0.125, 0, 0.25, 0.1875, 0.125, 0.75),
            Shapes.box(0.0625, 0, 0.25, 0.125, 0.0625, 0.75),
            Shapes.box(0, 0, 0.25, 0.0625, 0.0625, 0.75),
            Shapes.box(0.0625, 0.0625, 0.25, 0.125, 0.125, 0.75),
            Shapes.box(0.125, 0.125, 0.25, 0.1875, 0.1875, 0.75),
            Shapes.box(0.75, 0, 0.25, 0.8125, 0.1875, 0.75),
            Shapes.box(0.75, 0.1875, 0.25, 0.8125, 0.25, 0.75),
            Shapes.box(0.1875, 0.1875, 0.25, 0.25, 0.25, 0.75),
            Shapes.box(0.1875, 0, 0.25, 0.25, 0.1875, 0.75),
            Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75),
            Shapes.box(0.25, 0, 0.25, 0.75, 0.25, 0.75));
    private static final VoxelShape X_SHAPE = ShapeUtil.rotate(Z_SHAPE, Rotation.CLOCKWISE_90);

    public SwordPedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        return 0;
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction != Direction.DOWN;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Override
    protected MapCodec<SwordPedestalBlock> codec() {
        return simpleCodec(SwordPedestalBlock::new);
    }
}
