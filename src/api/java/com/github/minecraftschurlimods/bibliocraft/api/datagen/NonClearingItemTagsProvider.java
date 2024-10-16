package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The default {@link ItemTagsProvider} implementation clears the builders before calling {@link ItemTagsProvider#addTags(HolderLookup.Provider)}.
 * We don't want that, so we override {@link ItemTagsProvider#addTags(HolderLookup.Provider)} to not do that.
 */
public abstract class NonClearingItemTagsProvider extends ItemTagsProvider {
    // Store the provider here because while the superclass has it, it is private there.
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    /**
     * See super constructor for information.
     */
    public NonClearingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
        this.lookupProvider = lookupProvider;
    }

    /**
     * See super constructor for information.
     */
    @Deprecated
    public NonClearingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> parentProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, parentProvider, blockTags);
        this.lookupProvider = lookupProvider;
    }

    /**
     * See super constructor for information.
     */
    public NonClearingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, modId, existingFileHelper);
        this.lookupProvider = lookupProvider;
    }

    /**
     * See super constructor for information.
     */
    public NonClearingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> parentProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, parentProvider, blockTags, modId, existingFileHelper);
        this.lookupProvider = lookupProvider;
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return lookupProvider.thenApply(provider -> {
            addTags(provider);
            return provider;
        }).thenCombine(blockTags, (provider, tagLookup) -> {
            tagsToCopy.forEach((block, item) -> {
                TagBuilder tagBuilder = getOrCreateRawBuilder(item);
                tagLookup.apply(block).orElseThrow(() -> new IllegalStateException("Missing block tag " + item.location())).build().forEach(tagBuilder::add);
            });
            return provider;
        });
    }
}
