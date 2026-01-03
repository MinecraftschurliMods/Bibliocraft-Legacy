package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.BlockLootTableProvider;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCModelTemplates;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.BlockModelDatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.github.minecraftschurlimods.bibliocraft.util.BlockModelDatagenUtil.*;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public final class BibliocraftDatagenHelperImpl implements BibliocraftDatagenHelper {
    public static final String NAME_SUFFIX = " wood type registration helper";
    private final List<BibliocraftWoodType> WOOD_TYPES = new ArrayList<>();

    @Override
    public synchronized void addWoodTypeToGenerate(BibliocraftWoodType woodType) {
        WOOD_TYPES.add(woodType);
    }

    @Override
    public List<BibliocraftWoodType> getWoodTypesToGenerate() {
        return Collections.unmodifiableList(WOOD_TYPES);
    }

    @Override
    public void generateAll(String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator.PackGenerator clientPack, DataGenerator.PackGenerator serverPack, LanguageProvider languageProvider, Function<TagKey<Block>, TagAppender<Block, Block>> blockTagAppenderFactory, Function<TagKey<Item>, TagAppender<Item, Item>> itemTagAppenderFactory) {
        clientPack.addProvider(output -> new Models(output, modId));
        serverPack.addProvider(output -> new BlockLootTables(output, lookupProvider));
        serverPack.addProvider(output -> new RecipesRunner(output, lookupProvider, modId));
        addBlockTags(blockTagAppenderFactory);
        addItemTags(itemTagAppenderFactory);
        addTranslations(languageProvider);
    }

    private class Models extends ModelProvider {
        private Models(PackOutput output, String modId) {
            super(output, modId);
        }

        @Override
        protected Stream<? extends Holder<Block>> getKnownBlocks() {
            return Stream.concat(
                    BCBlocks.WOODEN.stream().flatMap(holder -> WOOD_TYPES.stream().map(holder::holder)),
                    BCBlocks.WOODEN_COLORED.stream().flatMap(holder -> WOOD_TYPES.stream().flatMap(holder::streamGroup))
            );
        }

        @Override
        protected Stream<? extends Holder<Item>> getKnownItems() {
            return Stream.concat(
                    BCItems.WOODEN.stream().flatMap(holder -> WOOD_TYPES.stream().map(holder::holder)),
                    BCItems.WOODEN_COLORED.stream().flatMap(holder -> WOOD_TYPES.stream().flatMap(holder::streamGroup))
            );
        }

        @Override
        protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
            for (BibliocraftWoodType woodType : WOOD_TYPES) {
                for (GroupedModelTemplate<BibliocraftWoodType> template : WOODEN) {
                    template.build(blockModels, woodType);
                }
                for (DyeColor color : DyeColor.values()) {
                    for (GroupedModelTemplate2<BibliocraftWoodType, DyeColor> template : WOODEN_COLORED) {
                        template.builder(blockModels, woodType, color).build();
                    }
                    seatBackItemModel(itemModels, BCItems.SMALL_SEAT_BACK, woodType, color);
                    seatBackItemModel(itemModels, BCItems.RAISED_SEAT_BACK, woodType, color);
                    seatBackItemModel(itemModels, BCItems.FLAT_SEAT_BACK, woodType, color);
                    seatBackItemModel(itemModels, BCItems.TALL_SEAT_BACK, woodType, color);
                    seatBackItemModel(itemModels, BCItems.FANCY_SEAT_BACK, woodType, color);
                }
                createItemModel(itemModels, BCItems.FANCY_CLOCK, BCModelTemplates.FANCY_CLOCK_INVENTORY, woodType);
                createItemModel(itemModels, BCItems.GRANDFATHER_CLOCK, BCModelTemplates.GRANDFATHER_CLOCK_INVENTORY, woodType);
                createItemModel(itemModels, BCItems.FANCY_ARMOR_STAND, BCModelTemplates.FANCY_ARMOR_STAND_INVENTORY, woodType);
            }
        }

        private static void seatBackItemModel(ItemModelGenerators itemModels, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> seatBack, BibliocraftWoodType woodType, DyeColor color) {
            SeatBackItem item = seatBack.get(woodType, color);
            itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item).withPath("block/" + BlockModelDatagenUtil.nameFor(woodType, color, "seat_back") + "_" + item.type.getSerializedName())));
        }

        @Override
        public String getName() {
            return super.getName() + NAME_SUFFIX;
        }
    }

    private class BlockLootTables extends BlockLootTableProvider {
        private BlockLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void generate() {
            for (BibliocraftWoodType woodType : WOOD_TYPES) {
                // @formatter:off
                loot(BCBlocks.BOOKCASE.get(woodType),          woodType, DatagenUtil::createNameableTable);
                loot(BCBlocks.FANCY_ARMOR_STAND.get(woodType), woodType, DatagenUtil::createFancyArmorStandTable);
                loot(BCBlocks.FANCY_CLOCK.get(woodType),       woodType, DatagenUtil::createDefaultTable);
                loot(BCBlocks.WALL_FANCY_CLOCK.get(woodType),  woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_CLOCK.get(woodType)));
                loot(BCBlocks.FANCY_CRAFTER.get(woodType),     woodType, DatagenUtil::createNameableTable);
                loot(BCBlocks.FANCY_SIGN.get(woodType),        woodType, DatagenUtil::createDefaultTable);
                loot(BCBlocks.WALL_FANCY_SIGN.get(woodType),   woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_SIGN.get(woodType)));
                loot(BCBlocks.GRANDFATHER_CLOCK.get(woodType), woodType, DatagenUtil::createGrandfatherClockTable);
                loot(BCBlocks.LABEL.get(woodType),             woodType, DatagenUtil::createNameableTable);
                loot(BCBlocks.POTION_SHELF.get(woodType),      woodType, DatagenUtil::createNameableTable);
                loot(BCBlocks.SHELF.get(woodType),             woodType, DatagenUtil::createNameableTable);
                loot(BCBlocks.TABLE.get(woodType),             woodType, DatagenUtil::createDefaultTable);
                loot(BCBlocks.TOOL_RACK.get(woodType),         woodType, DatagenUtil::createNameableTable);
                for (DyeColor color : DyeColor.values()) {
                    loot(BCBlocks.DISPLAY_CASE.get(woodType, color),      woodType, DatagenUtil::createDefaultTable);
                    loot(BCBlocks.WALL_DISPLAY_CASE.get(woodType, color), woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.DISPLAY_CASE.get(woodType, color)));
                    loot(BCBlocks.SEAT.get(woodType, color),              woodType, DatagenUtil::createDefaultTable);
                    loot(BCBlocks.SEAT_BACK.get(woodType, color),         woodType, block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                            .add(LootItem.lootTableItem(BCItems.SMALL_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.SMALL))))
                            .add(LootItem.lootTableItem(BCItems.RAISED_SEAT_BACK.get(woodType, color)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.RAISED))))
                            .add(LootItem.lootTableItem(BCItems.FLAT_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FLAT))))
                            .add(LootItem.lootTableItem(BCItems.TALL_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.TALL))))
                            .add(LootItem.lootTableItem(BCItems.FANCY_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FANCY))))));
                }
                // @formatter:on
            }
        }

        @Override
        public String getName() {
            return super.getName() + NAME_SUFFIX;
        }

        /**
         * Adds a loot table for a block.
         *
         * @param block    The block.
         * @param factory  A function that returns a {@link LootTable.Builder} for a given block.
         */
        private void loot(Block block, BibliocraftWoodType woodType, Function<Block, LootTable.Builder> factory) {
            BlockLootTableProvider.WithConditionsBuilder<LootTable.Builder> builder = BlockLootTableProvider.wrapLootTable(factory.apply(block));
            if (!woodType.getNamespace().equals("minecraft")) {
                builder.addCondition(new ModLoadedCondition(woodType.getNamespace()));
            }
            add(block, builder);
        }
    }
    
    private class Recipes extends RecipeProvider {
        private final String modId;

        private Recipes(HolderLookup.Provider registries, RecipeOutput output, String modId) {
            super(registries, output);
            this.modId = modId;
        }

        @Override
        protected void buildRecipes() {
            for (BibliocraftWoodType woodType : WOOD_TYPES) {
                RecipeOutput output = this.output;
                if (!woodType.getNamespace().equals("minecraft")) {
                    output = output.withConditions(new ModLoadedCondition(woodType.getNamespace()));
                }
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
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "bookcase")));
                shapedRecipe(itemLookup, BCItems.FANCY_ARMOR_STAND.get(woodType), woodType, "fancy_armor_stands")
                        .pattern(" R ")
                        .pattern(" R ")
                        .pattern("SSS")
                        .define('S', slab)
                        .define('R', Tags.Items.RODS_WOODEN)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "fancy_armor_stand")));
                shapedRecipe(itemLookup, BCItems.FANCY_CLOCK.get(woodType), woodType, "fancy_clock")
                        .pattern("SCS")
                        .pattern("SRS")
                        .pattern("SIS")
                        .define('S', slab)
                        .define('C', Items.CLOCK)
                        .define('R', Tags.Items.RODS_WOODEN)
                        .define('I', Tags.Items.INGOTS_COPPER)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "fancy_clock")));
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
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "fancy_crafter")));
                shapedRecipe(itemLookup, BCItems.FANCY_SIGN.get(woodType), woodType, "fancy_sign")
                        .pattern("P#P")
                        .pattern("P#P")
                        .pattern(" R ")
                        .define('P', planks)
                        .define('#', Items.PAPER)
                        .define('R', Tags.Items.RODS_WOODEN)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "fancy_sign")));
                shapelessRecipe(itemLookup, BCItems.GRANDFATHER_CLOCK.get(woodType), woodType, "grandfather_clock")
                        .requires(BCItems.FANCY_CLOCK.get(woodType))
                        .requires(BCItems.FANCY_CLOCK.get(woodType))
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "grandfather_clock")));
                shapedRecipe(itemLookup, BCItems.LABEL.get(woodType), woodType, "labels")
                        .pattern("SSS")
                        .pattern("SSS")
                        .define('S', slab)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "label")));
                shapedRecipe(itemLookup, BCItems.POTION_SHELF.get(woodType), woodType, "potion_shelves")
                        .pattern("SSS")
                        .pattern("P#P")
                        .pattern("SSS")
                        .define('P', planks)
                        .define('S', slab)
                        .define('#', Items.GLASS_BOTTLE)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "potion_shelf")));
                shapedRecipe(itemLookup, BCItems.SHELF.get(woodType), woodType, "shelves")
                        .pattern("SSS")
                        .pattern(" P ")
                        .pattern("SSS")
                        .define('P', planks)
                        .define('S', slab)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "shelf")));
                shapedRecipe(itemLookup, BCItems.TABLE.get(woodType), woodType, "tables")
                        .pattern("SSS")
                        .pattern(" P ")
                        .pattern(" P ")
                        .define('P', planks)
                        .define('S', slab)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "table")));
                shapedRecipe(itemLookup, BCItems.TOOL_RACK.get(woodType), woodType, "tool_racks")
                        .pattern("SSS")
                        .pattern("S#S")
                        .pattern("SSS")
                        .define('S', slab)
                        .define('#', Tags.Items.INGOTS_IRON)
                        .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "tool_rack")));
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
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "display_case")));
                    shapedRecipe(itemLookup, BCItems.SEAT.get(woodType, color), woodType, "seats")
                            .pattern(" W ")
                            .pattern(" S ")
                            .pattern("RSR")
                            .define('S', slab)
                            .define('R', stick)
                            .define('W', wool)
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "seat")));
                    shapedRecipe(itemLookup, BCItems.SMALL_SEAT_BACK.get(woodType, color), woodType, "small_seat_backs")
                            .pattern("W")
                            .pattern("S")
                            .define('S', slab)
                            .define('W', wool)
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "small_seat_back")));
                    shapedRecipe(itemLookup, BCItems.RAISED_SEAT_BACK.get(woodType, color), woodType, "raised_seat_backs")
                            .pattern(" W ")
                            .pattern(" S ")
                            .pattern("R R")
                            .define('S', slab)
                            .define('R', stick)
                            .define('W', wool)
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "raised_seat_back")));
                    shapedRecipe(itemLookup, BCItems.FLAT_SEAT_BACK.get(woodType, color), woodType, "flat_seat_backs")
                            .pattern("RWR")
                            .pattern("RSR")
                            .pattern("R R")
                            .define('S', slab)
                            .define('R', stick)
                            .define('W', wool)
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "flat_seat_back")));
                    shapedRecipe(itemLookup, BCItems.TALL_SEAT_BACK.get(woodType, color), woodType, "tall_seat_backs")
                            .pattern("S")
                            .pattern("#")
                            .define('S', slab)
                            .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "tall_seat_back")));
                    shapedRecipe(itemLookup, BCItems.FANCY_SEAT_BACK.get(woodType, color), woodType, "fancy_seat_backs")
                            .pattern("S#S")
                            .define('S', slab)
                            .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                            .save(output, ResourceKey.create(Registries.RECIPE, BCUtil.modLoc(modId, prefix + "fancy_seat_back")));
                }
            }
        }

        /**
         * Adds a shaped recipe for an item.
         *
         * @param item     The item.
         * @param woodType The {@link BibliocraftWoodType}.
         * @return A {@link ShapedRecipeBuilder} with the
         */
        private static ShapedRecipeBuilder shapedRecipe(HolderLookup.RegistryLookup<Item> lookup, Item item, BibliocraftWoodType woodType, String group) {
            return ShapedRecipeBuilder.shaped(lookup, RecipeCategory.DECORATIONS, item)
                    .group(BibliocraftApi.MOD_ID + ":" + group)
                    .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(lookup, woodType.family().get().getBaseBlock()).build()))));
        }

        /**
         * Adds a shapeless recipe for an item.
         *
         * @param item     The item.
         * @param woodType The {@link BibliocraftWoodType}.
         * @return A {@link ShapelessRecipeBuilder} with the
         */
        private static ShapelessRecipeBuilder shapelessRecipe(HolderLookup.RegistryLookup<Item> lookup, Item item, BibliocraftWoodType woodType, String group) {
            return ShapelessRecipeBuilder.shapeless(lookup, RecipeCategory.DECORATIONS, item)
                    .group(BibliocraftApi.MOD_ID + ":" + group)
                    .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(lookup, woodType.family().get().getBaseBlock()).build()))));
        }
    }

    private class RecipesRunner extends RecipeProvider.Runner {
        private final String modId;

        private RecipesRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modId) {
            super(packOutput, registries);
            this.modId = modId;
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new Recipes(registries, output, modId);
        }

        @Override
        public String getName() {
            return "Recipes" + NAME_SUFFIX;
        }
    }
    
    private void addBlockTags(Function<TagKey<Block>, TagAppender<Block, Block>> tag) {
        for (BibliocraftWoodType woodType : WOOD_TYPES) {
            BiConsumer<TagAppender<Block, Block>, Block> adder;
            BiConsumer<TagAppender<Block, Block>, Stream<Block>> streamAdder;
            if (woodType.getNamespace().equals("minecraft")) {
                adder = TagAppender::add;
                streamAdder = TagAppender::addAll;
            } else {
                adder = TagAppender::addOptional;
                streamAdder = (appender, s) -> s.forEach(appender::addOptional);
            }
            adder.accept(tag.apply(BCTags.Blocks.BOOKCASES), BCBlocks.BOOKCASE.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD), BCBlocks.FANCY_ARMOR_STAND.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_CLOCKS), BCBlocks.FANCY_CLOCK.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_CLOCKS), BCBlocks.WALL_FANCY_CLOCK.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_CRAFTERS), BCBlocks.FANCY_CRAFTER.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_SIGNS), BCBlocks.FANCY_SIGN.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.FANCY_SIGNS), BCBlocks.WALL_FANCY_SIGN.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.GRANDFATHER_CLOCKS), BCBlocks.GRANDFATHER_CLOCK.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.LABELS), BCBlocks.LABEL.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.POTION_SHELVES), BCBlocks.POTION_SHELF.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.SHELVES), BCBlocks.SHELF.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.TABLES), BCBlocks.TABLE.get(woodType));
            adder.accept(tag.apply(BCTags.Blocks.TOOL_RACKS), BCBlocks.TOOL_RACK.get(woodType));
            streamAdder.accept(tag.apply(BCTags.Blocks.DISPLAY_CASES), BCBlocks.DISPLAY_CASE.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Blocks.DISPLAY_CASES), BCBlocks.WALL_DISPLAY_CASE.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Blocks.SEATS), BCBlocks.SEAT.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Blocks.SEAT_BACKS), BCBlocks.SEAT_BACK.group(woodType).stream());
        }
    }

    private void addItemTags(Function<TagKey<Item>, TagAppender<Item, Item>> tag) {
        for (BibliocraftWoodType woodType : WOOD_TYPES) {
            BiConsumer<TagAppender<Item, Item>, Item> adder;
            BiConsumer<TagAppender<Item, Item>, Stream<Item>> streamAdder;
            if (woodType.getNamespace().equals("minecraft")) {
                adder = TagAppender::add;
                streamAdder = TagAppender::addAll;
            } else {
                adder = TagAppender::addOptional;
                streamAdder = (appender, s) -> s.forEach(appender::addOptional);
            }
            adder.accept(tag.apply(BCTags.Items.BOOKCASES), BCItems.BOOKCASE.get(woodType));
            adder.accept(tag.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD), BCItems.FANCY_ARMOR_STAND.get(woodType));
            adder.accept(tag.apply(BCTags.Items.FANCY_CLOCKS), BCItems.FANCY_CLOCK.get(woodType));
            adder.accept(tag.apply(BCTags.Items.FANCY_CRAFTERS), BCItems.FANCY_CRAFTER.get(woodType));
            adder.accept(tag.apply(BCTags.Items.FANCY_SIGNS), BCItems.FANCY_SIGN.get(woodType));
            adder.accept(tag.apply(BCTags.Items.GRANDFATHER_CLOCKS), BCItems.GRANDFATHER_CLOCK.get(woodType));
            adder.accept(tag.apply(BCTags.Items.LABELS), BCItems.LABEL.get(woodType));
            adder.accept(tag.apply(BCTags.Items.POTION_SHELVES), BCItems.POTION_SHELF.get(woodType));
            adder.accept(tag.apply(BCTags.Items.SHELVES), BCItems.SHELF.get(woodType));
            adder.accept(tag.apply(BCTags.Items.TABLES), BCItems.TABLE.get(woodType));
            adder.accept(tag.apply(BCTags.Items.TOOL_RACKS), BCItems.TOOL_RACK.get(woodType));
            streamAdder.accept(tag.apply(BCTags.Items.DISPLAY_CASES), BCItems.DISPLAY_CASE.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEATS), BCItems.SEAT.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEAT_BACKS_SMALL), BCItems.SMALL_SEAT_BACK.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEAT_BACKS_RAISED), BCItems.RAISED_SEAT_BACK.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEAT_BACKS_FLAT), BCItems.FLAT_SEAT_BACK.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEAT_BACKS_TALL), BCItems.TALL_SEAT_BACK.group(woodType).stream());
            streamAdder.accept(tag.apply(BCTags.Items.SEAT_BACKS_FANCY), BCItems.FANCY_SEAT_BACK.group(woodType).stream());
        }
    }

    private void addTranslations(LanguageProvider provider) {
        for (BibliocraftWoodType woodType : WOOD_TYPES) {
            woodenBlockTranslation(provider, woodType, BCBlocks.BOOKCASE, "Bookcase");
            woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
            woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_CLOCK, "Fancy Clock");
            woodenBlockTranslation(provider, woodType, BCBlocks.WALL_FANCY_CLOCK, "Fancy Clock");
            woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_CRAFTER, "Fancy Crafter");
            woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_SIGN, "Fancy Sign");
            woodenBlockTranslation(provider, woodType, BCBlocks.WALL_FANCY_SIGN, "Fancy Sign");
            woodenBlockTranslation(provider, woodType, BCBlocks.GRANDFATHER_CLOCK, "Grandfather Clock");
            woodenBlockTranslation(provider, woodType, BCBlocks.LABEL, "Label");
            woodenBlockTranslation(provider, woodType, BCBlocks.POTION_SHELF, "Potion Shelf");
            woodenBlockTranslation(provider, woodType, BCBlocks.SHELF, "Shelf");
            woodenBlockTranslation(provider, woodType, BCBlocks.TABLE, "Table");
            woodenBlockTranslation(provider, woodType, BCBlocks.TOOL_RACK, "Tool Rack");
            for (DyeColor color : DyeColor.values()) {
                coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.DISPLAY_CASE, "Display Case");
                coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.WALL_DISPLAY_CASE, "Display Case");
                coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.SEAT, "Seat");
                coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.SEAT_BACK, "Seat Back");
                coloredWoodenItemTranslation(provider, woodType, color, BCItems.SMALL_SEAT_BACK, "Small Seat Back");
                coloredWoodenItemTranslation(provider, woodType, color, BCItems.RAISED_SEAT_BACK, "Raised Seat Back");
                coloredWoodenItemTranslation(provider, woodType, color, BCItems.FLAT_SEAT_BACK, "Flat Seat Back");
                coloredWoodenItemTranslation(provider, woodType, color, BCItems.TALL_SEAT_BACK, "Tall Seat Back");
                coloredWoodenItemTranslation(provider, woodType, color, BCItems.FANCY_SEAT_BACK, "Fancy Seat Back");
            }
        }
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param holder   The {@link GroupedHolder.Nested} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void woodenBlockTranslation(LanguageProvider provider, BibliocraftWoodType woodType, GroupedHolder<BibliocraftWoodType, Block, ?> holder, String suffix) {
        provider.add(holder.get(woodType), DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param color    The {@link DyeColor} that is currently being processed.
     * @param holder   The {@link GroupedHolder.Nested} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void coloredWoodenBlockTranslation(LanguageProvider provider, BibliocraftWoodType woodType, DyeColor color, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, ?> holder, String suffix) {
        provider.add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param color    The {@link DyeColor} that is currently being processed.
     * @param holder   The {@link GroupedHolder.Nested} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void coloredWoodenItemTranslation(LanguageProvider provider, BibliocraftWoodType woodType, DyeColor color, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder, String suffix) {
        provider.add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }
}
