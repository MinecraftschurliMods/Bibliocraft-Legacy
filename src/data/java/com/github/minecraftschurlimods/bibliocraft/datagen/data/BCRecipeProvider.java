package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.crafting.IntersectionIngredient;

import java.util.concurrent.CompletableFuture;

public final class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            ItemStack swordPedestal = new ItemStack(BCItems.SWORD_PEDESTAL.get());
            swordPedestal.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), true));
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, swordPedestal)
                    .pattern(" S ")
                    .pattern("SWS")
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .define('W', BuiltInRegistries.ITEM.get(BCUtil.mcLoc(name + "_wool")))
                    .group("bibliocraft:sword_pedestal")
                    .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                    .save(output, BCUtil.bcLoc("color/" + name + "/sword_pedestal"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_GOLD_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', IntersectionIngredient.of(Ingredient.of(Tags.Items.GLASS_BLOCKS), Ingredient.of(TagKey.create(Registries.ITEM, BCUtil.cLoc("dyed/" + name)))))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('N', Tags.Items.NUGGETS_GOLD)
                    .group("bibliocraft:fancy_lamp")
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output, BCUtil.bcLoc("color/" + name + "/fancy_gold_lamp"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_IRON_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', IntersectionIngredient.of(Ingredient.of(Tags.Items.GLASS_BLOCKS), Ingredient.of(TagKey.create(Registries.ITEM, BCUtil.cLoc("dyed/" + name)))))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('N', Tags.Items.NUGGETS_IRON)
                    .group("bibliocraft:fancy_lamp")
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output, BCUtil.bcLoc("color/" + name + "/fancy_iron_lamp"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_GOLD_LANTERN.get(color))
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.get(BCUtil.mcLoc(name + "_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output, BCUtil.bcLoc("color/" + name + "/fancy_gold_lantern"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_IRON_LANTERN.get(color))
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.get(BCUtil.mcLoc(name + "_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output, BCUtil.bcLoc("color/" + name + "/fancy_iron_lantern"));
        }
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_GOLD_LAMP)
                .pattern("CGC")
                .pattern(" I ")
                .pattern("NIN")
                .define('C', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('G', Items.GLOWSTONE)
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .group("bibliocraft:fancy_lamp")
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_IRON_LAMP)
                .pattern("CGC")
                .pattern(" I ")
                .pattern("NIN")
                .define('C', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('G', Items.GLOWSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('N', Tags.Items.NUGGETS_IRON)
                .group("bibliocraft:fancy_lamp")
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_GOLD_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('C', Ingredient.of(Items.CANDLE))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_IRON_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Ingredient.of(Items.CANDLE))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        /*ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.SOUL_FANCY_GOLD_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('C', Ingredient.of(BuiltInRegistries.ITEM.get(BCUtil.modLoc("buzzier_bees", "soul_candle"))))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output.withConditions(new ModLoadedCondition("buzzier_bees")));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.SOUL_FANCY_IRON_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Ingredient.of(BuiltInRegistries.ITEM.get(BCUtil.modLoc("buzzier_bees", "soul_candle"))))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output.withConditions(new ModLoadedCondition("buzzier_bees")));*/
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCItems.CLIPBOARD)
                .pattern("I F")
                .pattern("PPP")
                .pattern(" L ")
                .define('I', Tags.Items.DYES_BLACK)
                .define('F', Tags.Items.FEATHERS)
                .define('P', Items.PAPER)
                .define('L', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.COOKIE_JAR)
                .pattern(" I ")
                .pattern("GCG")
                .pattern("GRG")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('C', Items.COOKIE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DESK_BELL)
                .pattern(" B ")
                .pattern(" I ")
                .pattern("IRI")
                .define('B', Items.STONE_BUTTON)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DINNER_PLATE)
                .pattern("SSS")
                .define('S', Items.SMOOTH_QUARTZ_SLAB)
                .unlockedBy("has_smooth_quartz", has(Items.SMOOTH_QUARTZ_SLAB))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.DISC_RACK)
                .pattern("RRR")
                .pattern("SSS")
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_CHAIN)
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .unlockedBy("has_gold_chain", has(BCItems.GOLD_CHAIN))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_LANTERN)
                .pattern("NNN")
                .pattern("NTN")
                .pattern("NNN")
                .define('T', Items.TORCH)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_SOUL_LANTERN)
                .pattern("NNN")
                .pattern("NTN")
                .pattern("NNN")
                .define('T', Items.SOUL_TORCH)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BCItems.BIG_BOOK)
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('B', Items.WRITABLE_BOOK)
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BCItems.IRON_FANCY_ARMOR_STAND)
                .pattern(" I ")
                .pattern(" I ")
                .pattern("SSS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BCItems.LOCK_AND_KEY)
                .pattern("NI")
                .pattern("NI")
                .pattern(" I")
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BCItems.PLUMB_LINE)
                .pattern("SSS")
                .pattern("S S")
                .pattern("I S")
                .define('S', Tags.Items.STRINGS)
                .define('I', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BCItems.STOCKROOM_CATALOG)
                .pattern("PDP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('D', Tags.Items.DYES_GREEN)
                .define('B', Items.BOOK)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCItems.TAPE_MEASURE)
                .pattern(" I ")
                .pattern("IRI")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', BCItems.TAPE_REEL)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BCItems.TAPE_REEL)
                .pattern("SSS")
                .pattern("SDS")
                .pattern("SSS")
                .define('S', Tags.Items.STRINGS)
                .define('D', Tags.Items.DYES_YELLOW)
                .unlockedBy("has_yellow_dye", has(Tags.Items.DYES_YELLOW))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BCItems.REDSTONE_BOOK)
                .requires(Items.BOOK)
                .requires(Items.REDSTONE_TORCH)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, BCItems.SLOTTED_BOOK)
                .requires(Items.BOOK)
                .requires(BCTags.Items.LABELS)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        SpecialRecipeBuilder.special(BigBookCloningRecipe::new).save(output, "big_book_cloning");
    }
}
