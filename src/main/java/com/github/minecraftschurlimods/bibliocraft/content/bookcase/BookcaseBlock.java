package com.github.minecraftschurlimods.bibliocraft.content.bookcase;

import com.github.minecraftschurlimods.bibliocraft.content.redstonebook.RedstoneBookItem;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class BookcaseBlock extends BCFacingEntityBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.0625, 0, 0.5, 0.9375, 0.0625, 0.9375),
            Shapes.box(0, 0, 0.9375, 1, 1, 1),
            Shapes.box(0.0625, 0.9375, 0.5, 0.9375, 1, 0.9375),
            Shapes.box(0.0625, 0.4375, 0.5, 0.9375, 0.5625, 0.9375),
            Shapes.box(0.9375, 0, 0.5, 1, 1, 0.9375),
            Shapes.box(0, 0, 0.5, 0.0625, 1, 0.9375));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public BookcaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookcaseBlockEntity(pos, state);
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
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide()) return super.getAnalogOutputSignal(state, level, pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BookcaseBlockEntity bcbe)) return super.getAnalogOutputSignal(state, level, pos);
        for (int i = 15; i >= 0; i--) {
            if (bcbe.getItem(i).getItem() instanceof RedstoneBookItem) return i;
        }
        return super.getAnalogOutputSignal(state, level, pos);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BookcaseBlockEntity bcbe)) return super.getEnchantPowerBonus(state, level, pos);
        return super.getEnchantPowerBonus(state, level, pos) + 0.125f * IntStream.range(0, bcbe.getContainerSize())
                .mapToObj(bcbe::getItem)
                .filter(e -> !e.isEmpty())
                .count();
    }
}
