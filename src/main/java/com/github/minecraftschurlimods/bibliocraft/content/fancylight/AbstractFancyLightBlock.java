package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class AbstractFancyLightBlock extends BCFacingBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public AbstractFancyLightBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(LIT, true).setValue(TYPE, Type.STANDING).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        return BCUtil.nonNull(super.getStateForPlacement(context)).setValue(TYPE, direction == Direction.UP ? Type.STANDING : direction == Direction.DOWN ? Type.HANGING : Type.WALL);
    }

    public enum Type implements StringRepresentableEnum {
        STANDING, HANGING, WALL
    }
}
