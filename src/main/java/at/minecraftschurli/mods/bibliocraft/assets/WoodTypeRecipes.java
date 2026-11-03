package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Optional;

/// Recipes for the Bibliocraft blocks and items of the given wood types.
final class WoodTypeRecipes extends RecipeProvider {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeRecipes(HolderLookup.Provider registries, RecipeOutput output, List<BibliocraftWoodType> woodTypes) {
        super(registries, output);
        this.woodTypes = woodTypes;
    }

    @Override
    protected void buildRecipes() {
        for (BibliocraftWoodType woodType : woodTypes) {
            HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
            String prefix = "wood/" + woodType.getRegistrationPrefix() + "/";
            Block planks = woodType.family().get().getBaseBlock();
            Block slab = woodType.family().get().get(BlockFamily.Variant.SLAB);
            TagKey<Item> stick = Tags.Items.RODS_WOODEN;
            shapedRecipe(itemLookup, BCItems.BOOKCASE.get(woodType), woodType, "bookcases")
                    .pattern("PSP")
                    .pattern("PSP")
                    .pattern("PSP")
                    .define('P', planks)
                    .define('S', slab)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "bookcase")));
            shapedRecipe(itemLookup, BCItems.FANCY_ARMOR_STAND.get(woodType), woodType, "fancy_armor_stands")
                    .pattern(" R ")
                    .pattern(" R ")
                    .pattern("SSS")
                    .define('S', slab)
                    .define('R', Tags.Items.RODS_WOODEN)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "fancy_armor_stand")));
            shapedRecipe(itemLookup, BCItems.FANCY_CLOCK.get(woodType), woodType, "fancy_clock")
                    .pattern("SCS")
                    .pattern("SRS")
                    .pattern("SIS")
                    .define('S', slab)
                    .define('C', Items.CLOCK)
                    .define('R', Tags.Items.RODS_WOODEN)
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "fancy_clock")));
            shapedRecipe(itemLookup, BCItems.FANCY_CRAFTER.get(woodType), woodType, "fancy_crafter")
                    .pattern("ITF")
                    .pattern("PGP")
                    .pattern("PCP")
                    .define('P', planks)
                    .define('I', Tags.Items.DYES_BLACK)
                    .define('T', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                    .define('F', Tags.Items.FEATHERS)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .define('C', Items.CRAFTER)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "fancy_crafter")));
            shapedRecipe(itemLookup, BCItems.FANCY_SIGN.get(woodType), woodType, "fancy_sign")
                    .pattern("P#P")
                    .pattern("P#P")
                    .pattern(" R ")
                    .define('P', planks)
                    .define('#', Items.PAPER)
                    .define('R', Tags.Items.RODS_WOODEN)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "fancy_sign")));
            shapelessRecipe(itemLookup, BCItems.GRANDFATHER_CLOCK.get(woodType), woodType, "grandfather_clock")
                    .requires(BCItems.FANCY_CLOCK.get(woodType))
                    .requires(BCItems.FANCY_CLOCK.get(woodType))
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "grandfather_clock")));
            shapedRecipe(itemLookup, BCItems.LABEL.get(woodType), woodType, "labels")
                    .pattern("SSS")
                    .pattern("SSS")
                    .define('S', slab)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "label")));
            shapedRecipe(itemLookup, BCItems.POTION_SHELF.get(woodType), woodType, "potion_shelves")
                    .pattern("SSS")
                    .pattern("P#P")
                    .pattern("SSS")
                    .define('P', planks)
                    .define('S', slab)
                    .define('#', Items.GLASS_BOTTLE)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "potion_shelf")));
            shapedRecipe(itemLookup, BCItems.SHELF.get(woodType), woodType, "shelves")
                    .pattern("SSS")
                    .pattern(" P ")
                    .pattern("SSS")
                    .define('P', planks)
                    .define('S', slab)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "shelf")));
            shapedRecipe(itemLookup, BCItems.TABLE.get(woodType), woodType, "tables")
                    .pattern("SSS")
                    .pattern(" P ")
                    .pattern(" P ")
                    .define('P', planks)
                    .define('S', slab)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "table")));
            shapedRecipe(itemLookup, BCItems.TOOL_RACK.get(woodType), woodType, "tool_racks")
                    .pattern("SSS")
                    .pattern("S#S")
                    .pattern("SSS")
                    .define('S', slab)
                    .define('#', Tags.Items.INGOTS_IRON)
                    .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "tool_rack")));
            for (DyeColor color : DyeColor.values()) {
                Item wool = itemLookup.getOrThrow(ResourceKey.create(Registries.ITEM, BCUtil.mcLoc(color.getName() + "_wool"))).value();
                prefix = "color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/";
                shapedRecipe(itemLookup, BCItems.DISPLAY_CASE.get(woodType, color), woodType, "display_cases")
                        .pattern("SGS")
                        .pattern("SWS")
                        .pattern("SSS")
                        .define('S', slab)
                        .define('W', wool)
                        .define('G', Tags.Items.GLASS_BLOCKS)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "display_case")));
                shapedRecipe(itemLookup, BCItems.SEAT.get(woodType, color), woodType, "seats")
                        .pattern(" W ")
                        .pattern(" S ")
                        .pattern("RSR")
                        .define('S', slab)
                        .define('R', stick)
                        .define('W', wool)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "seat")));
                shapedRecipe(itemLookup, BCItems.SMALL_SEAT_BACK.get(woodType, color), woodType, "small_seat_backs")
                        .pattern("W")
                        .pattern("S")
                        .define('S', slab)
                        .define('W', wool)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "small_seat_back")));
                shapedRecipe(itemLookup, BCItems.RAISED_SEAT_BACK.get(woodType, color), woodType, "raised_seat_backs")
                        .pattern(" W ")
                        .pattern(" S ")
                        .pattern("R R")
                        .define('S', slab)
                        .define('R', stick)
                        .define('W', wool)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "raised_seat_back")));
                shapedRecipe(itemLookup, BCItems.FLAT_SEAT_BACK.get(woodType, color), woodType, "flat_seat_backs")
                        .pattern("RWR")
                        .pattern("RSR")
                        .pattern("R R")
                        .define('S', slab)
                        .define('R', stick)
                        .define('W', wool)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "flat_seat_back")));
                shapedRecipe(itemLookup, BCItems.TALL_SEAT_BACK.get(woodType, color), woodType, "tall_seat_backs")
                        .pattern("S")
                        .pattern("#")
                        .define('S', slab)
                        .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "tall_seat_back")));
                shapedRecipe(itemLookup, BCItems.FANCY_SEAT_BACK.get(woodType, color), woodType, "fancy_seat_backs")
                        .pattern("S#S")
                        .define('S', slab)
                        .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(BibliocraftApi.MOD_ID, prefix + "fancy_seat_back")));
            }
        }
    }

    /// Adds a shaped recipe for an item.
    ///
    /// @param item     The item.
    /// @param woodType The [BibliocraftWoodType].
    /// @return A [ShapedRecipeBuilder] with the
    private static ShapedRecipeBuilder shapedRecipe(HolderLookup.RegistryLookup<Item> lookup, Item item, BibliocraftWoodType woodType, String group) {
        return ShapedRecipeBuilder.shaped(lookup, RecipeCategory.DECORATIONS, item)
                .group(BibliocraftApi.MOD_ID + ":" + group)
                .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(lookup, woodType.family().get().getBaseBlock()).build()))));
    }

    /// Adds a shapeless recipe for an item.
    ///
    /// @param item     The item.
    /// @param woodType The [BibliocraftWoodType].
    /// @return A [ShapelessRecipeBuilder] with the
    private static ShapelessRecipeBuilder shapelessRecipe(HolderLookup.RegistryLookup<Item> lookup, Item item, BibliocraftWoodType woodType, String group) {
        return ShapelessRecipeBuilder.shapeless(lookup, RecipeCategory.DECORATIONS, item)
                .group(BibliocraftApi.MOD_ID + ":" + group)
                .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(lookup, woodType.family().get().getBaseBlock()).build()))));
    }
}
