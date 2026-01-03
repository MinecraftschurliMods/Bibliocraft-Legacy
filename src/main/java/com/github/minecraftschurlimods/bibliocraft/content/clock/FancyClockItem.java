package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.block.WoodTypeBlockItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class FancyClockItem extends WoodTypeBlockItem {
    public FancyClockItem(BibliocraftWoodType woodType, Properties properties) {
        super(BCBlocks.FANCY_CLOCK, woodType, properties);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Block block = context.getClickedFace() == Direction.UP ? BCBlocks.FANCY_CLOCK.get(woodType) : BCBlocks.WALL_FANCY_CLOCK.get(woodType);
        BlockState state = block.defaultBlockState().setValue(AbstractClockBlock.FACING, context.getHorizontalDirection().getOpposite());
        return canPlace(context, state) ? state : null;
    }
}
