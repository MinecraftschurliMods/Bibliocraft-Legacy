package com.github.minecraftschurlimods.bibliocraft.content.shelf;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingInteractibleBlock;
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

public class ShelfBlock extends BCFacingInteractibleBlock {
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0, 0, 0.5, 1, 1, 1);
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    public static final MapCodec<ShelfBlock> CODEC = simpleCodec(ShelfBlock::new);

    public ShelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<ShelfBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShelfBlockEntity(pos, state);
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
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        Direction direction = state.getValue(FACING).getClockWise();
        Direction.Axis axis = direction.getAxis();
        double hitX = hit.getLocation().get(axis) - hit.getBlockPos().get(axis);
        if (direction.getStepX() > 0 || direction.getStepZ() > 0) {
            hitX = 1 - hitX;
        }
        double hitY = hit.getLocation().y - hit.getBlockPos().getY();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double minX = j * 0.5625;
                double maxX = minX + 0.4375;
                double maxY = 1 - i * 0.5;
                double minY = maxY - 0.4375;
                if (hitX >= minX && hitX < maxX && hitY >= minY && hitY < maxY) return i * 2 + j;
            }
        }
        return -1;
    }
}
