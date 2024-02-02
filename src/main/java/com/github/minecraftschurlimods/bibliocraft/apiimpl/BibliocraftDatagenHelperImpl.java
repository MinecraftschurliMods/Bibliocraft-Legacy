package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
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
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BibliocraftDatagenHelperImpl implements BibliocraftDatagenHelper {
    private static final BibliocraftDatagenHelperImpl INSTANCE = new BibliocraftDatagenHelperImpl();
    private static final List<BibliocraftWoodType> WOOD_TYPES = new ArrayList<>();

    private BibliocraftDatagenHelperImpl() {}

    /**
     * @return The only instance of this class.
     */
    public static BibliocraftDatagenHelperImpl get() {
        return INSTANCE;
    }

    @Override
    public synchronized void addWoodTypeToGenerate(BibliocraftWoodType woodType) {
        WOOD_TYPES.add(woodType);
    }

    @Override
    public List<BibliocraftWoodType> getWoodTypesToGenerate() {
        return Collections.unmodifiableList(WOOD_TYPES);
    }

    public void generateEnglishTranslationsFor(LanguageProvider provider, BibliocraftWoodType woodType) {
        woodenBlockTranslation(provider, woodType, BCBlocks.BOOKCASE, "Bookcase");
        woodenBlockTranslation(provider, woodType, BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
        woodenBlockTranslation(provider, woodType, BCBlocks.LABEL, "Label");
        woodenBlockTranslation(provider, woodType, BCBlocks.POTION_SHELF, "Potion Shelf");
        woodenBlockTranslation(provider, woodType, BCBlocks.SHELF, "Shelf");
        woodenBlockTranslation(provider, woodType, BCBlocks.TOOL_RACK, "Tool Rack");
        for (DyeColor color : DyeColor.values()) {
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.DISPLAY_CASE, "Display Case");
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.WALL_DISPLAY_CASE, "Display Case");
            coloredWoodenBlockTranslation(provider, woodType, color, BCBlocks.SEAT, "Seat");
        }
    }

    @Override
    public void generateBlockStatesFor(BlockStateProvider provider, BibliocraftWoodType woodType) {
        String prefix = woodType.getRegistrationPrefix();
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.BOOKCASE.holder(woodType),
                prefix + "_bookcase",
                bcLoc("block/template/bookcase/bookcase"),
                woodType.getTexture());
        DatagenUtil.doubleHighHorizontalBlockModel(provider, BCBlocks.FANCY_ARMOR_STAND.holder(woodType),
                provider.models().withExistingParent(prefix + "_fancy_armor_stand_bottom", bcLoc("block/template/fancy_armor_stand/bottom")).texture("texture", woodType.getTexture()),
                provider.models().withExistingParent(prefix + "_fancy_armor_stand_top", bcLoc("block/template/fancy_armor_stand/top")).texture("texture", woodType.getTexture()),
                true);
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.LABEL.holder(woodType),
                prefix + "_label",
                bcLoc("block/template/label"),
                woodType.getTexture());
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.POTION_SHELF.holder(woodType),
                prefix + "_potion_shelf",
                bcLoc("block/template/potion_shelf"),
                woodType.getTexture());
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.SHELF.holder(woodType),
                prefix + "_shelf",
                bcLoc("block/template/shelf"),
                woodType.getTexture());
        DatagenUtil.horizontalBlockModel(provider, BCBlocks.TOOL_RACK.holder(woodType),
                prefix + "_tool_rack",
                bcLoc("block/template/tool_rack"),
                woodType.getTexture());
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation wool = DatagenUtil.WOOL_TEXTURES.get(color);
            DeferredHolder<Block, ?> holder = BCBlocks.DISPLAY_CASE.holder(woodType, color);
            String path = holder.getId().getPath();
            DatagenUtil.openClosedHorizontalBlockModel(provider, holder,
                    provider.models().withExistingParent(path + "_open", bcLoc("block/template/display_case/open")).texture("texture", woodType.getTexture()).texture("color", wool),
                    provider.models().withExistingParent(path + "_closed", bcLoc("block/template/display_case/closed")).texture("texture", woodType.getTexture()).texture("color", wool),
                    false);
            holder = BCBlocks.WALL_DISPLAY_CASE.holder(woodType, color);
            path = holder.getId().getPath();
            DatagenUtil.openClosedHorizontalBlockModel(provider, holder,
                    provider.models().withExistingParent(path + "_open", bcLoc("block/template/display_case/wall_open")).texture("texture", woodType.getTexture()).texture("color", wool),
                    provider.models().withExistingParent(path + "_closed", bcLoc("block/template/display_case/wall_closed")).texture("texture", woodType.getTexture()).texture("color", wool),
                    true);
            holder = BCBlocks.SEAT.holder(woodType, color);
            path = holder.getId().getPath();
            final String finalPath = path; // I love Java
            provider.getVariantBuilder(holder.get()).forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(provider.models().withExistingParent(finalPath, bcLoc("block/template/seat/seat")).texture("texture", woodType.getTexture()).texture("color", DatagenUtil.WOOL_TEXTURES.get(color)))
                    .build());
        }
    }

    @Override
    public void generateItemModelsFor(ItemModelProvider provider, BibliocraftWoodType woodType) {
        String prefix = woodType.getRegistrationPrefix();
        withParentBlock(provider, prefix + "_bookcase");
        provider.withExistingParent(prefix + "_fancy_armor_stand", bcLoc("block/template/fancy_armor_stand/inventory")).texture("texture", woodType.getTexture());
        withParentBlock(provider, prefix + "_label");
        withParentBlock(provider, prefix + "_potion_shelf");
        withParentBlock(provider, prefix + "_shelf");
        withParentBlock(provider, prefix + "_tool_rack");
        for (DyeColor color : DyeColor.values()) {
            withParent(provider, BCItems.DISPLAY_CASE.holder(woodType, color).getId().getPath(), n -> bcLoc("block/" + n + "_open"));
            withParentBlock(provider, BCItems.SEAT.holder(woodType, color).getId().getPath());
        }
    }

    @Override
    public void generateBlockTagsFor(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAccessor, BibliocraftWoodType woodType) {
        tagAccessor.apply(BCTags.Blocks.BOOKCASES).add(BCBlocks.BOOKCASE.get(woodType));
        tagAccessor.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.FANCY_ARMOR_STAND.get(woodType));
        tagAccessor.apply(BCTags.Blocks.LABELS).add(BCBlocks.LABEL.get(woodType));
        tagAccessor.apply(BCTags.Blocks.POTION_SHELVES).add(BCBlocks.POTION_SHELF.get(woodType));
        tagAccessor.apply(BCTags.Blocks.SHELVES).add(BCBlocks.SHELF.get(woodType));
        tagAccessor.apply(BCTags.Blocks.TOOL_RACKS).add(BCBlocks.TOOL_RACK.get(woodType));
        DatagenUtil.addColorVariants(woodType, BCBlocks.DISPLAY_CASE, tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
        DatagenUtil.addColorVariants(woodType, BCBlocks.WALL_DISPLAY_CASE, tagAccessor.apply(BCTags.Blocks.DISPLAY_CASES));
        DatagenUtil.addColorVariants(woodType, BCBlocks.SEAT, tagAccessor.apply(BCTags.Blocks.SEATS));
    }

    @Override
    public void generateItemTagsFor(Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tagAccessor, BibliocraftWoodType woodType) {
        tagAccessor.apply(BCTags.Items.BOOKCASES).add(BCItems.BOOKCASE.get(woodType));
        tagAccessor.apply(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.FANCY_ARMOR_STAND.get(woodType));
        tagAccessor.apply(BCTags.Items.LABELS).add(BCItems.LABEL.get(woodType));
        tagAccessor.apply(BCTags.Items.POTION_SHELVES).add(BCItems.POTION_SHELF.get(woodType));
        tagAccessor.apply(BCTags.Items.SHELVES).add(BCItems.SHELF.get(woodType));
        tagAccessor.apply(BCTags.Items.TOOL_RACKS).add(BCItems.TOOL_RACK.get(woodType));
        DatagenUtil.addColorVariants(woodType, BCItems.DISPLAY_CASE, tagAccessor.apply(BCTags.Items.DISPLAY_CASES));
        DatagenUtil.addColorVariants(woodType, BCItems.SEAT, tagAccessor.apply(BCTags.Items.SEATS));
    }

    @Override
    public void generateLootTablesFor(BiConsumer<Block, LootTable.Builder> lootTableAdder, BibliocraftWoodType woodType) {
        loot(lootTableAdder, BCBlocks.BOOKCASE.get(woodType), DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.FANCY_ARMOR_STAND.get(woodType), DatagenUtil::createFancyArmorStandTable);
        loot(lootTableAdder, BCBlocks.LABEL.get(woodType), DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.POTION_SHELF.get(woodType), DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.SHELF.get(woodType), DatagenUtil::createNameableTable);
        loot(lootTableAdder, BCBlocks.TOOL_RACK.get(woodType), DatagenUtil::createNameableTable);
        for (DyeColor color : DyeColor.values()) {
            loot(lootTableAdder, BCBlocks.DISPLAY_CASE.get(woodType, color), DatagenUtil::createDefaultTable);
            loot(lootTableAdder, BCBlocks.WALL_DISPLAY_CASE.get(woodType, color), DatagenUtil::createDefaultTable);
            loot(lootTableAdder, BCBlocks.SEAT.get(woodType, color), DatagenUtil::createDefaultTable);
        }
    }

    @Override
    public void generateRecipesFor(RecipeOutput output, BibliocraftWoodType woodType) {
        shapedRecipe(output, BCItems.BOOKCASE.get(woodType), woodType, (builder, wood) -> builder
                .pattern("PSP")
                .pattern("PSP")
                .pattern("PSP")
                .define('P', wood.getFamily().getBaseBlock())
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB)));
        shapedRecipe(output, BCItems.FANCY_ARMOR_STAND.get(woodType), woodType, (builder, wood) -> builder
                .pattern(" R ")
                .pattern(" R ")
                .pattern("SSS")
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB))
                .define('R', Tags.Items.RODS_WOODEN));
        shapedRecipe(output, BCItems.LABEL.get(woodType), woodType, (builder, wood) -> builder
                .pattern("SSS")
                .pattern("SSS")
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB)));
        shapedRecipe(output, BCItems.POTION_SHELF.get(woodType), woodType, (builder, wood) -> builder
                .pattern("SSS")
                .pattern("PBP")
                .pattern("SSS")
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB))
                .define('P', wood.getFamily().getBaseBlock())
                .define('B', Items.GLASS_BOTTLE));
        shapedRecipe(output, BCItems.SHELF.get(woodType), woodType, (builder, wood) -> builder
                .pattern("SSS")
                .pattern(" P ")
                .pattern("SSS")
                .define('P', wood.getFamily().getBaseBlock())
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB)));
        shapedRecipe(output, BCItems.TOOL_RACK.get(woodType), woodType, (builder, wood) -> builder
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB))
                .define('I', Tags.Items.INGOTS_IRON));
        for (DyeColor color : DyeColor.values()) {
            shapedRecipe(output, BCItems.DISPLAY_CASE.get(woodType, color), woodType, (builder, wood) -> builder
                    .pattern("SGS")
                    .pattern("SWS")
                    .pattern("SSS")
                    .define('S', wood.getFamily().get(BlockFamily.Variant.SLAB))
                    .define('G', Tags.Items.GLASS)
                    .define('W', BuiltInRegistries.ITEM.get(new ResourceLocation(color.getName() + "_wool"))));
            shapedRecipe(output, BCItems.SEAT.get(woodType, color), woodType, (builder, wood) -> builder
                    .pattern(" W ")
                    .pattern("PPP")
                    .pattern("S S")
                    .define('W', BuiltInRegistries.ITEM.get(new ResourceLocation(color.getName() + "_wool")))
                    .define('P', wood.getFamily().getBaseBlock())
                    .define('S', Tags.Items.RODS_WOODEN));
        }
    }

    /**
     * @param path The path of the {@link ResourceLocation}.
     * @return A new {@link ResourceLocation} with Bibliocraft's namespace and the given path.
     */
    private static ResourceLocation bcLoc(String path) {
        return new ResourceLocation(Bibliocraft.MOD_ID, path);
    }

    /**
     * @param s The string to create a translation for.
     * @return A translated form of the given string.
     */
    private static String toTranslation(String s) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (char c : s.toCharArray()) {
            if (c == '_') {
                builder.append(' ');
                first = true;
            }
            if (first) {
                builder.append(Character.toUpperCase(c));
                first = false;
            } else {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
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
        provider.add(holder.get(woodType), toTranslation(woodType.getPath()) + " " + suffix);
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
        provider.add(holder.get(woodType, color), toTranslation(color.getName()) + " " + toTranslation(woodType.getPath()) + " " + suffix);
    }

    /**
     * Adds an item with a parent block model of the same name.
     *
     * @param provider Your mod's {@link ItemModelProvider}.
     * @param name     The name of the item model.
     * @return The {@link ItemModelBuilder}, for chaining.
     */
    private static ItemModelBuilder withParentBlock(ItemModelProvider provider, String name) {
        return withParent(provider, name, n -> bcLoc("block/" + n));
    }

    /**
     * Adds an item with a parent model provided by the given factory.
     *
     * @param provider      Your mod's {@link ItemModelProvider}.
     * @param name          The name of the item model.
     * @param parentFactory The factory for the parent model. Receives the name as input.
     * @return The {@link ItemModelBuilder}, for chaining.
     */
    private static ItemModelBuilder withParent(ItemModelProvider provider, String name, Function<String, ResourceLocation> parentFactory) {
        return provider.withExistingParent(name, parentFactory.apply(name));
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
     * @param output            The {@link RecipeOutput} to use.
     * @param item              The item.
     * @param woodType          The {@link BibliocraftWoodType}.
     * @param recipeTransformer A function that adds the actual recipe to the provided {@link ShapedRecipeBuilder}.
     */
    private static void shapedRecipe(RecipeOutput output, Item item, BibliocraftWoodType woodType, BiFunction<ShapedRecipeBuilder, BibliocraftWoodType, ShapedRecipeBuilder> recipeTransformer) {
        recipeTransformer.apply(ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item), woodType)
                .unlockedBy("has_planks", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(woodType.getFamily().getBaseBlock()).build()))))
                .save(output);
    }
}
