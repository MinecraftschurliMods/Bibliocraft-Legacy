package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseItem extends BlockItem {
    private final WoodType woodType;
    private final DyeColor color;

    public DisplayCaseItem(WoodType woodType, DyeColor color) {
        super(BCBlocks.DISPLAY_CASE.get(woodType, color), new Properties());
        this.woodType = woodType;
        this.color = color;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Block block = context.getClickedFace() == Direction.UP ? BCBlocks.DISPLAY_CASE.get(woodType, color) : BCBlocks.WALL_DISPLAY_CASE.get(woodType, color);
        BlockState state = block.defaultBlockState().setValue(AbstractDisplayCaseBlock.FACING, context.getHorizontalDirection().getOpposite());
        return canPlace(context, state) ? state : null;
    }
}
