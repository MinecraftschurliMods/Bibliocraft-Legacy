package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class WallDisplayCaseBlock extends AbstractDisplayCaseBlock {
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.0625, 0, 0.5, 0.9375, 1, 1);
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private final WoodType woodType;
    private final DyeColor color;

    public WallDisplayCaseBlock(Properties properties) {
        this(properties, null, null);
    }

    public WallDisplayCaseBlock(Properties properties, @Nullable WoodType woodType, @Nullable DyeColor color) {
        super(properties);
        this.woodType = woodType;
        this.color = color;
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return state.getValue(FACING) == direction;
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return woodType != null && color != null ? new ItemStack(BCItems.DISPLAY_CASE.get(woodType, color)) : super.getCloneItemStack(state, target, level, pos, player);
    }
}
