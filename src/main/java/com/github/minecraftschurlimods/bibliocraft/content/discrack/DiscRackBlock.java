package com.github.minecraftschurlimods.bibliocraft.content.discrack;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DiscRackBlock extends AbstractDiscRackBlock {
    private static final VoxelShape Z_SHAPE = ShapeUtil.combine(
            Shapes.box(0.3125, 0, 0.1875, 0.6875, 0.0625, 0.8125),
            Shapes.box(0.375, 0.0625, 0.203125, 0.625, 0.25, 0.796875));
    private static final VoxelShape X_SHAPE = ShapeUtil.rotate(Z_SHAPE, Rotation.CLOCKWISE_90);

    public DiscRackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        Vec3 pos = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos())).scale(16);
        double value = switch (state.getValue(FACING)) {
            case SOUTH -> pos.z() - 3.5;
            case EAST -> pos.x() - 3.5;
            case NORTH -> 12.5 - pos.z();
            case WEST -> 12.5 - pos.x();
            default -> -1;
        };
        return value == -1 ? -1 : (int) value;
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction == Direction.UP ||
               state.getValue(FACING).getAxis() == Direction.Axis.X && direction.getAxis() == Direction.Axis.Z ||
               state.getValue(FACING).getAxis() == Direction.Axis.Z && direction.getAxis() == Direction.Axis.X;
    }
}
