package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPageCloningRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public interface BCRecipes {
    Supplier<RecipeSerializer<BigBookCloningRecipe>>        BIG_BOOK_CLONING        = BCRegistries.RECIPE_SERIALIZERS.register("big_book_cloning",        () -> new SimpleCraftingRecipeSerializer<>(BigBookCloningRecipe::new));
    Supplier<RecipeSerializer<TypewriterPageCloningRecipe>> TYPEWRITER_PAGE_CLONING = BCRegistries.RECIPE_SERIALIZERS.register("typewriter_page_cloning", () -> new SimpleCraftingRecipeSerializer<>(TypewriterPageCloningRecipe::new));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
