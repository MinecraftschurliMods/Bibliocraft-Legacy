package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.NonClearingBlockTagsProvider;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public final class BCBlockTagsProvider extends NonClearingBlockTagsProvider {
    public BCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        BCBlockItemTagsProvider.addBlockTags(this::tag);
        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTags(BCTags.Blocks.BOOKCASES,
                        BCTags.Blocks.DISPLAY_CASES,
                        BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD,
                        BCTags.Blocks.FANCY_CLOCKS,
                        BCTags.Blocks.FANCY_CRAFTERS,
                        BCTags.Blocks.FANCY_SIGNS,
                        BCTags.Blocks.GRANDFATHER_CLOCKS,
                        BCTags.Blocks.LABELS,
                        BCTags.Blocks.POTION_SHELVES,
                        BCTags.Blocks.SEATS,
                        BCTags.Blocks.SEAT_BACKS,
                        BCTags.Blocks.SHELVES,
                        BCTags.Blocks.TABLES,
                        BCTags.Blocks.TOOL_RACKS)
                .add(BCBlocks.DISC_RACK.get(),
                        BCBlocks.WALL_DISC_RACK.get(),
                        BCBlocks.PRINTING_TABLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTags(
                        BCTags.Blocks.FANCY_LAMPS,
                        BCTags.Blocks.FANCY_LANTERNS,
                        BCTags.Blocks.TYPEWRITERS)
                .add(BCBlocks.COOKIE_JAR.get(),
                        BCBlocks.DESK_BELL.get(),
                        BCBlocks.DINNER_PLATE.get(),
                        BCBlocks.GOLD_CHAIN.get(),
                        BCBlocks.GOLD_LANTERN.get(),
                        BCBlocks.GOLD_SOUL_LANTERN.get(),
                        BCBlocks.IRON_FANCY_ARMOR_STAND.get(),
                        BCBlocks.IRON_PRINTING_TABLE.get(),
                        BCBlocks.SWORD_PEDESTAL.get());
    }

}
