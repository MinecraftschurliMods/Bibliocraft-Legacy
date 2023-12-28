package com.github.minecraftschurlimods.bibliocraft.content.displaycase;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.level.block.state.properties.WoodType;

public class DisplayCaseItem extends BlockItem implements DyeableLeatherItem {
    public DisplayCaseItem(WoodType woodType) {
        super(BCBlocks.DISPLAY_CASE.get(woodType), new Properties());
    }
}
