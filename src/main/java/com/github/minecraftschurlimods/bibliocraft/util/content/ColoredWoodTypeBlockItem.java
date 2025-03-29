package com.github.minecraftschurlimods.bibliocraft.util.content;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class ColoredWoodTypeBlockItem extends BlockItem {
    protected final BibliocraftWoodType woodType;
    protected final DyeColor color;

    public ColoredWoodTypeBlockItem(ColoredWoodTypeDeferredHolder<Block, ? extends Block> holder, BibliocraftWoodType woodType, DyeColor color) {
        super(holder.get(woodType, color), new Properties());
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
