package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallFancyClockBlock extends AbstractClockBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0.25, 0.0625, 0.625, 0.75, 0.875, 1),
            Shapes.box(0.25, 0.875, 0.625, 0.3125, 0.9375, 0.6875),
            Shapes.box(0.4375, 0.875, 0.625, 0.5625, 0.9375, 0.6875),
            Shapes.box(0.6875, 0.875, 0.625, 0.75, 0.9375, 0.6875),
            Shapes.box(0.25, 0.875, 0.9375, 0.3125, 0.9375, 1),
            Shapes.box(0.4375, 0.875, 0.9375, 0.5625, 0.9375, 1),
            Shapes.box(0.6875, 0.875, 0.9375, 0.75, 0.9375, 1));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private final BibliocraftWoodType woodType;

    public WallFancyClockBlock(Properties properties) {
        this(properties, null);
    }

    public WallFancyClockBlock(Properties properties, @Nullable BibliocraftWoodType woodType) {
        super(properties);
        this.woodType = woodType;
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
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return woodType != null ? new ItemStack(BCItems.FANCY_CLOCK.get(woodType)) : super.getCloneItemStack(level, pos, state, includeData, player);
    }
}
