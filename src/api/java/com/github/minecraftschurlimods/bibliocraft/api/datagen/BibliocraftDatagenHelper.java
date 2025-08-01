package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.function.Function;

/**
 * This class provides helper methods to generate datagen entries for Bibliocraft blocks with your mod's wood type(s). Get via {@link BibliocraftApi#getDatagenHelper()}.
 * To use this class, during {@link net.neoforged.neoforge.data.event.GatherDataEvent}, create a new instance of this class with your mod id.
 * Then, call whatever methods you need from the respective providers. Always pass in your mod's corresponding data provider.
 */
@SuppressWarnings("unused")
public interface BibliocraftDatagenHelper {
    /**
     * Marks a {@link BibliocraftWoodType} as to-be-datagenned. This method is thread-safe.
     *
     * @param woodType The {@link BibliocraftWoodType} to mark.
     */
    void addWoodTypeToGenerate(BibliocraftWoodType woodType);

    /**
     * @return An unmodifiable list of all {@link BibliocraftWoodType}s to datagen.
     */
    List<BibliocraftWoodType> getWoodTypesToGenerate();

    /**
     * Generates the English (en_us) translation files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param provider The {@link LanguageProvider} to use.
     * @param woodType The {@link BibliocraftWoodType} to generate the translations for.
     */
    void generateEnglishTranslationsFor(LanguageProvider provider, BibliocraftWoodType woodType);

    /**
     * Generates the blockstates and block model files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param provider The {@link BlockStateProvider} to use. Note: The provider's mod id must be the same as {@link BibliocraftApi#MOD_ID}!
     * @param woodType The {@link BibliocraftWoodType} to generate the blockstates and block models for.
     */
    void generateBlockStatesFor(BlockStateProvider provider, BibliocraftWoodType woodType);

    /**
     * Generates the item model files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param provider The {@link ItemModelProvider} to use. Note: The provider's mod id must be the same as {@link BibliocraftApi#MOD_ID}!
     * @param woodType The {@link BibliocraftWoodType} to generate the item models for.
     */
    void generateItemModelsFor(ItemModelProvider provider, BibliocraftWoodType woodType);

    /**
     * Generates the block tag files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param tagAccessor A reference to your mod's {@link BlockTagsProvider#tag(TagKey)} method, as it is protected for some reason.
     * @param woodType    The {@link BibliocraftWoodType} to generate the block tags for.
     */
    void generateBlockTagsFor(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAccessor, BibliocraftWoodType woodType);

    /**
     * Generates the item tag files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param tagAccessor A reference to your mod's {@link ItemTagsProvider#tag(TagKey)} method, as it is protected for some reason.
     * @param woodType    The {@link BibliocraftWoodType} to generate the item tags for.
     */
    void generateItemTagsFor(Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tagAccessor, BibliocraftWoodType woodType);

    /**
     * Generates the loot table files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param provider The {@link BlockLootTableProvider} to use.
     * @param woodType The {@link BibliocraftWoodType} to generate the loot tables for.
     */
    void generateLootTablesFor(BlockLootTableProvider provider, BibliocraftWoodType woodType);

    /**
     * Generates the recipe files for Bibliocraft blocks with a {@link BibliocraftWoodType}.
     *
     * @param output   The {@link RecipeOutput} to use.
     * @param woodType The {@link BibliocraftWoodType} to generate the recipes for.
     * @param modId    The namespace to store the recipes under.
     */
    void generateRecipesFor(RecipeOutput output, BibliocraftWoodType woodType, String modId);

    /**
     * Generates language files, block and item models, block and item tags, loot tables, and recipes for Bibliocraft blocks with a {@link BibliocraftWoodType}. Call this directly from a {@link GatherDataEvent} handler!
     *
     * @param woodType                The {@link BibliocraftWoodType} to generate the files for.
     * @param modId                   The namespace to store the files under, where applicable.
     * @param event                   The {@link GatherDataEvent} whose handler this is called from.
     * @param englishLanguageProvider The {@link LanguageProvider} to use for generating english translations.
     * @param blockTagsProvider       The {@link BlockTagsProvider} to use for generating block tags. It is strongly suggested this be a {@link NonClearingBlockTagsProvider}.
     * @param itemTagsProvider        The {@link ItemTagsProvider} to use for generating item tags. It is strongly suggested this be a {@link NonClearingItemTagsProvider}.
     */
    void generateAllFor(BibliocraftWoodType woodType, String modId, GatherDataEvent event, LanguageProvider englishLanguageProvider, BlockTagsProvider blockTagsProvider, ItemTagsProvider itemTagsProvider);

