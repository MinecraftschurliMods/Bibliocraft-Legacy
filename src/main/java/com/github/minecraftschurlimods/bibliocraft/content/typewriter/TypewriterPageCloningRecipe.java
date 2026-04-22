package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TypewriterPageCloningRecipe extends CustomRecipe {
    private static final TypewriterPageCloningRecipe INSTANCE = new TypewriterPageCloningRecipe();
    public static final MapCodec<TypewriterPageCloningRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, TypewriterPageCloningRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int paper = 0;
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack written = input.getItem(i);
            if (written.isEmpty()) continue;
            if (written.is(BCItems.TYPEWRITER_PAGE)) {
                if (!stack.isEmpty()) return false;
                stack = written;
            } else if (written.is(BCTags.Items.TYPEWRITER_PAPER)) {
                paper++;
            } else return false;
        }
        return !stack.isEmpty() && paper > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        int paper = 0;
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack written = input.getItem(i);
            if (written.isEmpty()) continue;
            if (written.is(BCItems.TYPEWRITER_PAGE)) {
                if (!stack.isEmpty()) return ItemStack.EMPTY;
                stack = written;
            } else if (written.is(BCTags.Items.TYPEWRITER_PAPER)) {
                paper++;
            } else return ItemStack.EMPTY;
        }
        if (!stack.has(BCDataComponents.TYPEWRITER_PAGE)) return ItemStack.EMPTY;
        ItemStack result = stack.copyWithCount(paper);
        result.set(BCDataComponents.TYPEWRITER_PAGE, stack.get(BCDataComponents.TYPEWRITER_PAGE));
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> list = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = input.getItem(i);
            ItemStackTemplate craftingRemainder = stack.getCraftingRemainder();
            if (craftingRemainder != null) {
                list.set(i, craftingRemainder.create());
            } else if (stack.is(BCItems.TYPEWRITER_PAGE)) {
                list.set(i, stack.copyWithCount(1));
            }
        }
        return list;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return BCRecipes.TYPEWRITER_PAGE_CLONING.get();
    }
}
