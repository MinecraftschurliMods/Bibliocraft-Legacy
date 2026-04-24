package at.minecraftschurli.mods.bibliocraft.content.fancysign;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.util.block.WoodTypeBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class FancySignItem extends WoodTypeBlockItem {
    public FancySignItem(BibliocraftWoodType woodType, Properties properties) {
        super(BCBlocks.FANCY_SIGN, woodType, properties);
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
