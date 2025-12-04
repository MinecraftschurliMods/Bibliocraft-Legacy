package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
     * Generates language files, block and item models, block and item tags, loot tables, and recipes for Bibliocraft blocks with your mod's wood type(s). Call this directly from a {@link GatherDataEvent} handler!
     *
     * @param modId            The namespace to store the files under, where applicable.
     * @param lookupProvider   The lookup provider to use for the datagen.
     * @param clientPack       The client datagen pack to use.
     * @param serverPack       The server datagen pack to use.
     * @param languageProvider Your mod's {@link LanguageProvider}.
     */
    void generateAll(String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, DataGenerator.PackGenerator clientPack, DataGenerator.PackGenerator serverPack, LanguageProvider languageProvider, Function<TagKey<Block>, TagAppender<Block, Block>> blockTagAppenderFactory, Function<TagKey<Item>, TagAppender<Item, Item>> itemTagAppenderFactory);
}
