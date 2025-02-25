package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancySignItem extends WoodTypeBlockItem {
    public FancySignItem(BibliocraftWoodType woodType) {
        super(BCBlocks.FANCY_SIGN, woodType);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = (switch (context.getClickedFace()) {
            case UP -> BCBlocks.FANCY_SIGN.get(woodType).defaultBlockState();
            case DOWN -> BCBlocks.FANCY_SIGN.get(woodType).defaultBlockState().setValue(FancySignBlock.HANGING, true);
            default -> BCBlocks.WALL_FANCY_SIGN.get(woodType).defaultBlockState();
        }).setValue(FancySignBlock.FACING, context.getHorizontalDirection().getOpposite());
        return canPlace(context, state) ? state : null;
    }
}
