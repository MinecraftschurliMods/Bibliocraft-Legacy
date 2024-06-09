package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancyLampItem extends BlockItem {
    public FancyLampItem(Block block) {
        super(block, new Properties());
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState state = getBlock().defaultBlockState().setValue(FancyLampBlock.FACING, context.getHorizontalDirection().getOpposite()).setValue(FancyLampBlock.TYPE, direction == Direction.UP ? FancyLampBlock.Type.STANDING : direction == Direction.DOWN ? FancyLampBlock.Type.HANGING : FancyLampBlock.Type.WALL);
        return canPlace(context, state) ? state : null;
    }
}
