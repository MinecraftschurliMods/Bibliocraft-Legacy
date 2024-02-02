package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenAPI;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.ShapedNBTRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        BibliocraftDatagenAPI.get().generateRecipes(output);
        for (DyeColor color : DyeColor.values()) {
            ItemStack result = new ItemStack(BCItems.SWORD_PEDESTAL.get());
            DyeableLeatherItem.dyeArmor(result, List.of(DyeItem.byColor(color)));
            ShapedNBTRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                    .pattern(" S ")
                    .pattern("SWS")
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .define('W', BuiltInRegistries.ITEM.get(new ResourceLocation(color.getName() + "_wool")))
                    .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                    .save(output, new ResourceLocation(Bibliocraft.MOD_ID, "sword_pedestal_" + color.getName()));
        }
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BCItems.REDSTONE_BOOK.get())
                .requires(Items.BOOK)
                .requires(Items.REDSTONE_TORCH)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
    }
}
