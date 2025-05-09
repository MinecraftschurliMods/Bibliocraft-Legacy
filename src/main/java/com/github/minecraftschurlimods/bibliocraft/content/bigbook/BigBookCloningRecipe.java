package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class BigBookCloningRecipe extends CustomRecipe {
    public BigBookCloningRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int books = 0;
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack written = input.getItem(i);
            if (written.isEmpty()) continue;
            if (written.is(BCItems.WRITTEN_BIG_BOOK)) {
                if (!stack.isEmpty()) return false;
                stack = written;
            } else if (written.is(BCItems.BIG_BOOK)) {
                books++;
            } else return false;
        }
        return !stack.isEmpty() && books > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        int books = 0;
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack written = input.getItem(i);
            if (written.isEmpty()) continue;
            if (written.is(BCItems.WRITTEN_BIG_BOOK)) {
                if (!stack.isEmpty()) return ItemStack.EMPTY;
                stack = written;
            } else if (written.is(BCItems.BIG_BOOK)) {
                books++;
            } else return ItemStack.EMPTY;
        }
        WrittenBigBookContent content = stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT);
        if (stack.isEmpty() || books < 1 || content == null) return ItemStack.EMPTY;
        WrittenBigBookContent contentCopy = content.tryCraftCopy();
        if (contentCopy == null) return ItemStack.EMPTY;
        ItemStack result = stack.copyWithCount(books);
        result.set(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, contentCopy);
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> list = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            } else if (stack.is(BCItems.WRITTEN_BIG_BOOK)) {
                list.set(i, stack.copyWithCount(1));
            }
        }
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.BIG_BOOK_CLONING.get();
    }
}
