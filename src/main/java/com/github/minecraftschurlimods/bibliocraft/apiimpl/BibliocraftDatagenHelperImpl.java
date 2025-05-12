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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
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
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public final class BibliocraftDatagenHelperImpl implements BibliocraftDatagenHelper {
    private final List<BibliocraftWoodType> WOOD_TYPES = new ArrayList<>();

    @ApiStatus.Internal
    public BibliocraftDatagenHelperImpl() {}

    @Override
    public synchronized void addWoodTypeToGenerate(BibliocraftWoodType woodType) {
        WOOD_TYPES.add(woodType);
    }

    @Override
    public List<BibliocraftWoodType> getWoodTypesToGenerate() {
        return Collections.unmodifiableList(WOOD_TYPES);
    }

    @Override
    public void generateAllFor(BibliocraftWoodType woodType, String modId, GatherDataEvent event, LanguageProvider englishLanguageProvider, BlockTagsProvider blockTagsProvider, ItemTagsProvider itemTagsProvider) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generateEnglishTranslationsFor(englishLanguageProvider, woodType);
        generator.addProvider(event.includeClient(), new BlockStateProvider(output, BibliocraftApi.MOD_ID, existingFileHelper) {
            @Override
            protected void registerStatesAndModels() {
                generateBlockStatesFor(this, woodType);
            }

            @Override
            public String getName() {
                return super.getName() + " (Bibliocraft datagen helper for wood type " + woodType.id() + ")";
            }
        });
        generator.addProvider(event.includeClient(), new ItemModelProvider(output, BibliocraftApi.MOD_ID, existingFileHelper) {
            @Override
            protected void registerModels() {
                generateItemModelsFor(this, woodType);
            }

            @Override
            public String getName() {
                return super.getName() + " (Bibliocraft datagen helper for wood type " + woodType.id() + ")";
            }
        });
        generator.addProvider(event.includeServer(), new BlockLootTableProvider(output, lookupProvider) {
            @Override
            protected void generate() {
                generateLootTablesFor(this, woodType);
            }

            @Override
            public String getName() {
                return super.getName() + " (Bibliocraft datagen helper for wood type " + woodType.id() + ")";
            }
        });
        generator.addProvider(event.includeServer(), new RecipeProvider(output, lookupProvider) {
            @Override
            protected void buildRecipes(RecipeOutput output) {
                generateRecipesFor(output, woodType, modId);
            }

            @Override
            public String getName() {
                return super.getName() + " (Bibliocraft datagen helper for wood type " + woodType.id() + ")";
            }
        });
        generateBlockTagsFor(blockTagsProvider::tag, woodType);
        generateItemTagsFor(itemTagsProvider::tag, woodType);
    }

    @Override
    public void generateEnglishTranslationsFor(LanguageProvider provider, BibliocraftWoodType woodType) {
        woodenBlockTranslation(provider, woodType, BCBlocks.BOOKCASE,          "Bookcase");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_CLOCK,       "Fancy Clock");
        woodenBlockTranslation(provider, woodType, BCBlocks.WALL_FANCY_CLOCK,  "Fancy Clock");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_CRAFTER,     "Fancy Crafter");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_SIGN,        "Fancy Sign");
        woodenBlockTranslation(provider, woodType, BCBlocks.WALL_FANCY_SIGN,   "Fancy Sign");
        woodenBlockTranslation(provider, woodType, BCBlocks.GRANDFATHER_CLOCK, "Grandfather Clock");
        woodenBlockTranslation(provider, woodType, BCBlocks.LABEL,             "Label");
        woodenBlockTranslation(provider, woodType, BCBlocks.POTION_SHELF,      "Potion Shelf");
        woodenBlockTranslation(provider, woodType, BCBlocks.PRINTING_TABLE,    "Printing Table");
        woodenBlockTranslation(provider, woodType, BCBlocks.SHELF,             "Shelf");
        woodenBlockTranslation(provider, woodType, BCBlocks.TABLE,             "Table");
        woodenBlockTranslation(provider, woodType, BCBlocks.TOOL_RACK,         "Tool Rack");
        for (DyeColor color : DyeColor.values()) {
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.DISPLAY_CASE,      "Display Case");
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.WALL_DISPLAY_CASE, "Display Case");
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.SEAT,              "Seat");
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.SEAT_BACK,         "Seat Back");
            coloredWoodenItemTranslation(provider, woodType, color, BCItems.SMALL_SEAT_BACK,     "Small Seat Back");
            coloredWoodenItemTranslation(provider, woodType, color, BCItems.RAISED_SEAT_BACK,    "Raised Seat Back");
            coloredWoodenItemTranslation(provider, woodType, color, BCItems.FLAT_SEAT_BACK,      "Flat Seat Back");
            coloredWoodenItemTranslation(provider, woodType, color, BCItems.TALL_SEAT_BACK,      "Tall Seat Back");
            coloredWoodenItemTranslation(provider, woodType, color, BCItems.FANCY_SEAT_BACK,     "Fancy Seat Back");
        }
    }

    @Override
    public void generateBlockStatesFor(BlockStateProvider provider, BibliocraftWoodType woodType) {
        BlockModelProvider models = provider.models();
        ResourceLocation woodTexture = woodType.texture();
        String prefix = "block/wood/" + woodType.getRegistrationPrefix() + "/";
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.BOOKCASE.holder(woodType),
                prefix + "bookcase",
                bcLoc("block/template/bookcase/bookcase"),
                woodTexture);
        DatagenUtil.doubleHighHorizontalBlockModel(provider, BCBlocks.FANCY_ARMOR_STAND.holder(woodType),
                models.withExistingParent(prefix + "fancy_armor_stand_bottom", bcLoc("block/template/fancy_armor_stand/bottom")).texture("texture", woodTexture),
                models.withExistingParent(prefix + "fancy_armor_stand_top", bcLoc("block/template/fancy_armor_stand/top")).texture("texture", woodTexture),
                true);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.FANCY_CLOCK.holder(woodType),
                prefix + "fancy_clock",
                bcLoc("block/template/clock/fancy"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.WALL_FANCY_CLOCK.holder(woodType),
                prefix + "wall_fancy_clock",
                bcLoc("block/template/clock/wall_fancy"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.FANCY_CRAFTER.holder(woodType),
                prefix + "fancy_crafter",
                bcLoc("block/template/fancy_crafter"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.FANCY_SIGN.holder(woodType), state -> state.getValue(FancySignBlock.HANGING)
                ? models.withExistingParent(prefix + "fancy_sign_hanging", bcLoc("block/template/fancy_sign/hanging")).texture("texture", woodTexture)
                : models.withExistingParent(prefix + "fancy_sign", bcLoc("block/template/fancy_sign/standing")).texture("texture", woodTexture));
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.WALL_FANCY_SIGN.holder(woodType),
                prefix + "wall_fancy_sign",
                bcLoc("block/template/fancy_sign/wall"),
                woodTexture);
        DatagenUtil.doubleHighHorizontalBlockModel(provider, BCBlocks.GRANDFATHER_CLOCK.holder(woodType),
                models.withExistingParent(prefix + "grandfather_clock_bottom", bcLoc("block/template/clock/grandfather_bottom")).texture("texture", woodTexture),
                models.withExistingParent(prefix + "grandfather_clock_top", bcLoc("block/template/clock/grandfather_top")).texture("texture", woodTexture),
                true);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.LABEL.holder(woodType),
                prefix + "label",
                bcLoc("block/template/label"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.POTION_SHELF.holder(woodType),
                prefix + "potion_shelf",
                bcLoc("block/template/potion_shelf"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.PRINTING_TABLE.holder(woodType),
                prefix + "printing_table",
                bcLoc("block/template/printing_table"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.SHELF.holder(woodType),
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
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.TABLE.holder(woodType), state -> models.getExistingFile(provider.modLoc(prefix + "table")));
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.TOOL_RACK.holder(woodType),
                prefix + "tool_rack",
                bcLoc("block/template/tool_rack"),
                woodTexture);
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation wool = DatagenUtil.WOOL_TEXTURES.get(color);
            DeferredHolder<Block, ?> holder = BCBlocks.DISPLAY_CASE.holder(woodType, color);
            String colorPrefix = "block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/";
            final String finalColorPrefix = colorPrefix; // I love Java
            DatagenUtil.openClosedHorizontalBlockModel(provider, holder,
                    models.withExistingParent(colorPrefix + "display_case_open", bcLoc("block/template/display_case/open")).texture("texture", woodTexture).texture("color", wool),
                    models.withExistingParent(colorPrefix + "display_case_closed", bcLoc("block/template/display_case/closed")).texture("texture", woodTexture).texture("color", wool),
                    false);
            holder = BCBlocks.WALL_DISPLAY_CASE.holder(woodType, color);
            DatagenUtil.openClosedHorizontalBlockModel(provider, holder,
                    models.withExistingParent(colorPrefix + "wall_display_case_open", bcLoc("block/template/display_case/wall_open")).texture("texture", woodTexture).texture("color", wool),
                    models.withExistingParent(colorPrefix + "wall_display_case_closed", bcLoc("block/template/display_case/wall_closed")).texture("texture", woodTexture).texture("color", wool),
                    true);
            holder = BCBlocks.SEAT.holder(woodType, color);
            provider.getVariantBuilder(holder.get()).forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(models.withExistingParent(finalColorPrefix + "seat", bcLoc("block/template/seat/seat")).texture("texture", woodTexture).texture("color", DatagenUtil.WOOL_TEXTURES.get(color)))
                    .build());
            DatagenUtil.horizontalBlockModel(provider, BCBlocks.SEAT_BACK.holder(woodType, color), state -> {
                String suffix = state.getValue(SeatBackBlock.TYPE).getSerializedName() + "_seat_back";
                return models.withExistingParent(finalColorPrefix + suffix, BCUtil.bcLoc("block/template/seat/" + suffix)).texture("texture", woodTexture).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }, true);
        }
    }

    @Override
    public void generateItemModelsFor(ItemModelProvider provider, BibliocraftWoodType woodType) {
        String prefix = woodType.getRegistrationPrefix();
        provider.withExistingParent(prefix + "_bookcase",          bcLoc("block/wood/" + prefix + "/bookcase"));
        provider.withExistingParent(prefix + "_fancy_armor_stand", bcLoc("block/template/fancy_armor_stand/inventory")).texture("texture", woodType.texture());
        provider.withExistingParent(prefix + "_fancy_clock",       bcLoc("block/template/clock/fancy_inventory")).texture("texture", woodType.texture());
        provider.withExistingParent(prefix + "_fancy_crafter",     bcLoc("block/wood/" + prefix + "/fancy_crafter"));
        provider.withExistingParent(prefix + "_fancy_sign",        bcLoc("block/wood/" + prefix + "/fancy_sign"));
        provider.withExistingParent(prefix + "_grandfather_clock", bcLoc("block/template/clock/grandfather_inventory")).texture("texture", woodType.texture());
        provider.withExistingParent(prefix + "_label",             bcLoc("block/wood/" + prefix + "/label"));
        provider.withExistingParent(prefix + "_potion_shelf",      bcLoc("block/wood/" + prefix + "/potion_shelf"));
        provider.withExistingParent(prefix + "_printing_table",    bcLoc("block/wood/" + prefix + "/printing_table"));
        provider.withExistingParent(prefix + "_shelf",             bcLoc("block/wood/" + prefix + "/shelf"));
        provider.withExistingParent(prefix + "_table",             bcLoc("block/wood/" + prefix + "/table_inventory"));
        provider.withExistingParent(prefix + "_tool_rack",         bcLoc("block/wood/" + prefix + "/tool_rack"));
        for (DyeColor color : DyeColor.values()) {
            provider.withExistingParent(BCItems.DISPLAY_CASE.holder(woodType, color).getId().getPath(), bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/display_case_open"));
            provider.withExistingParent(BCItems.SEAT.holder(woodType, color).getId().getPath(),         bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/seat"));
            seatBackItemModel(provider, BCItems.SMALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.RAISED_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.FLAT_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.TALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.FANCY_SEAT_BACK.get(woodType, color));
        }
    }

    @Override
    public void generateBlockTagsFor(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAccessor, BibliocraftWoodType woodType) {
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
            tagAccessor.apply(BCTags.Blocks.PRINTING_TABLES_WOOD)   .add(BCBlocks.PRINTING_TABLE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.SHELVES)                .add(BCBlocks.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TABLES)                 .add(BCBlocks.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Blocks.TOOL_RACKS)             .add(BCBlocks.TOOL_RACK.get(woodType));
            DatagenUtil.addAll(BuiltInRegistries.BLOCK, BCBlocks.DISPLAY_CASE.element(woodType).values(),      tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAll(BuiltInRegistries.BLOCK, BCBlocks.WALL_DISPLAY_CASE.element(woodType).values(), tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAll(BuiltInRegistries.BLOCK, BCBlocks.SEAT.element(woodType).values(),              tagAccessor.apply(BCTags.Blocks.SEATS));
            DatagenUtil.addAll(BuiltInRegistries.BLOCK, BCBlocks.SEAT_BACK.element(woodType).values(),         tagAccessor.apply(BCTags.Blocks.SEAT_BACKS));
        } else {
            tagAccessor.apply(BCTags.Blocks.BOOKCASES)              .addOptional(BCBlocks.BOOKCASE.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).addOptional(BCBlocks.FANCY_ARMOR_STAND.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .addOptional(BCBlocks.FANCY_CLOCK.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CLOCKS)           .addOptional(BCBlocks.WALL_FANCY_CLOCK.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_CRAFTERS)         .addOptional(BCBlocks.FANCY_CRAFTER.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .addOptional(BCBlocks.FANCY_SIGN.id(woodType));
            tagAccessor.apply(BCTags.Blocks.FANCY_SIGNS)            .addOptional(BCBlocks.WALL_FANCY_SIGN.id(woodType));
            tagAccessor.apply(BCTags.Blocks.GRANDFATHER_CLOCKS)     .addOptional(BCBlocks.GRANDFATHER_CLOCK.id(woodType));
            tagAccessor.apply(BCTags.Blocks.LABELS)                 .addOptional(BCBlocks.LABEL.id(woodType));
            tagAccessor.apply(BCTags.Blocks.POTION_SHELVES)         .addOptional(BCBlocks.POTION_SHELF.id(woodType));
            tagAccessor.apply(BCTags.Blocks.PRINTING_TABLES_WOOD)   .addOptional(BCBlocks.PRINTING_TABLE.id(woodType));
            tagAccessor.apply(BCTags.Blocks.SHELVES)                .addOptional(BCBlocks.SHELF.id(woodType));
            tagAccessor.apply(BCTags.Blocks.TABLES)                 .addOptional(BCBlocks.TABLE.id(woodType));
            tagAccessor.apply(BCTags.Blocks.TOOL_RACKS)             .addOptional(BCBlocks.TOOL_RACK.id(woodType));
            DatagenUtil.addAllOptional(BuiltInRegistries.BLOCK, BCBlocks.DISPLAY_CASE.element(woodType).values(),      tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BuiltInRegistries.BLOCK, BCBlocks.WALL_DISPLAY_CASE.element(woodType).values(), tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BuiltInRegistries.BLOCK, BCBlocks.SEAT.element(woodType).values(),              tagAccessor.apply(BCTags.Blocks.SEATS));
            DatagenUtil.addAllOptional(BuiltInRegistries.BLOCK, BCBlocks.SEAT_BACK.element(woodType).values(),         tagAccessor.apply(BCTags.Blocks.SEAT_BACKS));
        }
    }

    @Override
    public void generateItemTagsFor(Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tagAccessor, BibliocraftWoodType woodType) {
        if (woodType.getNamespace().equals("minecraft")) {
            tagAccessor.apply(BCTags.Items.BOOKCASES)              .add(BCItems.BOOKCASE.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.FANCY_ARMOR_STAND.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CLOCKS)           .add(BCItems.FANCY_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CRAFTERS)         .add(BCItems.FANCY_CRAFTER.get(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_SIGNS)            .add(BCItems.FANCY_SIGN.get(woodType));
            tagAccessor.apply(BCTags.Items.GRANDFATHER_CLOCKS)     .add(BCItems.GRANDFATHER_CLOCK.get(woodType));
            tagAccessor.apply(BCTags.Items.LABELS)                 .add(BCItems.LABEL.get(woodType));
            tagAccessor.apply(BCTags.Items.POTION_SHELVES)         .add(BCItems.POTION_SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.PRINTING_TABLES_WOOD)   .add(BCItems.PRINTING_TABLE.get(woodType));
            tagAccessor.apply(BCTags.Items.SHELVES)                .add(BCItems.SHELF.get(woodType));
            tagAccessor.apply(BCTags.Items.TABLES)                 .add(BCItems.TABLE.get(woodType));
            tagAccessor.apply(BCTags.Items.TOOL_RACKS)             .add(BCItems.TOOL_RACK.get(woodType));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.DISPLAY_CASE.element(woodType).values(),     tagAccessor.apply(BCTags.Items.DISPLAY_CASES));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.SEAT.element(woodType).values(),             tagAccessor.apply(BCTags.Items.SEATS));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.SMALL_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_SMALL));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.RAISED_SEAT_BACK.element(woodType).values(), tagAccessor.apply(BCTags.Items.SEAT_BACKS_RAISED));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.FLAT_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_FLAT));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.TALL_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_TALL));
            DatagenUtil.addAll(BuiltInRegistries.ITEM, BCItems.FANCY_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_FANCY));
        } else {
            tagAccessor.apply(BCTags.Items.BOOKCASES)              .addOptional(BCItems.BOOKCASE.id(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).addOptional(BCItems.FANCY_ARMOR_STAND.id(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CLOCKS)           .addOptional(BCItems.FANCY_CLOCK.id(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_CRAFTERS)         .addOptional(BCItems.FANCY_CRAFTER.id(woodType));
            tagAccessor.apply(BCTags.Items.FANCY_SIGNS)            .addOptional(BCItems.FANCY_SIGN.id(woodType));
            tagAccessor.apply(BCTags.Items.GRANDFATHER_CLOCKS)     .addOptional(BCItems.GRANDFATHER_CLOCK.id(woodType));
            tagAccessor.apply(BCTags.Items.LABELS)                 .addOptional(BCItems.LABEL.id(woodType));
            tagAccessor.apply(BCTags.Items.POTION_SHELVES)         .addOptional(BCItems.POTION_SHELF.id(woodType));
            tagAccessor.apply(BCTags.Items.PRINTING_TABLES_WOOD)   .addOptional(BCItems.PRINTING_TABLE.id(woodType));
            tagAccessor.apply(BCTags.Items.SHELVES)                .addOptional(BCItems.SHELF.id(woodType));
            tagAccessor.apply(BCTags.Items.TABLES)                 .addOptional(BCItems.TABLE.id(woodType));
            tagAccessor.apply(BCTags.Items.TOOL_RACKS)             .addOptional(BCItems.TOOL_RACK.id(woodType));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.DISPLAY_CASE.element(woodType).values(),     tagAccessor.apply(BCTags.Items.DISPLAY_CASES));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.SEAT.element(woodType).values(),             tagAccessor.apply(BCTags.Items.SEATS));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.SMALL_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_SMALL));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.RAISED_SEAT_BACK.element(woodType).values(), tagAccessor.apply(BCTags.Items.SEAT_BACKS_RAISED));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.FLAT_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_FLAT));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.TALL_SEAT_BACK.element(woodType).values(),   tagAccessor.apply(BCTags.Items.SEAT_BACKS_TALL));
            DatagenUtil.addAllOptional(BuiltInRegistries.ITEM, BCItems.FANCY_SEAT_BACK.element(woodType).values(),  tagAccessor.apply(BCTags.Items.SEAT_BACKS_FANCY));
        }
    }

    @Override
    public void generateLootTablesFor(BlockLootTableProvider provider, BibliocraftWoodType woodType) {
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
        loot(provider, BCBlocks.PRINTING_TABLE.get(woodType),    woodType, DatagenUtil::createNameableTable);
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
    }

    @Override
    public void generateRecipesFor(RecipeOutput output, BibliocraftWoodType woodType, String modId) {
        if (!woodType.getNamespace().equals("minecraft")) {
            output = output.withConditions(new ModLoadedCondition(woodType.getNamespace()));
        }
        String prefix = "wood/" + woodType.getRegistrationPrefix() + "/";
        Block planks = woodType.family().get().getBaseBlock();
        Block slab = woodType.family().get().get(BlockFamily.Variant.SLAB);
        TagKey<Item> stick = Tags.Items.RODS_WOODEN;
        shapedRecipe(BCItems.BOOKCASE.get(woodType), woodType, "bookcases")
                .pattern("PSP")
                .pattern("PSP")
                .pattern("PSP")
                .define('P', planks)
                .define('S', slab)
                .save(output, BCUtil.modLoc(modId, prefix + "bookcase"));
        shapedRecipe(BCItems.FANCY_ARMOR_STAND.get(woodType), woodType, "fancy_armor_stands")
                .pattern(" R ")
                .pattern(" R ")
                .pattern("SSS")
                .define('S', slab)
                .define('R', Tags.Items.RODS_WOODEN)
                .save(output, BCUtil.modLoc(modId, prefix + "fancy_armor_stand"));
        shapedRecipe(BCItems.FANCY_CLOCK.get(woodType), woodType, "fancy_clock")
                .pattern("SCS")
                .pattern("SRS")
                .pattern("SIS")
                .define('S', slab)
                .define('C', Items.CLOCK)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('I', Tags.Items.INGOTS_COPPER)
                .save(output, BCUtil.modLoc(modId, prefix + "fancy_clock"));
        shapedRecipe(BCItems.FANCY_CRAFTER.get(woodType), woodType, "fancy_crafter")
                .pattern("ITF")
                .pattern("PGP")
                .pattern("PCP")
                .define('P', planks)
                .define('I', Tags.Items.DYES_BLACK)
                .define('T', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .define('F', Tags.Items.FEATHERS)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('C', Items.CRAFTER)
                .save(output, BCUtil.modLoc(modId, prefix + "fancy_crafter"));
        shapedRecipe(BCItems.FANCY_SIGN.get(woodType), woodType, "fancy_sign")
                .pattern("P#P")
                .pattern("P#P")
                .pattern(" R ")
                .define('P', planks)
                .define('#', Items.PAPER)
                .define('R', Tags.Items.RODS_WOODEN)
                .save(output, BCUtil.modLoc(modId, prefix + "fancy_sign"));
        shapelessRecipe(BCItems.GRANDFATHER_CLOCK.get(woodType), woodType, "grandfather_clock")
                .requires(BCItems.FANCY_CLOCK.get(woodType))
                .requires(BCItems.FANCY_CLOCK.get(woodType))
                .save(output, BCUtil.modLoc(modId, prefix + "grandfather_clock"));
        shapedRecipe(BCItems.LABEL.get(woodType), woodType, "labels")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', slab)
                .save(output, BCUtil.modLoc(modId, prefix + "label"));
        shapedRecipe(BCItems.POTION_SHELF.get(woodType), woodType, "potion_shelves")
                .pattern("SSS")
                .pattern("P#P")
                .pattern("SSS")
                .define('P', planks)
                .define('S', slab)
                .define('#', Items.GLASS_BOTTLE)
                .save(output, BCUtil.modLoc(modId, prefix + "potion_shelf"));
        shapedRecipe(BCItems.PRINTING_TABLE.get(woodType), woodType, "printing_tables")
                .pattern("CCC")
                .pattern("SSS")
                .pattern("PRP")
                .define('P', planks)
                .define('S', slab)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .save(output, BCUtil.modLoc(modId, prefix + "printing_table"));
        shapedRecipe(BCItems.SHELF.get(woodType), woodType, "shelves")
                .pattern("SSS")
                .pattern(" P ")
                .pattern("SSS")
                .define('P', planks)
                .define('S', slab)
                .save(output, BCUtil.modLoc(modId, prefix + "shelf"));
        shapedRecipe(BCItems.TABLE.get(woodType), woodType, "tables")
                .pattern("SSS")
                .pattern(" P ")
                .pattern(" P ")
                .define('P', planks)
                .define('S', slab)
                .save(output, BCUtil.modLoc(modId, prefix + "table"));
        shapedRecipe(BCItems.TOOL_RACK.get(woodType), woodType, "tool_racks")
                .pattern("SSS")
                .pattern("S#S")
                .pattern("SSS")
                .define('S', slab)
                .define('#', Tags.Items.INGOTS_IRON)
                .save(output, BCUtil.modLoc(modId, prefix + "tool_rack"));
        for (DyeColor color : DyeColor.values()) {
            Item wool = BuiltInRegistries.ITEM.get(BCUtil.mcLoc(color.getName() + "_wool"));
            prefix = "color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/";
            shapedRecipe(BCItems.DISPLAY_CASE.get(woodType, color), woodType, "display_cases")
                    .pattern("SGS")
                    .pattern("SWS")
                    .pattern("SSS")
                    .define('S', slab)
                    .define('W', wool)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .save(output, BCUtil.modLoc(modId, prefix + "display_case"));
            shapedRecipe(BCItems.SEAT.get(woodType, color), woodType, "seats")
                    .pattern(" W ")
                    .pattern(" S ")
                    .pattern("RSR")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, BCUtil.modLoc(modId, prefix + "seat"));
            shapedRecipe(BCItems.SMALL_SEAT_BACK.get(woodType, color), woodType, "small_seat_backs")
                    .pattern("W")
                    .pattern("S")
                    .define('S', slab)
                    .define('W', wool)
                    .save(output, BCUtil.modLoc(modId, prefix + "small_seat_back"));
            shapedRecipe(BCItems.RAISED_SEAT_BACK.get(woodType, color), woodType, "raised_seat_backs")
                    .pattern(" W ")
                    .pattern(" S ")
                    .pattern("R R")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, BCUtil.modLoc(modId, prefix + "raised_seat_back"));
            shapedRecipe(BCItems.FLAT_SEAT_BACK.get(woodType, color), woodType, "flat_seat_backs")
                    .pattern("RWR")
                    .pattern("RSR")
                    .pattern("R R")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, BCUtil.modLoc(modId, prefix + "flat_seat_back"));
            shapedRecipe(BCItems.TALL_SEAT_BACK.get(woodType, color), woodType, "tall_seat_backs")
                    .pattern("S")
                    .pattern("#")
                    .define('S', slab)
                    .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                    .save(output, BCUtil.modLoc(modId, prefix + "tall_seat_back"));
            shapedRecipe(BCItems.FANCY_SEAT_BACK.get(woodType, color), woodType, "fancy_seat_backs")
                    .pattern("S#S")
                    .define('S', slab)
                    .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                    .save(output, BCUtil.modLoc(modId, prefix + "fancy_seat_back"));
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
    private static ShapedRecipeBuilder shapedRecipe(Item item, BibliocraftWoodType woodType, String group) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item)
                .group(BibliocraftApi.MOD_ID + ":" + group)
                .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(woodType.family().get().getBaseBlock()).build()))));
    }

    /**
     * Adds a shapeless recipe for an item.
     *
     * @param item     The item.
     * @param woodType The {@link BibliocraftWoodType}.
     * @return A {@link ShapelessRecipeBuilder} with the
     */
    private static ShapelessRecipeBuilder shapelessRecipe(Item item, BibliocraftWoodType woodType, String group) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item)
                .group(BibliocraftApi.MOD_ID + ":" + group)
                .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(woodType.family().get().getBaseBlock()).build()))));
    }
}
