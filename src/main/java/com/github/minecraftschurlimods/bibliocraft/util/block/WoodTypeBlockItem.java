package com.github.minecraftschurlimods.bibliocraft.util.block;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.util.holder.WoodTypeDeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class WoodTypeBlockItem extends BlockItem {
    protected final BibliocraftWoodType woodType;

    public WoodTypeBlockItem(WoodTypeDeferredHolder<Block, ? extends Block> holder, BibliocraftWoodType woodType) {
        super(holder.get(woodType), new Properties());
        this.woodType = woodType;
    }
}
