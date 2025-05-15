package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record PrintingTableRecipeInput(NonNullList<ItemStack> left, ItemStack right) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < 9) return left.get(slot);
        if (slot == 9) return right;
        throw new IllegalArgumentException("No item for index " + slot);
    }

    @Override
    public int size() {
        return 10;
    }
}
