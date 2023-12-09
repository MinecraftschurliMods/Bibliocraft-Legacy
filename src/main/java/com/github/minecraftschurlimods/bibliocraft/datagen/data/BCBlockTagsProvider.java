package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BCBlockTagsProvider extends BlockTagsProvider {
    public BCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        woodTypeTag(BCTags.Blocks.BOOKCASES, BCBlocks.BOOKCASE);
        woodTypeTag(BCTags.Blocks.POTION_SHELVES, BCBlocks.POTION_SHELF);
        woodTypeTag(BCTags.Blocks.SHELVES, BCBlocks.SHELF);
        woodTypeTag(BCTags.Blocks.TOOL_RACKS, BCBlocks.TOOL_RACK);
        tag(BlockTags.MINEABLE_WITH_AXE).addTags(BCTags.Blocks.BOOKCASES, BCTags.Blocks.POTION_SHELVES, BCTags.Blocks.SHELVES, BCTags.Blocks.TOOL_RACKS);
    }

    private void woodTypeTag(TagKey<Block> tag, WoodTypeDeferredHolder<Block, ? extends Block> holder) {
        tag(tag).add(holder.values().toArray(new Block[0]));
    }
}
