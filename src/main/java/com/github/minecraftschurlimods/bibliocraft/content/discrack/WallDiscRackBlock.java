package com.github.minecraftschurlimods.bibliocraft.content.discrack;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingInteractibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallDiscRackBlock extends BCFacingInteractibleBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.3125, 0.1875, 0.9375, 0.6875, 0.8125, 1),
            Shapes.box(0.375, 0.203125, 0.75, 0.625, 0.796875, 0.9375));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public WallDiscRackBlock(Properties properties) {
        super(properties);
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
        return (int) (12.5 - hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos())).scale(16).y());
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return state.getValue(FACING) == direction ||
                state.getValue(FACING).getAxis() == Direction.Axis.X && direction.getAxis() == Direction.Axis.Z ||
                state.getValue(FACING).getAxis() == Direction.Axis.Z && direction.getAxis() == Direction.Axis.X;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(BCItems.DISC_RACK.get());
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DiscRackBlockEntity(pos, state);
    }
}
