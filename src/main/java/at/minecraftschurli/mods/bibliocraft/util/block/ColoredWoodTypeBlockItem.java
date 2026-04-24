package at.minecraftschurli.mods.bibliocraft.util.block;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class ColoredWoodTypeBlockItem extends BlockItem {
    protected final BibliocraftWoodType woodType;
    protected final DyeColor color;

    public ColoredWoodTypeBlockItem(GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, ? extends Block> holder, BibliocraftWoodType woodType, DyeColor color, Properties properties) {
        super(holder.get(woodType, color), properties);
        this.woodType = woodType;
        this.color = color;
    }

    public BibliocraftWoodType getWoodType() {
        return woodType;
    }

    public DyeColor getColor() {
        return color;
    }
}
