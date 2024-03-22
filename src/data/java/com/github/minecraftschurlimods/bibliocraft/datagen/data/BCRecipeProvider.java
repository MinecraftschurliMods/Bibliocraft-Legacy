package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public final class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        BibliocraftApi.getDatagenHelper().generateRecipes(output);
        for (DyeColor color : DyeColor.values()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DyeableLeatherItem.dyeArmor(new ItemStack(BCItems.SWORD_PEDESTAL.get()), List.of(DyeItem.byColor(color))))
                    .pattern(" S ")
                    .pattern("SWS")
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .define('W', BuiltInRegistries.ITEM.get(new ResourceLocation(color.getName() + "_wool")))
                    .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                    .save(output, new ResourceLocation(BibliocraftApi.MOD_ID, "sword_pedestal_" + color.getName()));
        }
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.COOKIE_JAR.get())
                .pattern(" I ")
                .pattern("GCG")
                .pattern("GRG")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('C', Items.COOKIE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DESK_BELL.get())
                .pattern(" B ")
                .pattern(" I ")
                .pattern("IRI")
                .define('B', Items.STONE_BUTTON)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BCItems.IRON_FANCY_ARMOR_STAND.get())
                .pattern(" I ")
                .pattern(" I ")
                .pattern("SSS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BCItems.REDSTONE_BOOK.get())
                .requires(Items.BOOK)
                .requires(Items.REDSTONE_TORCH)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
    }
}
