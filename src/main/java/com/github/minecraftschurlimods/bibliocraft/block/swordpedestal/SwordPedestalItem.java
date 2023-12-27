package com.github.minecraftschurlimods.bibliocraft.block.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;

public class SwordPedestalItem extends BlockItem implements DyeableLeatherItem {
    public SwordPedestalItem() {
        super(BCBlocks.SWORD_PEDESTAL.get(), new Properties());
    }
}
