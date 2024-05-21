package com.github.minecraftschurlimods.bibliocraft.content.potionshelf;

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

@SuppressWarnings("deprecation")
public class PotionShelfBlock extends BCFacingInteractibleBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0, 0.0625, 0.75, 1, 1, 1),
            Shapes.box(0, 0, 0.75, 0.0625, 0.0625, 1),
            Shapes.box(0.9375, 0, 0.75, 1, 0.0625, 1));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    public static final MapCodec<PotionShelfBlock> CODEC = simpleCodec(PotionShelfBlock::new);

    public PotionShelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<PotionShelfBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionShelfBlockEntity(pos, state);
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
        double hitX = hit.getLocation().get(axis) - hit.getBlockPos().get(axis) + 0.125;
        if (direction.getStepX() > 0 || direction.getStepZ() > 0) {
            hitX = 1.25 - hitX;
        }
        double hitY = hit.getLocation().y - hit.getBlockPos().getY();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                double minX = 0.171875 + j * 0.21875;
                double maxX = minX + 0.21875;
                double maxY = 0.90625 - i * 0.3125;
                double minY = maxY - 0.21875;
                if (hitX >= minX && hitX < maxX && hitY >= minY && hitY < maxY) return i * 4 + j;
            }
        }
        return -1;
    }
}
