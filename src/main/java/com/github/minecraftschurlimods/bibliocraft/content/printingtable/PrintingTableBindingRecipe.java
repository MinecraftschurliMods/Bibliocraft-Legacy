package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import net.minecraft.world.item.ItemStack;

public abstract class PrintingTableBindingRecipe extends PrintingTableRecipe {
    public PrintingTableBindingRecipe(ItemStack result, int duration) {
        super(result, duration);
    }

    @Override
    public PrintingTableMode getMode() {
        return PrintingTableMode.BIND;
    }
}
