package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

/**
 * The default {@link BlockTagsProvider} implementation clears the builders before calling {@link BlockTagsProvider#addTags(HolderLookup.Provider)}.
 * We don't want that, so we override {@link BlockTagsProvider#addTags(HolderLookup.Provider)} to not do that.
 */
public abstract class NonClearingBlockTagsProvider extends BlockTagsProvider {
    // Store the provider here because while the superclass has it, it is private there.
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    /**
     * See super constructor for information.
     */
    public NonClearingBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
        this.lookupProvider = lookupProvider;
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return lookupProvider.thenApply(provider -> {
            addTags(provider);
            return provider;
        });
    }

    @Override
    public TagAppender<Block, Block> tag(TagKey<Block> key) {
        return super.tag(key);
    }
}
