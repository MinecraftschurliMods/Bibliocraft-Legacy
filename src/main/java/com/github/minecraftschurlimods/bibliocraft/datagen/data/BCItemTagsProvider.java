package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class BCItemTagsProvider extends ItemTagsProvider {
    public BCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookup, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(BCTags.Items.BOOKCASE_BOOKS).addTags(ItemTags.BOOKSHELF_BOOKS, ItemTags.LECTERN_BOOKS).addOptional(new ResourceLocation("patchouli", "guide_book"));
    }
}
