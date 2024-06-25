package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public final class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        BibliocraftApi.getDatagenHelper().generateRecipes(output, BibliocraftApi.MOD_ID);
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            ItemStack swordPedestal = new ItemStack(BCItems.SWORD_PEDESTAL.get());
            swordPedestal.set(DataComponents.DYED_COLOR, new DyedItemColor(BCUtil.getTextureColor(color), true));
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, swordPedestal)
                    .pattern(" S ")
                    .pattern("SWS")
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .define('W', BuiltInRegistries.ITEM.get(new ResourceLocation(name + "_wool")))
                    .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                    .save(output, BCUtil.modLoc("color/" + name + "/sword_pedestal"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_GOLD_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', TagKey.create(BuiltInRegistries.ITEM.key(), BCUtil.forgeLoc("glass/" + name)))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('N', Tags.Items.NUGGETS_GOLD)
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output, BCUtil.modLoc("color/" + name + "/fancy_gold_lamp"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_IRON_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', TagKey.create(BuiltInRegistries.ITEM.key(), BCUtil.forgeLoc("glass/" + name)))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('N', Tags.Items.NUGGETS_IRON)
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output, BCUtil.modLoc("color/" + name + "/fancy_iron_lamp"));
        }
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_GOLD_LAMP.get())
                .pattern("CGC")
                .pattern(" I ")
                .pattern("NIN")
                .define('C', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('G', Items.GLOWSTONE)
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_IRON_LAMP.get())
                .pattern("CGC")
                .pattern(" I ")
                .pattern("NIN")
                .define('C', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('G', Items.GLOWSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCItems.CLIPBOARD.get())
                .pattern("I F")
                .pattern("PPP")
                .pattern(" L ")
                .define('I', Tags.Items.DYES_BLACK)
                .define('F', Tags.Items.FEATHERS)
                .define('P', Items.PAPER)
                .define('L', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);
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
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DINNER_PLATE.get())
                .pattern("SSS")
                .define('S', Items.SMOOTH_QUARTZ_SLAB)
                .unlockedBy("has_smooth_quartz", has(Items.SMOOTH_QUARTZ_SLAB))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DISC_RACK.get())
                .pattern("RRR")
                .pattern("SSS")
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
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
