package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableModel;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
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
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public final class BibliocraftDatagenHelperImpl implements BibliocraftDatagenHelper {
    private static final List<BibliocraftWoodType> WOOD_TYPES = new ArrayList<>();

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

    public void generateEnglishTranslationsFor(LanguageProvider provider, BibliocraftWoodType woodType) {
        woodenBlockTranslation(provider, woodType, BCBlocks.BOOKCASE,          "Bookcase");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
        woodenBlockTranslation(provider, woodType, BCBlocks.LABEL,             "Label");
        woodenBlockTranslation(provider, woodType, BCBlocks.POTION_SHELF,      "Potion Shelf");
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
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.LABEL.holder(woodType),
                prefix + "label",
                bcLoc("block/template/label"),
                woodTexture);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.POTION_SHELF.holder(woodType),
                prefix + "potion_shelf",
                bcLoc("block/template/potion_shelf"),
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
                return models.withExistingParent(finalColorPrefix + suffix, BCUtil.modLoc("block/template/seat/" + suffix)).texture("texture", woodTexture).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }, true);
        }
    }

    @Override
    public void generateItemModelsFor(ItemModelProvider provider, BibliocraftWoodType woodType) {
        String prefix = woodType.getRegistrationPrefix();
        provider.withExistingParent(prefix + "_bookcase", bcLoc("block/wood/" + prefix + "/bookcase"));
        provider.withExistingParent(prefix + "_fancy_armor_stand", bcLoc("block/template/fancy_armor_stand/inventory")).texture("texture", woodType.texture());
        provider.withExistingParent(prefix + "_label", bcLoc("block/wood/" + prefix + "/label"));
        provider.withExistingParent(prefix + "_potion_shelf", bcLoc("block/wood/" + prefix + "/potion_shelf"));
        provider.withExistingParent(prefix + "_shelf", bcLoc("block/wood/" + prefix + "/shelf"));
        provider.withExistingParent(prefix + "_table", bcLoc("block/wood/" + prefix + "/table_inventory"));
        provider.withExistingParent(prefix + "_tool_rack", bcLoc("block/wood/" + prefix + "/tool_rack"));
        for (DyeColor color : DyeColor.values()) {
            provider.withExistingParent(BCItems.DISPLAY_CASE.holder(woodType, color).getId().getPath(), bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/display_case_open"));
            provider.withExistingParent(BCItems.SEAT.holder(woodType, color).getId().getPath(), bcLoc("block/color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/seat"));
            seatBackItemModel(provider, BCItems.SMALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.RAISED_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.FLAT_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.TALL_SEAT_BACK.get(woodType, color));
            seatBackItemModel(provider, BCItems.FANCY_SEAT_BACK.get(woodType, color));
        }
    }

    @Override
    public void generateBlockTagsFor(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAccessor, BibliocraftWoodType woodType) {
        tagAccessor.apply(BCTags.Blocks.BOOKCASES)              .add(BCBlocks.BOOKCASE.get(woodType));
        tagAccessor.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.FANCY_ARMOR_STAND.get(woodType));
        tagAccessor.apply(BCTags.Blocks.LABELS)                 .add(BCBlocks.LABEL.get(woodType));
        tagAccessor.apply(BCTags.Blocks.POTION_SHELVES)         .add(BCBlocks.POTION_SHELF.get(woodType));
        tagAccessor.apply(BCTags.Blocks.SHELVES)                .add(BCBlocks.SHELF.get(woodType));
        tagAccessor.apply(BCTags.Blocks.TABLES)                 .add(BCBlocks.TABLE.get(woodType));
        tagAccessor.apply(BCTags.Blocks.TOOL_RACKS)             .add(BCBlocks.TOOL_RACK.get(woodType));
        DatagenUtil.addAll(BCBlocks.DISPLAY_CASE.element(woodType).values(),      tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
        DatagenUtil.addAll(BCBlocks.WALL_DISPLAY_CASE.element(woodType).values(), tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
        DatagenUtil.addAll(BCBlocks.SEAT.element(woodType).values(),              tagAccessor.apply(BCTags.Blocks.SEATS));
        DatagenUtil.addAll(BCBlocks.SEAT_BACK.element(woodType).values(),         tagAccessor.apply(BCTags.Blocks.SEAT_BACKS));
    }

    @Override
    public void generateItemTagsFor(Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tagAccessor, BibliocraftWoodType woodType) {
        tagAccessor.apply(BCTags.Items.BOOKCASES)              .add(BCItems.BOOKCASE.get(woodType));
        tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.FANCY_ARMOR_STAND.get(woodType));
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
    }

    @Override
    public void generateLootTablesFor(BiConsumer<Block, LootTable.Builder> lootTableAdder, BibliocraftWoodType woodType) {
        loot(lootTableAdder, BCBlocks.BOOKCASE.get(woodType),          DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.FANCY_ARMOR_STAND.get(woodType), DatagenUtil::createFancyArmorStandTable);
        loot(lootTableAdder, BCBlocks.LABEL.get(woodType),             DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.POTION_SHELF.get(woodType),      DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.SHELF.get(woodType),             DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.TABLE.get(woodType),             DatagenUtil::createDefaultTable);
        loot(lootTableAdder, BCBlocks.TOOL_RACK.get(woodType),         DatagenUtil::createNameableTable);
        for (DyeColor color : DyeColor.values()) {
            loot(lootTableAdder, BCBlocks.DISPLAY_CASE.get(woodType, color),      DatagenUtil::createDefaultTable);
            loot(lootTableAdder, BCBlocks.WALL_DISPLAY_CASE.get(woodType, color), DatagenUtil::createDefaultTable);
            loot(lootTableAdder, BCBlocks.SEAT.get(woodType, color),              DatagenUtil::createDefaultTable);
            loot(lootTableAdder, BCBlocks.SEAT_BACK.get(woodType, color), block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
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
        Block planks = woodType.family().getBaseBlock();
        Block slab = woodType.family().get(BlockFamily.Variant.SLAB);
        TagKey<Item> stick = Tags.Items.RODS_WOODEN;
        shapedRecipe(BCItems.BOOKCASE.get(woodType), woodType)
                .pattern("PSP")
                .pattern("PSP")
                .pattern("PSP")
                .define('P', planks)
                .define('S', slab)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "bookcase"));
        shapedRecipe(BCItems.FANCY_ARMOR_STAND.get(woodType), woodType)
                .pattern(" R ")
                .pattern(" R ")
                .pattern("SSS")
                .define('S', slab)
                .define('R', Tags.Items.RODS_WOODEN)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "fancy_armor_stand"));
        shapedRecipe(BCItems.LABEL.get(woodType), woodType)
                .pattern("SSS")
                .pattern("SSS")
                .define('S', slab)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "label"));
        shapedRecipe(BCItems.POTION_SHELF.get(woodType), woodType)
                .pattern("SSS")
                .pattern("P#P")
                .pattern("SSS")
                .define('P', planks)
                .define('S', slab)
                .define('#', Items.GLASS_BOTTLE)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "potion_shelf"));
        shapedRecipe(BCItems.SHELF.get(woodType), woodType)
                .pattern("SSS")
                .pattern(" P ")
                .pattern("SSS")
                .define('P', planks)
                .define('S', slab)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "shelf"));
        shapedRecipe(BCItems.TABLE.get(woodType), woodType)
                .pattern("SSS")
                .pattern(" P ")
                .pattern(" P ")
                .define('P', planks)
                .define('S', slab)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "table"));
        shapedRecipe(BCItems.TOOL_RACK.get(woodType), woodType)
                .pattern("SSS")
                .pattern("S#S")
                .pattern("SSS")
                .define('S', slab)
                .define('#', Tags.Items.INGOTS_IRON)
                .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "tool_rack"));
        for (DyeColor color : DyeColor.values()) {
            Item wool = BuiltInRegistries.ITEM.get(BCUtil.mcLoc(color.getName() + "_wool"));
            prefix = "color/" + color.getSerializedName() + "/wood/" + woodType.getRegistrationPrefix() + "/";
            shapedRecipe(BCItems.DISPLAY_CASE.get(woodType, color), woodType)
                    .pattern("SGS")
                    .pattern("SWS")
                    .pattern("SSS")
                    .define('S', slab)
                    .define('W', wool)
                    .define('G', Tags.Items.GLASS_BLOCKS)
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "display_case"));
            shapedRecipe(BCItems.SEAT.get(woodType, color), woodType)
                    .pattern(" W ")
                    .pattern(" S ")
                    .pattern("RSR")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "seat"));
            shapedRecipe(BCItems.SMALL_SEAT_BACK.get(woodType, color), woodType)
                    .pattern("W")
                    .pattern("S")
                    .define('S', slab)
                    .define('W', wool)
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "small_seat_back"));
            shapedRecipe(BCItems.RAISED_SEAT_BACK.get(woodType, color), woodType)
                    .pattern(" W ")
                    .pattern(" S ")
                    .pattern("R R")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "raised_seat_back"));
            shapedRecipe(BCItems.FLAT_SEAT_BACK.get(woodType, color), woodType)
                    .pattern("RWR")
                    .pattern("RSR")
                    .pattern("R R")
                    .define('S', slab)
                    .define('R', stick)
                    .define('W', wool)
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "flat_seat_back"));
            shapedRecipe(BCItems.TALL_SEAT_BACK.get(woodType, color), woodType)
                    .pattern("S")
                    .pattern("#")
                    .define('S', slab)
                    .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "tall_seat_back"));
            shapedRecipe(BCItems.FANCY_SEAT_BACK.get(woodType, color), woodType)
                    .pattern("S#S")
                    .define('S', slab)
                    .define('#', BCItems.FLAT_SEAT_BACK.get(woodType, color))
                    .save(output, ResourceLocation.fromNamespaceAndPath(modId, prefix + "fancy_seat_back"));
        }
    }

    /**
     * @param path The path of the {@link ResourceLocation}.
     * @return A new {@link ResourceLocation} with Bibliocraft's namespace and the given path.
     */
    private static ResourceLocation bcLoc(String path) {
        return BCUtil.modLoc(path);
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
     * @param lootTableAdder The loot table adder.
     * @param block          The block.
     * @param builder        A function that returns a {@link LootTable.Builder} for a given block.
     */
    private static void loot(BiConsumer<Block, LootTable.Builder> lootTableAdder, Block block, Function<Block, LootTable.Builder> builder) {
        lootTableAdder.accept(block, builder.apply(block));
    }

    /**
     * Adds a shaped recipe for an item.
     *
     * @param item     The item.
     * @param woodType The {@link BibliocraftWoodType}.
     * @return A {@link ShapedRecipeBuilder} with the
     */
    private static ShapedRecipeBuilder shapedRecipe(Item item, BibliocraftWoodType woodType) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item).unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(woodType.family().getBaseBlock()).build()))));
    }
}
