package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class BCBlockTagsProvider extends BlockTagsProvider {
    public BCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        BibliocraftApi.getDatagenHelper().generateBlockTags(this::tag);
        tag(BCTags.Blocks.FANCY_ARMOR_STANDS).addTag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.IRON_FANCY_ARMOR_STAND.get());
        tag(BCTags.Blocks.FANCY_LANTERNS).addTags(BCTags.Blocks.FANCY_LANTERNS_GOLD, BCTags.Blocks.FANCY_LANTERNS_IRON);
        tag(BCTags.Blocks.FANCY_LANTERNS_GOLD).add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get()).add(BCBlocks.FANCY_GOLD_LAMP.values().stream().toList().toArray(new Block[0]));
        tag(BCTags.Blocks.FANCY_LANTERNS_IRON).add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get()).add(BCBlocks.FANCY_IRON_LAMP.values().stream().toList().toArray(new Block[0]));
        tag(BlockTags.MINEABLE_WITH_AXE).addTags(BCTags.Blocks.BOOKCASES, BCTags.Blocks.DISPLAY_CASES, BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD, BCTags.Blocks.LABELS, BCTags.Blocks.POTION_SHELVES, BCTags.Blocks.SEATS, BCTags.Blocks.SHELVES, BCTags.Blocks.TOOL_RACKS).add(BCBlocks.DISC_RACK.get(), BCBlocks.WALL_DISC_RACK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).addTags(BCTags.Blocks.FANCY_LANTERNS).add(BCBlocks.COOKIE_JAR.get(), BCBlocks.DESK_BELL.get(), BCBlocks.DINNER_PLATE.get(), BCBlocks.IRON_FANCY_ARMOR_STAND.get(), BCBlocks.SWORD_PEDESTAL.get());
    }
}
