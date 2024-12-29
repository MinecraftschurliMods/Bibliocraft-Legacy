package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FancyClockBlock extends AbstractClockBlock {
    private static final VoxelShape X_SHAPE = ShapeUtil.combine(
            Shapes.box(0.25, 0, 0.25, 0.75, 0.0625, 0.75),
            Shapes.box(0.3125, 0.0625, 0.25, 0.6875, 0.875, 0.75),
            Shapes.box(0.3125, 0.875, 0.25, 0.375, 0.9375, 0.3125),
            Shapes.box(0.3125, 0.875, 0.4375, 0.375, 0.9375, 0.5625),
            Shapes.box(0.3125, 0.875, 0.6875, 0.375, 0.9375, 0.75),
            Shapes.box(0.625, 0.875, 0.25, 0.6875, 0.9375, 0.3125),
            Shapes.box(0.625, 0.875, 0.4375, 0.6875, 0.9375, 0.5625),
            Shapes.box(0.625, 0.875, 0.6875, 0.6875, 0.9375, 0.75));
    private static final VoxelShape Z_SHAPE = ShapeUtil.rotate(X_SHAPE, Rotation.CLOCKWISE_90);

    public FancyClockBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }
}
