package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public final class BCRecipeProvider extends RecipeProvider {
    //Defining this here so the helpers can use it
    private RecipeOutput output;

    public BCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        this.output = output;
        forEachWoodType(BCItems.BOOKCASE, (builder, family) -> builder
                .pattern("PSP")
                .pattern("PSP")
                .pattern("PSP")
                .define('P', family.getBaseBlock())
                .define('S', family.get(BlockFamily.Variant.SLAB)));
        forEachWoodType(BCItems.FANCY_ARMOR_STAND, (builder, family) -> builder
                .pattern(" R ")
                .pattern(" R ")
                .pattern("SSS")
                .define('S', family.get(BlockFamily.Variant.SLAB))
                .define('R', Tags.Items.RODS_WOODEN));
        forEachWoodType(BCItems.POTION_SHELF, (builder, family) -> builder
                .pattern("SSS")
                .pattern("PBP")
                .pattern("SSS")
                .define('S', family.get(BlockFamily.Variant.SLAB))
                .define('P', family.getBaseBlock())
                .define('B', Items.GLASS_BOTTLE));
        forEachWoodType(BCItems.SHELF, (builder, family) -> builder
                .pattern("SSS")
                .pattern(" P ")
                .pattern("SSS")
                .define('P', family.getBaseBlock())
                .define('S', family.get(BlockFamily.Variant.SLAB)));
        forEachWoodType(BCItems.TOOL_RACK, (builder, family) -> builder
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', family.get(BlockFamily.Variant.SLAB))
                .define('I', Tags.Items.INGOTS_IRON));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BCItems.IRON_FANCY_ARMOR_STAND.get())
                .pattern(" R ")
                .pattern(" R ")
                .pattern("SSS")
                .define('S', Items.SMOOTH_STONE_SLAB)
                .define('R', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    /**
     * @param wood The {@link WoodType} to get the {@link BlockFamily} for.
     * @return The {@link BlockFamily} associated with the given {@link WoodType}.
     */
    @Nullable
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

    /**
     * @param holder            The {@link WoodTypeDeferredHolder} to generate the recipes for.
     * @param recipeTransformer The function to generate the recipe with.
     */
    private void forEachWoodType(WoodTypeDeferredHolder<Item, ? extends Item> holder, BiFunction<ShapedRecipeBuilder, BlockFamily, ShapedRecipeBuilder> recipeTransformer) {
        for (Map.Entry<WoodType, ? extends DeferredHolder<Item, ? extends Item>> entry : holder.map().entrySet()) {
            BlockFamily family = woodFamily(entry.getKey());
            if (family == null) throw new RuntimeException("Tried to generate a recipe with an unknown wood type");
            recipeTransformer.apply(ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, entry.getValue().get()), family).unlockedBy("has_planks", has(family.getBaseBlock())).save(output);
        }
    }
}