    /**
     * Marks all {@link BibliocraftWoodType}s from the given mod as to-be-datagenned. This method is thread-safe.
     *
     * @param modid The id of the mod to mark the {@link BibliocraftWoodType}s of.
     */
    default void addWoodTypesToGenerateByModid(String modid) {
        BibliocraftApi.getWoodTypeRegistry().getAll().stream()
                .filter(e -> e.getNamespace().equals(modid))
                .forEach(this::addWoodTypeToGenerate);
    }

    /**
     * Generates the English (en_us) translation files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param provider Your mod's {@link LanguageProvider}.
     */
    default void generateEnglishTranslations(LanguageProvider provider) {
        getWoodTypesToGenerate().forEach(woodType -> generateEnglishTranslationsFor(provider, woodType));
    }

    /**
     * Generates the blockstates and block model files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param provider Your mod's {@link BlockStateProvider}.
     */
    default void generateBlockStates(BlockStateProvider provider) {
        getWoodTypesToGenerate().forEach(woodType -> generateBlockStatesFor(provider, woodType));
    }

    /**
     * Generates the item model files for Bibliocraft items with your mod's wood type(s).
     *
     * @param provider Your mod's {@link ItemModelProvider}.
     */
    default void generateItemModels(ItemModelProvider provider) {
        getWoodTypesToGenerate().forEach(woodType -> generateItemModelsFor(provider, woodType));
    }

    /**
     * Generates the block tag files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param tagAccessor A reference to your mod's {@link BlockTagsProvider#tag(TagKey)} method, as it is protected for some reason.
     */
    default void generateBlockTags(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAccessor) {
        getWoodTypesToGenerate().forEach(woodType -> generateBlockTagsFor(tagAccessor, woodType));
    }

    /**
     * Generates the item tag files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param tagAccessor A reference to your mod's {@link ItemTagsProvider#tag(TagKey)} method, as it is protected for some reason.
     */
    default void generateItemTags(Function<TagKey<Item>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> tagAccessor) {
        getWoodTypesToGenerate().forEach(woodType -> generateItemTagsFor(tagAccessor, woodType));
    }

    /**
     * Generates the loot table files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param provider Your mod's {@link BlockLootTableProvider}.
     */
    default void generateLootTables(BlockLootTableProvider provider) {
        getWoodTypesToGenerate().forEach(woodType -> generateLootTablesFor(provider, woodType));
    }

    /**
     * Generates the recipe files for Bibliocraft blocks with your mod's wood type(s).
     *
     * @param output Your mod's {@link RecipeOutput}.
     * @param modId  Your mod's namespace.
     */
    default void generateRecipes(RecipeOutput output, String modId) {
        getWoodTypesToGenerate().forEach(woodType -> generateRecipesFor(output, woodType, modId));
    }

    /**
     * Generates language files, block and item models, block and item tags, loot tables, and recipes for Bibliocraft blocks with your mod's wood type(s). Call this directly from a {@link GatherDataEvent} handler!
     *
     * @param modId                   The namespace to store the files under, where applicable.
     * @param event                   The {@link GatherDataEvent} whose handler this is called from.
     * @param englishLanguageProvider The {@link LanguageProvider} to use for generating english translations.
     * @param blockTagsProvider       The {@link BlockTagsProvider} to use for generating block tags. It is strongly suggested this be a {@link NonClearingBlockTagsProvider}.
     * @param itemTagsProvider        The {@link ItemTagsProvider} to use for generating item tags. It is strongly suggested this be a {@link NonClearingItemTagsProvider}.
     */
    default void generateAll(String modId, GatherDataEvent event, LanguageProvider englishLanguageProvider, BlockTagsProvider blockTagsProvider, ItemTagsProvider itemTagsProvider) {
        getWoodTypesToGenerate().forEach(woodType -> generateAllFor(woodType, modId, event, englishLanguageProvider, blockTagsProvider, itemTagsProvider));
    }
}
