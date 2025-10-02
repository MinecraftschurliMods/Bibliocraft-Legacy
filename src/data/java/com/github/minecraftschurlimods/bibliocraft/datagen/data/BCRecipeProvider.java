package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.EnchantmentLevelsNumberProvider;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBindingTypewriterPagesRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableCloningWithEnchantmentsRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMergingRecipe;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPageCloningRecipe;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
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
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.crafting.IntersectionIngredient;

import java.util.concurrent.CompletableFuture;

public final class BCRecipeProvider extends RecipeProvider {
    private BCRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            ItemStack swordPedestal = new ItemStack(BCItems.SWORD_PEDESTAL.get());
            swordPedestal.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor()));
            shaped(RecipeCategory.DECORATIONS, swordPedestal)
                    .pattern(" S ")
                    .pattern("SWS")
                    .define('S', Items.SMOOTH_STONE_SLAB)
                    .define('W', BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_wool")))
                    .group("bibliocraft:sword_pedestal")
                    .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                    .save(output, recipeKey("color/" + name + "/sword_pedestal"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_GOLD_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', IntersectionIngredient.of(tag(Tags.Items.GLASS_BLOCKS), tag(TagKey.create(Registries.ITEM, BCUtil.cLoc("dyed/" + name)))))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('N', Tags.Items.NUGGETS_GOLD)
                    .group("bibliocraft:fancy_lamp")
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output, recipeKey("color/" + name + "/fancy_gold_lamp"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_IRON_LAMP.get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', IntersectionIngredient.of(tag(Tags.Items.GLASS_BLOCKS), tag(TagKey.create(Registries.ITEM, BCUtil.cLoc("dyed/" + name)))))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('N', Tags.Items.NUGGETS_IRON)
                    .group("bibliocraft:fancy_lamp")
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output, recipeKey("color/" + name + "/fancy_iron_lamp"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_COPPER_LAMP.getWeathering(WeatheringCopper.WeatherState.UNAFFECTED).get(color))
                    .pattern("CGC")
                    .pattern(" I ")
                    .pattern("NIN")
                    .define('C', IntersectionIngredient.of(tag(Tags.Items.GLASS_BLOCKS), tag(TagKey.create(Registries.ITEM, BCUtil.cLoc("dyed/" + name)))))
                    .define('G', Items.GLOWSTONE)
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .define('N', Tags.Items.NUGGETS_COPPER)
                    .group("bibliocraft:fancy_lamp")
                    .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                    .save(output, recipeKey("color/" + name + "/fancy_copper_lamp"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_GOLD_LANTERN.get(color))
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output, recipeKey("color/" + name + "/fancy_gold_lantern"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_IRON_LANTERN.get(color))
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output, recipeKey("color/" + name + "/fancy_iron_lantern"));
            shaped(RecipeCategory.DECORATIONS, BCItems.FANCY_COPPER_LANTERN.getWeathering(WeatheringCopper.WeatherState.UNAFFECTED).get(color))
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                    .save(output, recipeKey("color/" + name + "/fancy_copper_lantern"));
            shaped(RecipeCategory.DECORATIONS, BCItems.TYPEWRITER.get(color))
                    .pattern("IPI")
                    .pattern("BDB")
                    .pattern("CCC")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('P', Items.PAPER)
                    .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                    .define('D', Tags.Items.DYES_BLACK)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_terracotta"))))
                    .group("bibliocraft:typewriter")
                    .unlockedBy("has_terracotta", has(BuiltInRegistries.ITEM.getValue(BCUtil.mcLoc(name + "_terracotta"))))
                    .save(output, recipeKey("color/" + name + "/typewriter"));
        }
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_GOLD_LAMP)
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
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_IRON_LAMP)
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
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_COPPER_LAMP.getWeathering(WeatheringCopper.WeatherState.UNAFFECTED))
                .pattern("CGC")
                .pattern(" I ")
                .pattern("NIN")
                .define('C', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('G', Items.GLOWSTONE)
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('N', Tags.Items.NUGGETS_COPPER)
                .group("bibliocraft:fancy_lamp")
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_GOLD_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('C', Ingredient.of(Items.CANDLE))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_IRON_LANTERN)
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', Ingredient.of(Items.CANDLE))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_FANCY_COPPER_LANTERN.getWeathering(WeatheringCopper.WeatherState.UNAFFECTED))
                .pattern("GIG")
                .pattern("ICI")
                .pattern("GIG")
                .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                .define('I', Tags.Items.INGOTS_COPPER)
                .define('C', Ingredient.of(Items.CANDLE))
                .group("bibliocraft:fancy_lantern")
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.CLEAR_TYPEWRITER)
                .pattern("IPI")
                .pattern("BDB")
                .pattern("CCC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', Items.PAPER)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('D', Tags.Items.DYES_BLACK)
                .define('C', Ingredient.of(Items.TERRACOTTA))
                .group("bibliocraft:typewriter")
                .unlockedBy("has_terracotta", has(Items.TERRACOTTA))
                .save(output);
        if (ModList.get().isLoaded("buzzier_bees")) {
            shaped(RecipeCategory.DECORATIONS, BCItems.SOUL_FANCY_GOLD_LANTERN)
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.modLoc("buzzier_bees", "soul_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                    .save(output.withConditions(new ModLoadedCondition("buzzier_bees")));
            shaped(RecipeCategory.DECORATIONS, BCItems.SOUL_FANCY_IRON_LANTERN)
                    .pattern("GIG")
                    .pattern("ICI")
                    .pattern("GIG")
                    .define('G', tag(Tags.Items.GLASS_PANES_COLORLESS))
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('C', Ingredient.of(BuiltInRegistries.ITEM.getValue(BCUtil.modLoc("buzzier_bees", "soul_candle"))))
                    .group("bibliocraft:fancy_lantern")
                    .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                    .save(output.withConditions(new ModLoadedCondition("buzzier_bees")));
        }
        shaped(RecipeCategory.TOOLS, BCItems.CLIPBOARD)
                .pattern("I F")
                .pattern("PPP")
                .pattern(" L ")
                .define('I', Tags.Items.DYES_BLACK)
                .define('F', Tags.Items.FEATHERS)
                .define('P', Items.PAPER)
                .define('L', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.COOKIE_JAR)
                .pattern(" I ")
                .pattern("GCG")
                .pattern("GRG")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('C', Items.COOKIE)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.DESK_BELL)
                .pattern(" B ")
                .pattern(" I ")
                .pattern("IRI")
                .define('B', Items.STONE_BUTTON)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.DINNER_PLATE)
                .pattern("SSS")
                .define('S', Items.SMOOTH_QUARTZ_SLAB)
                .unlockedBy("has_smooth_quartz", has(Items.SMOOTH_QUARTZ_SLAB))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.DISC_RACK)
                .pattern("RRR")
                .pattern("SSS")
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_wooden_slab", has(ItemTags.WOODEN_SLABS))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.IRON_FANCY_ARMOR_STAND)
                .pattern(" I ")
                .pattern(" I ")
                .pattern("SSS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .unlockedBy("has_smooth_stone_slab", has(Items.SMOOTH_STONE_SLAB))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_CHAIN)
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('I', Tags.Items.INGOTS_GOLD)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .unlockedBy("has_gold_chain", has(BCItems.GOLD_CHAIN))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_LANTERN)
                .pattern("NNN")
                .pattern("NTN")
                .pattern("NNN")
                .define('T', Items.TORCH)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.GOLD_SOUL_LANTERN)
                .pattern("NNN")
                .pattern("NTN")
                .pattern("NNN")
                .define('T', Items.SOUL_TORCH)
                .define('N', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_gold_nugget", has(Tags.Items.NUGGETS_GOLD))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.PRINTING_TABLE)
                .pattern("CCC")
                .pattern("PPP")
                .pattern("BRB")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('P', ItemTags.PLANKS)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, BCItems.IRON_PRINTING_TABLE)
                .pattern("CCC")
                .pattern("III")
                .pattern("BRB")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.BIG_BOOK)
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('B', Items.WRITABLE_BOOK)
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.LOCK_AND_KEY)
                .pattern("NI")
                .pattern("NI")
                .pattern(" I")
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.PLUMB_LINE)
                .pattern("SSS")
                .pattern("S S")
                .pattern("I S")
                .define('S', Tags.Items.STRINGS)
                .define('I', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.STOCKROOM_CATALOG)
                .pattern("PDP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('D', Tags.Items.DYES_GREEN)
                .define('B', Items.BOOK)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.TAPE_MEASURE)
                .pattern(" I ")
                .pattern("IRI")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', BCItems.TAPE_REEL)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);
        shaped(RecipeCategory.TOOLS, BCItems.TAPE_REEL)
                .pattern("SSS")
                .pattern("SDS")
                .pattern("SSS")
                .define('S', Tags.Items.STRINGS)
                .define('D', Tags.Items.DYES_YELLOW)
                .unlockedBy("has_yellow_dye", has(Tags.Items.DYES_YELLOW))
                .save(output);
        shapeless(RecipeCategory.TOOLS, BCItems.REDSTONE_BOOK)
                .requires(Items.BOOK)
                .requires(Items.REDSTONE_TORCH)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        shapeless(RecipeCategory.TOOLS, BCItems.SLOTTED_BOOK)
                .requires(Items.BOOK)
                .requires(BCTags.Items.LABELS)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);
        SpecialRecipeBuilder.special(BigBookCloningRecipe::new).save(output, "big_book_cloning");
        SpecialRecipeBuilder.special(TypewriterPageCloningRecipe::new).save(output, "typewriter_page_cloning");
        new PrintingTableCloningRecipe.Builder(new ItemStack(BCItems.CLIPBOARD.get()), 100)
                .addDataComponentType(BCDataComponents.CLIPBOARD_CONTENT.get())
                .addIngredient(Ingredient.of(BCItems.CLIPBOARD.get()))
                .unlockedBy("has_clipboard", has(BCItems.CLIPBOARD.get()))
                .save(output, recipeKey("clipboard_cloning_in_printing_table"));
        new PrintingTableCloningRecipe.Builder(new ItemStack(BCItems.TYPEWRITER_PAGE.get()), 100)
                .addDataComponentType(BCDataComponents.TYPEWRITER_PAGE.get())
                .addIngredient(tag(BCTags.Items.TYPEWRITER_PAPER))
                .unlockedBy("has_paper", has(BCTags.Items.TYPEWRITER_PAPER))
                .save(output, recipeKey("typewriter_page_cloning_in_printing_table"));
        new PrintingTableCloningRecipe.Builder(new ItemStack(Items.WRITABLE_BOOK), 100)
                .addDataComponentType(DataComponents.WRITABLE_BOOK_CONTENT)
                .addIngredient(Ingredient.of(Items.WRITABLE_BOOK))
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output, recipeKey("writable_book_cloning_in_printing_table"));
        new PrintingTableCloningRecipe.Builder(new ItemStack(Items.WRITTEN_BOOK), 100)
                .addDataComponentType(DataComponents.WRITTEN_BOOK_CONTENT)
                .addIngredient(Ingredient.of(Items.WRITABLE_BOOK))
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output, recipeKey("written_book_cloning_in_printing_table"));
        new PrintingTableCloningRecipe.Builder(new ItemStack(BCItems.BIG_BOOK.get()), 100)
                .addDataComponentType(BCDataComponents.BIG_BOOK_CONTENT.get())
                .addIngredient(Ingredient.of(BCItems.BIG_BOOK.get()))
                .unlockedBy("has_big_book", has(BCItems.BIG_BOOK.get()))
                .save(output, recipeKey("big_book_cloning_in_printing_table"));
        new PrintingTableCloningRecipe.Builder(new ItemStack(BCItems.WRITTEN_BIG_BOOK.get()), 100)
                .addDataComponentType(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get())
                .addIngredient(Ingredient.of(BCItems.BIG_BOOK.get()))
                .unlockedBy("has_big_book", has(BCItems.BIG_BOOK.get()))
                .save(output, recipeKey("written_big_book_cloning_in_printing_table"));
        new PrintingTableCloningWithEnchantmentsRecipe.Builder(new ItemStack(Items.ENCHANTED_BOOK), 600)
                .addIngredient(Ingredient.of(Items.BOOK))
                .experienceCost(new EnchantmentLevelsNumberProvider(DataComponents.STORED_ENCHANTMENTS, ConstantValue.exactly(1), ConstantValue.exactly(2)))
                .unlockedBy("has_enchanted_book", has(Items.ENCHANTED_BOOK))
                .save(output, recipeKey("enchanted_book_cloning_in_printing_table"));
        new PrintingTableMergingRecipe.Builder(Ingredient.of(BCItems.CLIPBOARD.get()), new ItemStack(BCItems.CLIPBOARD.get()), 200)
                .addMerger(BCDataComponents.CLIPBOARD_CONTENT.get(), "title", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(BCDataComponents.CLIPBOARD_CONTENT.get(), "active", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(BCDataComponents.CLIPBOARD_CONTENT.get(), "pages", PrintingTableMergingRecipe.MergeMethod.APPEND)
                .unlockedBy("has_clipboard", has(BCItems.CLIPBOARD.get()))
                .save(output, recipeKey("clipboard_merging"));
        new PrintingTableMergingRecipe.Builder(Ingredient.of(Items.WRITABLE_BOOK), new ItemStack(Items.WRITABLE_BOOK), 200)
                .addMerger(DataComponents.WRITABLE_BOOK_CONTENT, "pages", PrintingTableMergingRecipe.MergeMethod.APPEND)
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output, recipeKey("writable_book_merging"));
        new PrintingTableMergingRecipe.Builder(Ingredient.of(Items.WRITABLE_BOOK), new ItemStack(Items.WRITTEN_BOOK), 200)
                .addMerger(DataComponents.WRITTEN_BOOK_CONTENT, "title", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(DataComponents.WRITTEN_BOOK_CONTENT, "author", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(DataComponents.WRITTEN_BOOK_CONTENT, "generation", PrintingTableMergingRecipe.MergeMethod.MIN)
                .addMerger(DataComponents.WRITTEN_BOOK_CONTENT, "pages", PrintingTableMergingRecipe.MergeMethod.APPEND)
                .unlockedBy("has_writable_book", has(Items.WRITABLE_BOOK))
                .save(output, recipeKey("written_book_merging"));
        new PrintingTableMergingRecipe.Builder(Ingredient.of(BCItems.BIG_BOOK.get()), new ItemStack(BCItems.BIG_BOOK.get()), 200)
                .addMerger(BCDataComponents.BIG_BOOK_CONTENT.get(), "pages", PrintingTableMergingRecipe.MergeMethod.APPEND)
                .addMerger(BCDataComponents.BIG_BOOK_CONTENT.get(), "current_page", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .unlockedBy("has_big_book", has(BCItems.BIG_BOOK.get()))
                .save(output, recipeKey("big_book_merging"));
        new PrintingTableMergingRecipe.Builder(Ingredient.of(BCItems.BIG_BOOK.get()), new ItemStack(BCItems.WRITTEN_BIG_BOOK.get()), 200)
                .addMerger(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get(), "pages", PrintingTableMergingRecipe.MergeMethod.APPEND)
                .addMerger(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get(), "title", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get(), "author", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .addMerger(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get(), "generation", PrintingTableMergingRecipe.MergeMethod.MIN)
                .addMerger(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT.get(), "current_page", PrintingTableMergingRecipe.MergeMethod.FIRST)
                .unlockedBy("has_big_book", has(BCItems.BIG_BOOK.get()))
                .save(output, recipeKey("written_big_book_merging"));
        new PrintingTableBindingTypewriterPagesRecipe.Builder(tag(Tags.Items.LEATHERS), 200)
                .unlockedBy("has_leather", has(Tags.Items.LEATHERS))
                .save(output, recipeKey("typewriter_pages_binding"));
    }

    private static ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, BCUtil.bcLoc(name));
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new BCRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
