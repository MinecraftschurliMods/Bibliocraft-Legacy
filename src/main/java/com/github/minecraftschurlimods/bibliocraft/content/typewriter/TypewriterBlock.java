package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class TypewriterBlock extends BCFacingEntityBlock {
    public static final IntegerProperty PAPER = IntegerProperty.create("paper", 0, 8);

    public TypewriterBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(PAPER, 0).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PAPER);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
