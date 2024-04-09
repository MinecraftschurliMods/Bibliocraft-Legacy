package com.github.minecraftschurlimods.bibliocraft.content.dinnerplate;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class DinnerPlateBlock extends Block {
    public static final VoxelShape SHAPE = ShapeUtil.combine(
            Shapes.box(0.25, 0, 0.3125, 0.75, 0.0625, 0.6875),
            Shapes.box(0.1875, 0, 0.375, 0.25, 0.0625, 0.625),
            Shapes.box(0.75, 0, 0.375, 0.8125, 0.0625, 0.625),
            Shapes.box(0.71875, 0.03125, 0.34375, 0.78125, 0.09375, 0.65625),
            Shapes.box(0.34375, 0.03125, 0.21875, 0.65625, 0.09375, 0.28125),
            Shapes.box(0.21875, 0.03125, 0.34375, 0.28125, 0.09375, 0.65625),
            Shapes.box(0.34375, 0.03125, 0.71875, 0.65625, 0.09375, 0.78125),
            Shapes.box(0.28125, 0.03125, 0.28125, 0.40625, 0.09375, 0.34375),
            Shapes.box(0.28125, 0.03125, 0.34375, 0.34375, 0.09375, 0.40625),
            Shapes.box(0.59375, 0.03125, 0.28125, 0.71875, 0.09375, 0.34375),
            Shapes.box(0.65625, 0.03125, 0.34375, 0.71875, 0.09375, 0.40625),
            Shapes.box(0.59375, 0.03125, 0.65625, 0.71875, 0.09375, 0.71875),
            Shapes.box(0.65625, 0.03125, 0.59375, 0.71875, 0.09375, 0.65625),
            Shapes.box(0.28125, 0.03125, 0.65625, 0.40625, 0.09375, 0.71875),
            Shapes.box(0.28125, 0.03125, 0.59375, 0.34375, 0.09375, 0.65625));

    public DinnerPlateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
