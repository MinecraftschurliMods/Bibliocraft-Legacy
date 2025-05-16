package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class AbstractFancyLightBlock extends BCFacingBlock {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public AbstractFancyLightBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(TYPE, Type.STANDING).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }

    public enum Type implements StringRepresentableEnum {
        STANDING, HANGING, WALL
    }
}
