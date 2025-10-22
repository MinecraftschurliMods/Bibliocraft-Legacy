package com.github.minecraftschurlimods.bibliocraft.util.block;

import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class WoodTypeBlockItem extends BlockItem {
    protected final BibliocraftWoodType woodType;

    public WoodTypeBlockItem(GroupedHolder<BibliocraftWoodType, Block, ? extends Block> holder, BibliocraftWoodType woodType, Properties properties) {
        super(holder.get(woodType), properties.useBlockDescriptionPrefix());
        this.woodType = woodType;
    }
}
