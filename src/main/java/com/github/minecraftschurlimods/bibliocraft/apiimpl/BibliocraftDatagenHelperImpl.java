package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.BlockLootTableProvider;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableModel;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.holder.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.holder.WoodTypeDeferredHolder;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public final class BibliocraftDatagenHelperImpl implements BibliocraftDatagenHelper {
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
    public void generateAllFor(BibliocraftWoodType woodType, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator.PackGenerator clientPack, DataGenerator.PackGenerator serverPack, LanguageProvider englishLanguageProvider, BlockTagsProvider blockTagsProvider, ItemTagsProvider itemTagsProvider) {
        String nameSuffix = " (Bibliocraft datagen helper for wood type " + woodType.id() + ")";
        generateEnglishTranslationsFor(englishLanguageProvider, woodType);
        clientPack.addProvider(output -> new ModelProvider(output, BibliocraftApi.MOD_ID) {
            @Override
            protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
                generateBlockStatesFor(blockModels, woodType);
                generateItemModelsFor(itemModels, woodType);
            }

            @Override
            public String getName() {
                return super.getName() + nameSuffix;
            }
        });
        serverPack.addProvider(output -> new BlockLootTableProvider(output, lookupProvider) {
            @Override
            protected void generate() {
                generateLootTablesFor(this, woodType);
            }

            @Override
            public String getName() {
                return super.getName() + nameSuffix;
            }
        });
        serverPack.addProvider(output -> new RecipeProvider.Runner(output, lookupProvider) {
            @Override
            protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
                return new RecipeProvider(registries, output) {
                    @Override
                    protected void buildRecipes() {
                        generateRecipesFor(output, registries, woodType, modId);
                    }
                };
            }

            @Override
            public String getName() {
                return "Recipes" + nameSuffix;
            }
        });
        generateBlockTagsFor(blockTagsProvider::tag, woodType);
        generateItemTagsFor(itemTagsProvider::tag, woodType);
    }

    @Override
    public void generateEnglishTranslationsFor(LanguageProvider provider, BibliocraftWoodType woodType) {
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

    @Override
    public void generateBlockStatesFor(BlockModelGenerators generators, BibliocraftWoodType woodType) {
        ResourceLocation woodTexture = woodType.texture();
        String prefix = "block/wood/" + woodType.getRegistrationPrefix() + "/";
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.BOOKCASE.holder(woodType),
                prefix + "bookcase",
                bcLoc("block/template/bookcase/bookcase"),
                woodTexture);
        DatagenUtil.doubleHighHorizontalBlockModel(generators, BCBlocks.FANCY_ARMOR_STAND.holder(woodType),
                models.withExistingParent(prefix + "fancy_armor_stand_bottom", bcLoc("block/template/fancy_armor_stand/bottom")).texture("texture", woodTexture),
                models.withExistingParent(prefix + "fancy_armor_stand_top", bcLoc("block/template/fancy_armor_stand/top")).texture("texture", woodTexture),
                true);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.FANCY_CLOCK.holder(woodType),
                prefix + "fancy_clock",
                bcLoc("block/template/clock/fancy"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.WALL_FANCY_CLOCK.holder(woodType),
                prefix + "wall_fancy_clock",
                bcLoc("block/template/clock/wall_fancy"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.FANCY_CRAFTER.holder(woodType),
                prefix + "fancy_crafter",
                bcLoc("block/template/fancy_crafter"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.FANCY_SIGN.holder(woodType), state -> state.getValue(FancySignBlock.HANGING)
                ? models.withExistingParent(prefix + "fancy_sign_hanging", bcLoc("block/template/fancy_sign/hanging")).texture("texture", woodTexture)
                : models.withExistingParent(prefix + "fancy_sign", bcLoc("block/template/fancy_sign/standing")).texture("texture", woodTexture));
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.WALL_FANCY_SIGN.holder(woodType),
                prefix + "wall_fancy_sign",
                bcLoc("block/template/fancy_sign/wall"),
                woodTexture);
        DatagenUtil.doubleHighHorizontalBlockModel(generators, BCBlocks.GRANDFATHER_CLOCK.holder(woodType),
                models.withExistingParent(prefix + "grandfather_clock_bottom", bcLoc("block/template/clock/grandfather_bottom")).texture("texture", woodTexture),
                models.withExistingParent(prefix + "grandfather_clock_top", bcLoc("block/template/clock/grandfather_top")).texture("texture", woodTexture),
                true);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.LABEL.holder(woodType),
                prefix + "label",
                bcLoc("block/template/label"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.POTION_SHELF.holder(woodType),
                prefix + "potion_shelf",
                bcLoc("block/template/potion_shelf"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.SHELF.holder(woodType),
                prefix + "shelf",
                bcLoc("block/template/shelf"),
                woodTexture);
        TableModel.Builder tableBuilder = models.getBuilder(prefix + "table").customLoader(TableModel.Builder::new).withParticle(woodTexture);
        for (TableBlock.Type type : TableBlock.Type.values()) {
            String name = type.getSerializedName();
            JsonObject model = new JsonObject();
            model.addProperty("parent", bcLoc("block/template/table/" + name).toString());
            JsonObject textures = new JsonObject();
            textures.addProperty("texture", woodTexture.toString());
            model.add("textures", textures);
            tableBuilder.withModelForType(type, model);
        }
        models.withExistingParent(prefix + "table_inventory", bcLoc("block/template/table/none")).texture("texture", woodTexture);
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.TABLE.holder(woodType), state -> models.getExistingFile(generators.modLoc(prefix + "table")));
        DatagenUtil.horizontalBlockModel(generators, BCBlocks.TOOL_RACK.holder(woodType),
                prefix + "tool_rack",
                bcLoc("block/template/tool_rack"),
                woodTexture);
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation wool = DatagenUtil.WOOL_TEXTURES.get(color);
            DeferredHolder<Block, ?> holder = BCBlocks.DISPLAY_CASE.holder(woodType, color);
            String colorPrefix = "block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/";
            final String finalColorPrefix = colorPrefix; // I love Java
            DatagenUtil.openClosedHorizontalBlockModel(generators, holder,
                    models.withExistingParent(colorPrefix + "display_case_open", bcLoc("block/template/display_case/open")).texture("texture", woodTexture).texture("color", wool),
                    models.withExistingParent(colorPrefix + "display_case_closed", bcLoc("block/template/display_case/closed")).texture("texture", woodTexture).texture("color", wool),
                    false);
            holder = BCBlocks.WALL_DISPLAY_CASE.holder(woodType, color);
            DatagenUtil.openClosedHorizontalBlockModel(generators, holder,
                    models.withExistingParent(colorPrefix + "wall_display_case_open", bcLoc("block/template/display_case/wall_open")).texture("texture", woodTexture).texture("color", wool),
                    models.withExistingParent(colorPrefix + "wall_display_case_closed", bcLoc("block/template/display_case/wall_closed")).texture("texture", woodTexture).texture("color", wool),
                    true);
            holder = BCBlocks.SEAT.holder(woodType, color);
            generators.getVariantBuilder(holder.get()).forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(models.withExistingParent(finalColorPrefix + "seat", bcLoc("block/template/seat/seat")).texture("texture", woodTexture).texture("color", DatagenUtil.WOOL_TEXTURES.get(color)))
                    .build());
            DatagenUtil.horizontalBlockModel(generators, BCBlocks.SEAT_BACK.holder(woodType, color), state -> {
                String suffix = state.getValue(SeatBackBlock.TYPE).getSerializedName() + "_seat_back";
                return models.withExistingParent(finalColorPrefix + suffix, BCUtil.bcLoc("block/template/seat/" + suffix)).texture("texture", woodTexture).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }, true);
        }
    }

    @Override
    public void generateItemModelsFor(ItemModelGenerators generators, BibliocraftWoodType woodType) {
        // @formatter:off
        String prefix = woodType.getRegistrationPrefix();
        generators.withExistingParent(prefix + "_bookcase",          bcLoc("block/wood/" + prefix + "/bookcase"));
        generators.withExistingParent(prefix + "_fancy_armor_stand", bcLoc("block/template/fancy_armor_stand/inventory")).texture("texture", woodType.texture());
        generators.withExistingParent(prefix + "_fancy_clock",       bcLoc("block/template/clock/fancy_inventory")).texture("texture", woodType.texture());
        generators.withExistingParent(prefix + "_fancy_crafter",     bcLoc("block/wood/" + prefix + "/fancy_crafter"));
        generators.withExistingParent(prefix + "_fancy_sign",        bcLoc("block/wood/" + prefix + "/fancy_sign"));
        generators.withExistingParent(prefix + "_grandfather_clock", bcLoc("block/template/clock/grandfather_inventory")).texture("texture", woodType.texture());
        generators.withExistingParent(prefix + "_label",             bcLoc("block/wood/" + prefix + "/label"));
        generators.withExistingParent(prefix + "_potion_shelf",      bcLoc("block/wood/" + prefix + "/potion_shelf"));
        generators.withExistingParent(prefix + "_shelf",             bcLoc("block/wood/" + prefix + "/shelf"));
        generators.withExistingParent(prefix + "_table",             bcLoc("block/wood/" + prefix + "/table_inventory"));
        generators.withExistingParent(prefix + "_tool_rack",         bcLoc("block/wood/" + prefix + "/tool_rack"));
        for (DyeColor color : DyeColor.values()) {
            generators.withExistingParent(BCItems.DISPLAY_CASE.holder(woodType, color).getId().getPath(), bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/display_case_open"));
            generators.withExistingParent(BCItems.SEAT.holder(woodType, color).getId().getPath(), bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/seat"));
            seatBackItemModel(generators, BCItems.SMALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(generators, BCItems.RAISED_SEAT_BACK.get(woodType, color));
            seatBackItemModel(generators, BCItems.FLAT_SEAT_BACK.get(woodType, color));
            seatBackItemModel(generators, BCItems.TALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(generators, BCItems.FANCY_SEAT_BACK.get(woodType, color));
        }
        // @formatter:on
    }

    @Override
    public void generateBlockTagsFor(Function<TagKey<Block>, TagAppender<Block, Block>> tagAccessor, BibliocraftWoodType woodType) {
        // @formatter:off
        if (woodType.getNamespace().equals("minecraft")) {
            tagAccessor.apply(BCTags.Blocks.BOOKCASES)              .add(BCBlocks.BOOKCASE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.FANCY_ARMOR_STAND.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .add(BCBlocks.FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .add(BCBlocks.WALL_FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CRAFTERS)         .add(BCBlocks.FANCY_CRAFTER.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .add(BCBlocks.FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .add(BCBlocks.WALL_FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Blocks.GRANDFATHER_CLOCKS)     .add(BCBlocks.GRANDFATHER_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.LABELS)                 .add(BCBlocks.LABEL.get(woodType));
            tagAccessor.apply(BCTags.Blocks.POTION_SHELVES)         .add(BCBlocks.POTION_SHELF.get(woodType));
            tagAccessor.apply(BCTags.Blocks.SHELVES)                .add(BCBlocks.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TABLES)                 .add(BCBlocks.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TOOL_RACKS)             .add(BCBlocks.TOOL_RACK.get(woodType));
            DatagenUtil.addAll(BCBlocks.DISPLAY_CASE.element(woodType).values(),      tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAll(BCBlocks.WALL_DISPLAY_CASE.element(woodType).values(), tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAll(BCBlocks.SEAT.element(woodType).values(),              tagAccessor.apply(BCTags.Blocks.SEATS));
            DatagenUtil.addAll(BCBlocks.SEAT_BACK.element(woodType).values(),         tagAccessor.apply(BCTags.Blocks.SEAT_BACKS));
        } else {
            tagAccessor.apply(BCTags.Blocks.BOOKCASES)              .addOptional(BCBlocks.BOOKCASE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).addOptional(BCBlocks.FANCY_ARMOR_STAND.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .addOptional(BCBlocks.FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .addOptional(BCBlocks.WALL_FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CRAFTERS)         .addOptional(BCBlocks.FANCY_CRAFTER.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .addOptional(BCBlocks.FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .addOptional(BCBlocks.WALL_FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Blocks.GRANDFATHER_CLOCKS)     .addOptional(BCBlocks.GRANDFATHER_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Blocks.LABELS)                 .addOptional(BCBlocks.LABEL.get(woodType));
            tagAccessor.apply(BCTags.Blocks.POTION_SHELVES)         .addOptional(BCBlocks.POTION_SHELF.get(woodType));
            tagAccessor.apply(BCTags.Blocks.SHELVES)                .addOptional(BCBlocks.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TABLES)                 .addOptional(BCBlocks.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TOOL_RACKS)             .addOptional(BCBlocks.TOOL_RACK.get(woodType));
            DatagenUtil.addAllOptional(BCBlocks.DISPLAY_CASE.element(woodType).values(),      tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BCBlocks.WALL_DISPLAY_CASE.element(woodType).values(), tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BCBlocks.SEAT.element(woodType).values(),              tagAccessor.apply(BCTags.Blocks.SEATS));
            DatagenUtil.addAllOptional(BCBlocks.SEAT_BACK.element(woodType).values(),         tagAccessor.apply(BCTags.Blocks.SEAT_BACKS));
        }
        // @formatter:on
    }

    @Override
    public void generateItemTagsFor(Function<TagKey<Item>, TagAppender<Item, Item>> tagAccessor, BibliocraftWoodType woodType) {
        // @formatter:off
        if (woodType.getNamespace().equals("minecraft")) {
            tagAccessor.apply(BCTags.Items.BOOKCASES)              .add(BCItems.BOOKCASE.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.FANCY_ARMOR_STAND.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CLOCKS)           .add(BCItems.FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CRAFTERS)         .add(BCItems.FANCY_CRAFTER.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_SIGNS)            .add(BCItems.FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Items.GRANDFATHER_CLOCKS)     .add(BCItems.GRANDFATHER_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.LABELS)                 .add(BCItems.LABEL.get(woodType));
            tagAccessor.apply(BCTags.Items.POTION_SHELVES)         .add(BCItems.POTION_SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.SHELVES)                .add(BCItems.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.TABLES)                 .add(BCItems.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Items.TOOL_RACKS)             .add(BCItems.TOOL_RACK.get(woodType));
            DatagenUtil.addAll(BCItems.DISPLAY_CASE.element(woodType).values(),     tagAccessor.apply(BCTags.Items.DISPLAY_CASES));
            DatagenUtil.addAll(BCItems.SEAT.element(woodType).values(),             tagAccessor.apply(BCTags.Items.SEATS));
            DatagenUtil.addAll(BCItems.SMALL_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_SMALL));
            DatagenUtil.addAll(BCItems.RAISED_SEAT_BACK.element(woodType).values(), tagAccessor.apply(BCTags.Items.SEAT_BACKS_RAISED));
            DatagenUtil.addAll(BCItems.FLAT_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_FLAT));
            DatagenUtil.addAll(BCItems.TALL_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_TALL));
            DatagenUtil.addAll(BCItems.FANCY_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_FANCY));
        } else {
            tagAccessor.apply(BCTags.Items.BOOKCASES)              .addOptional(BCItems.BOOKCASE.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).addOptional(BCItems.FANCY_ARMOR_STAND.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CLOCKS)           .addOptional(BCItems.FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CRAFTERS)         .addOptional(BCItems.FANCY_CRAFTER.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_SIGNS)            .addOptional(BCItems.FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Items.GRANDFATHER_CLOCKS)     .addOptional(BCItems.GRANDFATHER_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.LABELS)                 .addOptional(BCItems.LABEL.get(woodType));
            tagAccessor.apply(BCTags.Items.POTION_SHELVES)         .addOptional(BCItems.POTION_SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.SHELVES)                .addOptional(BCItems.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.TABLES)                 .addOptional(BCItems.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Items.TOOL_RACKS)             .addOptional(BCItems.TOOL_RACK.get(woodType));
            DatagenUtil.addAllOptional(BCItems.DISPLAY_CASE.element(woodType).values(),     tagAccessor.apply(BCTags.Items.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BCItems.SEAT.element(woodType).values(),             tagAccessor.apply(BCTags.Items.SEATS));
            DatagenUtil.addAllOptional(BCItems.SMALL_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_SMALL));
            DatagenUtil.addAllOptional(BCItems.RAISED_SEAT_BACK.element(woodType).values(), tagAccessor.apply(BCTags.Items.SEAT_BACKS_RAISED));
            DatagenUtil.addAllOptional(BCItems.FLAT_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_FLAT));
            DatagenUtil.addAllOptional(BCItems.TALL_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_TALL));
            DatagenUtil.addAllOptional(BCItems.FANCY_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_FANCY));
        }
        // @formatter:on
    }

    @Override
    public void generateLootTablesFor(BlockLootTableProvider provider, BibliocraftWoodType woodType) {
        // @formatter:off
        loot(provider, BCBlocks.BOOKCASE.get(woodType),          woodType, DatagenUtil::createNameableTable);
        loot(provider, BCBlocks.FANCY_ARMOR_STAND.get(woodType), woodType, DatagenUtil::createFancyArmorStandTable);
        loot(provider, BCBlocks.FANCY_CLOCK.get(woodType),       woodType, DatagenUtil::createDefaultTable);
        loot(provider, BCBlocks.WALL_FANCY_CLOCK.get(woodType),  woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_CLOCK.get(woodType)));
        loot(provider, BCBlocks.FANCY_CRAFTER.get(woodType),     woodType, DatagenUtil::createNameableTable);
        loot(provider, BCBlocks.FANCY_SIGN.get(woodType),        woodType, DatagenUtil::createDefaultTable);
        loot(provider, BCBlocks.WALL_FANCY_SIGN.get(woodType),   woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_SIGN.get(woodType)));
        loot(provider, BCBlocks.GRANDFATHER_CLOCK.get(woodType), woodType, DatagenUtil::createGrandfatherClockTable);
        loot(provider, BCBlocks.LABEL.get(woodType),             woodType, DatagenUtil::createNameableTable);
        loot(provider, BCBlocks.POTION_SHELF.get(woodType),      woodType, DatagenUtil::createNameableTable);
        loot(provider, BCBlocks.SHELF.get(woodType),             woodType, DatagenUtil::createNameableTable);
        loot(provider, BCBlocks.TABLE.get(woodType),             woodType, DatagenUtil::createDefaultTable);
        loot(provider, BCBlocks.TOOL_RACK.get(woodType),         woodType, DatagenUtil::createNameableTable);
        for (DyeColor color : DyeColor.values()) {
            loot(provider, BCBlocks.DISPLAY_CASE.get(woodType, color),      woodType, DatagenUtil::createDefaultTable);
            loot(provider, BCBlocks.WALL_DISPLAY_CASE.get(woodType, color), woodType, block -> DatagenUtil.createDefaultTable(BCBlocks.DISPLAY_CASE.get(woodType, color)));
            loot(provider, BCBlocks.SEAT.get(woodType, color),              woodType, DatagenUtil::createDefaultTable);
            loot(provider, BCBlocks.SEAT_BACK.get(woodType, color), woodType, block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                    .add(LootItem.lootTableItem(BCItems.SMALL_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.SMALL))))
                    .add(LootItem.lootTableItem(BCItems.RAISED_SEAT_BACK.get(woodType, color)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.RAISED))))
                    .add(LootItem.lootTableItem(BCItems.FLAT_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FLAT))))
                    .add(LootItem.lootTableItem(BCItems.TALL_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.TALL))))
                    .add(LootItem.lootTableItem(BCItems.FANCY_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FANCY))))));
        }
        // @formatter:on
    }

    @Override
    public void generateRecipesFor(RecipeOutput output, HolderLookup.Provider registries, BibliocraftWoodType woodType, String modId) {
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

    /**
     * @param path The path of the {@link ResourceLocation}.
     * @return A new {@link ResourceLocation} with Bibliocraft's namespace and the given path.
     */
    private static ResourceLocation bcLoc(String path) {
        return BCUtil.bcLoc(path);
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param holder   The {@link WoodTypeDeferredHolder} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void woodenBlockTranslation(LanguageProvider provider, BibliocraftWoodType woodType, WoodTypeDeferredHolder<Block, ?> holder, String suffix) {
        provider.add(holder.get(woodType), DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param color    The {@link DyeColor} that is currently being processed.
     * @param holder   The {@link WoodTypeDeferredHolder} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void coloredWoodenBlockTranslation(LanguageProvider provider, BibliocraftWoodType woodType, DyeColor color, ColoredWoodTypeDeferredHolder<Block, ?> holder, String suffix) {
        provider.add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Adds an English (en_us) translation to the given {@link LanguageProvider}.
     *
     * @param provider The {@link LanguageProvider} to add the translation to.
     * @param woodType The {@link BibliocraftWoodType} that is currently being processed.
     * @param color    The {@link DyeColor} that is currently being processed.
     * @param holder   The {@link WoodTypeDeferredHolder} to add the translation for.
     * @param suffix   The suffix of the translation.
     */
    private static void coloredWoodenItemTranslation(LanguageProvider provider, BibliocraftWoodType woodType, DyeColor color, ColoredWoodTypeDeferredHolder<Item, ?> holder, String suffix) {
        provider.add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Generates an item with a seat back model.
     *
     * @param provider Your mod's {@link ItemModelProvider}.
     * @param item     The item to generate the models for.
     */
    private static void seatBackItemModel(ItemModelProvider provider, SeatBackItem item) {
        provider.withExistingParent(item.getColor().getName() + "_" + item.getWoodType().getRegistrationPrefix() + "_" + item.type.getSerializedName() + "_seat_back", bcLoc("block/color/" + item.getColor().getName() + "/wood/" + item.getWoodType().getRegistrationPrefix() + "/" + item.type.getSerializedName() + "_seat_back"));
    }

    /**
     * Adds a loot table for a block.
     *
     * @param provider The loot table provider.
     * @param block    The block.
     * @param factory  A function that returns a {@link LootTable.Builder} for a given block.
     */
    private static void loot(BlockLootTableProvider provider, Block block, BibliocraftWoodType woodType, Function<Block, LootTable.Builder> factory) {
        BlockLootTableProvider.WithConditionsBuilder<LootTable.Builder> builder = BlockLootTableProvider.wrapLootTable(factory.apply(block));
        if (!woodType.getNamespace().equals("minecraft")) {
            builder.addCondition(new ModLoadedCondition(woodType.getNamespace()));
        }
        provider.add(block, builder);
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
