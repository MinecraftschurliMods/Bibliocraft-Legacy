package com.github.minecraftschurlimods.bibliocraft.block.toolrack;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ToolRackBlock extends BCBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.0625, 0.9375, 0.5, 0.9375, 1, 1),
            Shapes.box(0.9375, 0, 0.5, 1, 1, 1),
            Shapes.box(0, 0, 0.5, 0.0625, 1, 1),
            Shapes.box(0.0625, 0, 0.5, 0.9375, 0.0625, 1),
            Shapes.box(0.0625, 0.0625, 0.5625, 0.9375, 0.9375, 1),
            Shapes.box(0.75, 0.0625, 0.5, 0.8125, 0.125, 0.5625),
            Shapes.box(0.625, 0.1875, 0.5, 0.6875, 0.25, 0.5625),
            Shapes.box(0.3125, 0.0625, 0.5, 0.375, 0.125, 0.5625),
            Shapes.box(0.1875, 0.1875, 0.5, 0.25, 0.25, 0.5625),
            Shapes.box(0.75, 0.5, 0.5, 0.8125, 0.5625, 0.5625),
            Shapes.box(0.625, 0.625, 0.5, 0.6875, 0.6875, 0.5625),
            Shapes.box(0.3125, 0.5, 0.5, 0.375, 0.5625, 0.5625),
            Shapes.box(0.1875, 0.625, 0.5, 0.25, 0.6875, 0.5625));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public ToolRackBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToolRackBlockEntity(pos, state);
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
}
