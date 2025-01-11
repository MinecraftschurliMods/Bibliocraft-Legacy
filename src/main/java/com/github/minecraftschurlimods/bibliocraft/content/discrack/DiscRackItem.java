package com.github.minecraftschurlimods.bibliocraft.content.discrack;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DiscRackItem extends BlockItem {
    public DiscRackItem(Properties properties) {
        super(BCBlocks.DISC_RACK.get(), properties);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Block block = context.getClickedFace() == Direction.UP ? BCBlocks.DISC_RACK.get() : BCBlocks.WALL_DISC_RACK.get();
        BlockState state = block.defaultBlockState().setValue(DiscRackBlock.FACING, context.getHorizontalDirection().getOpposite());
        return canPlace(context, state) ? state : null;
    }
}
