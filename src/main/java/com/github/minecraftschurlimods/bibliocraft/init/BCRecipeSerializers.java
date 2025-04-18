package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public interface BCRecipeSerializers {
    Supplier<RecipeSerializer<BigBookCloningRecipe>> BIG_BOOK_CLONING = BCRegistries.RECIPE_SERIALIZERS.register("big_book_cloning", () -> new SimpleCraftingRecipeSerializer<>(BigBookCloningRecipe::new));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
