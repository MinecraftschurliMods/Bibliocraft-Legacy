package com.github.minecraftschurlimods.bibliocraft.util.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ColoredWoodTypeBlockItem extends BlockItem {
    protected final WoodType woodType;
    protected final DyeColor color;

    public ColoredWoodTypeBlockItem(ColoredWoodTypeDeferredHolder<Block, ? extends Block> holder, WoodType woodType, DyeColor color) {
        super(holder.get(woodType, color), new Properties());
        this.woodType = woodType;
        this.color = color;
    }
}
