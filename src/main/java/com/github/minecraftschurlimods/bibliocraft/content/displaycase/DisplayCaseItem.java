package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.block.ColoredWoodTypeBlockItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class DisplayCaseItem extends ColoredWoodTypeBlockItem {
    public DisplayCaseItem(BibliocraftWoodType woodType, DyeColor color, Properties properties) {
        super(BCBlocks.DISPLAY_CASE, woodType, color, properties);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Block block = context.getClickedFace() == Direction.UP ? BCBlocks.DISPLAY_CASE.get(woodType, color) : BCBlocks.WALL_DISPLAY_CASE.get(woodType, color);
        BlockState state = block.defaultBlockState().setValue(AbstractDisplayCaseBlock.FACING, context.getHorizontalDirection().getOpposite());
        return canPlace(context, state) ? state : null;
    }
}
