package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        for (Map.Entry<WoodType, DeferredHolder<Item, BlockItem>> entry : BCItems.BOOKCASE.map().entrySet()) {
            BlockFamily woodFamily = woodFamily(entry.getKey());
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, entry.getValue().get())
                    .pattern("PSP")
                    .pattern("PSP")
                    .pattern("PSP")
                    .define('P', woodFamily.getBaseBlock())
                    .define('S', woodFamily.get(BlockFamily.Variant.SLAB))
                    .unlockedBy("has_planks", has(woodFamily.getBaseBlock()))
                    .save(output);
        }
        for (Map.Entry<WoodType, DeferredHolder<Item, BlockItem>> entry : BCItems.POTION_SHELF.map().entrySet()) {
            BlockFamily woodFamily = woodFamily(entry.getKey());
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, entry.getValue().get())
                    .pattern("SSS")
                    .pattern("PBP")
                    .pattern("SSS")
                    .define('S', woodFamily.get(BlockFamily.Variant.SLAB))
                    .define('P', woodFamily.getBaseBlock())
                    .define('B', Items.GLASS_BOTTLE)
                    .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                    .save(output);
        }
        for (Map.Entry<WoodType, DeferredHolder<Item, BlockItem>> entry : BCItems.TOOL_RACK.map().entrySet()) {
            BlockFamily woodFamily = woodFamily(entry.getKey());
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, entry.getValue().get())
                    .pattern("SSS")
                    .pattern("SIS")
                    .pattern("SSS")
                    .define('S', woodFamily.get(BlockFamily.Variant.SLAB))
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output);
        }
    }

    private static BlockFamily woodFamily(WoodType wood) {
        if (wood == WoodType.OAK) return BlockFamilies.OAK_PLANKS;
        if (wood == WoodType.SPRUCE) return BlockFamilies.SPRUCE_PLANKS;
        if (wood == WoodType.BIRCH) return BlockFamilies.BIRCH_PLANKS;
        if (wood == WoodType.JUNGLE) return BlockFamilies.JUNGLE_PLANKS;
        if (wood == WoodType.ACACIA) return BlockFamilies.ACACIA_PLANKS;
        if (wood == WoodType.DARK_OAK) return BlockFamilies.DARK_OAK_PLANKS;
        if (wood == WoodType.CRIMSON) return BlockFamilies.CRIMSON_PLANKS;
        if (wood == WoodType.WARPED) return BlockFamilies.WARPED_PLANKS;
        if (wood == WoodType.MANGROVE) return BlockFamilies.MANGROVE_PLANKS;
        if (wood == WoodType.BAMBOO) return BlockFamilies.BAMBOO_PLANKS;
        if (wood == WoodType.CHERRY) return BlockFamilies.CHERRY_PLANKS;
        return null;
    }
}
