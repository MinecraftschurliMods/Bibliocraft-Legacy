package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancyLightItem extends BlockItem {
    public FancyLightItem(AbstractFancyLightBlock block) {
        super(block, new Properties());
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState state = getBlock().defaultBlockState().setValue(AbstractFancyLightBlock.FACING, context.getHorizontalDirection().getOpposite()).setValue(AbstractFancyLightBlock.TYPE, direction == Direction.UP ? AbstractFancyLightBlock.Type.STANDING : direction == Direction.DOWN ? AbstractFancyLightBlock.Type.HANGING : AbstractFancyLightBlock.Type.WALL);
        return canPlace(context, state) ? state : null;
    }
}
