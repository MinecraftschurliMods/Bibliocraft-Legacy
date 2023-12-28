package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BCBlockTagsProvider extends BlockTagsProvider {
    public BCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        woodenTag(BCTags.Blocks.BOOKCASES, BCBlocks.BOOKCASE);
        coloredWoodenTag(BCTags.Blocks.DISPLAY_CASES, BCBlocks.DISPLAY_CASE, BCBlocks.WALL_DISPLAY_CASE);
        woodenTag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD, BCBlocks.FANCY_ARMOR_STAND);
        woodenTag(BCTags.Blocks.POTION_SHELVES, BCBlocks.POTION_SHELF);
        woodenTag(BCTags.Blocks.SHELVES, BCBlocks.SHELF);
        woodenTag(BCTags.Blocks.TOOL_RACKS, BCBlocks.TOOL_RACK);
        tag(BCTags.Blocks.FANCY_ARMOR_STANDS).addTag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.IRON_FANCY_ARMOR_STAND.get());
        tag(BlockTags.MINEABLE_WITH_AXE).addTags(BCTags.Blocks.BOOKCASES, BCTags.Blocks.DISPLAY_CASES, BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD, BCTags.Blocks.POTION_SHELVES, BCTags.Blocks.SHELVES, BCTags.Blocks.TOOL_RACKS);
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), BCBlocks.SWORD_PEDESTAL.get());
    }

    /**
     * Adds a tag containing all values of one or multiple {@link WoodTypeDeferredHolder}s.
     *
     * @param tag     The {@link TagKey} to use.
     * @param holders The {@link WoodTypeDeferredHolder}s to add.
     */
    @SafeVarargs
    private void woodenTag(TagKey<Block> tag, WoodTypeDeferredHolder<Block, ? extends Block>... holders) {
        tag(tag).add(Arrays.stream(holders).flatMap(e -> e.values().stream()).toList().toArray(new Block[0]));
    }

    /**
     * Adds a tag containing all values of one or multiple {@link ColoredWoodTypeDeferredHolder}s.
     *
     * @param tag     The {@link TagKey} to use.
     * @param holders The {@link ColoredWoodTypeDeferredHolder}s to add.
     */
    @SafeVarargs
    private void coloredWoodenTag(TagKey<Block> tag, ColoredWoodTypeDeferredHolder<Block, ? extends Block>... holders) {
        tag(tag).add(Arrays.stream(holders).flatMap(e -> e.values().stream()).toList().toArray(new Block[0]));
    }
}
