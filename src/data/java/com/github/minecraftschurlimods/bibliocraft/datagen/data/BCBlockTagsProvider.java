package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.NonClearingBlockTagsProvider;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class BCBlockTagsProvider extends NonClearingBlockTagsProvider {
    public BCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(BCTags.Blocks.FANCY_ARMOR_STANDS).addTag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.IRON_FANCY_ARMOR_STAND.get());
        tag(BCTags.Blocks.FANCY_LAMPS).addTags(BCTags.Blocks.FANCY_LAMPS_GOLD, BCTags.Blocks.FANCY_LAMPS_IRON);
        Collection<? extends Block> collection4 = BCBlocks.FANCY_GOLD_LAMP.values();
        collection4.forEach(tag(BCTags.Blocks.FANCY_LAMPS_GOLD).add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get())::add);
        Collection<? extends Block> collection3 = BCBlocks.FANCY_IRON_LAMP.values();
        collection3.forEach(tag(BCTags.Blocks.FANCY_LAMPS_IRON).add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get())::add);
        tag(BCTags.Blocks.FANCY_LANTERNS).addTags(BCTags.Blocks.FANCY_LANTERNS_GOLD, BCTags.Blocks.FANCY_LANTERNS_IRON);
        Collection<? extends Block> collection2 = BCBlocks.FANCY_GOLD_LANTERN.values();
        collection2.forEach(tag(BCTags.Blocks.FANCY_LANTERNS_GOLD).add(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get()).addOptional(BCBlocks.SOUL_FANCY_GOLD_LANTERN.getId())::add);
        Collection<? extends Block> collection1 = BCBlocks.FANCY_IRON_LANTERN.values();
        collection1.forEach(tag(BCTags.Blocks.FANCY_LANTERNS_IRON).add(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get()).addOptional(BCBlocks.SOUL_FANCY_IRON_LANTERN.getId())::add);
        tag(BCTags.Blocks.PRINTING_TABLES).add(BCBlocks.PRINTING_TABLE.get(), BCBlocks.IRON_PRINTING_TABLE.get());
        Collection<? extends Block> collection = BCBlocks.TYPEWRITER.values();
        collection.forEach(tag(BCTags.Blocks.TYPEWRITERS).add(BCBlocks.CLEAR_TYPEWRITER.get())::add);
        tag(BlockTags.MINEABLE_WITH_AXE).addTags(BCTags.Blocks.BOOKCASES, BCTags.Blocks.DISPLAY_CASES, BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD, BCTags.Blocks.FANCY_CLOCKS, BCTags.Blocks.FANCY_CRAFTERS, BCTags.Blocks.FANCY_SIGNS, BCTags.Blocks.GRANDFATHER_CLOCKS, BCTags.Blocks.LABELS, BCTags.Blocks.POTION_SHELVES, BCTags.Blocks.SEATS, BCTags.Blocks.SEAT_BACKS, BCTags.Blocks.SHELVES, BCTags.Blocks.TABLES, BCTags.Blocks.TOOL_RACKS).add(BCBlocks.DISC_RACK.get(), BCBlocks.WALL_DISC_RACK.get(), BCBlocks.PRINTING_TABLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).addTags(BCTags.Blocks.FANCY_LAMPS, BCTags.Blocks.FANCY_LANTERNS, BCTags.Blocks.TYPEWRITERS).add(BCBlocks.COOKIE_JAR.get(), BCBlocks.DESK_BELL.get(), BCBlocks.DINNER_PLATE.get(), BCBlocks.GOLD_CHAIN.get(), BCBlocks.GOLD_LANTERN.get(), BCBlocks.GOLD_SOUL_LANTERN.get(), BCBlocks.IRON_FANCY_ARMOR_STAND.get(), BCBlocks.IRON_PRINTING_TABLE.get(), BCBlocks.SWORD_PEDESTAL.get());
    }
}
