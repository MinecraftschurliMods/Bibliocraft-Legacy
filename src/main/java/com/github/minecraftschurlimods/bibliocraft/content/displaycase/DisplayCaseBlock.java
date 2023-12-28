package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class DisplayCaseBlock extends AbstractDisplayCaseBlock {
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.0625, 0, 0, 0.9375, 0.5, 1);
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public DisplayCaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction == Direction.UP;
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
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DisplayCaseBlock::new);
    }
}