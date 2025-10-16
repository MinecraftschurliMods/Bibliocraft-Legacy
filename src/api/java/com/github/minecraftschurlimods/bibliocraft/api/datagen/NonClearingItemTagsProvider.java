package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

/**
 * The default {@link ItemTagsProvider} implementation clears the builders before calling {@link ItemTagsProvider#addTags(HolderLookup.Provider)}.
 * We don't want that, so we override {@link ItemTagsProvider#addTags(HolderLookup.Provider)} to not do that.
 */
@SuppressWarnings("unused")
public abstract class NonClearingItemTagsProvider extends ItemTagsProvider {
    // Store the provider here because while the superclass has it, it is private there.
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    /**
     * See super constructor for information.
     */
    public NonClearingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
        this.lookupProvider = lookupProvider;
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return lookupProvider.thenApply(provider -> {
            addTags(provider);
            return provider;
            // TODO commented out temporarily
        })/*.thenCombine(blockTags, (provider, tagLookup) -> {
            tagsToCopy.forEach((block, item) -> {
                TagBuilder tagBuilder = getOrCreateRawBuilder(item);
                tagLookup.apply(block).orElseThrow(() -> new IllegalStateException("Missing block tag " + item.location())).build().forEach(tagBuilder::add);
            });
            return provider;
        })*/;
    }

    protected TagAppender<ResourceLocation, Item> lazyTag(TagKey<Item> key) {
        TagBuilder tagbuilder = this.getOrCreateRawBuilder(key);
        return TagAppender.<Item>forBuilder(tagbuilder).map(rl -> ResourceKey.create(Registries.ITEM, rl));
    }
}
