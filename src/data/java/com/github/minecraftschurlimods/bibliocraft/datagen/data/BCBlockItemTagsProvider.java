package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class BCBlockItemTagsProvider {
    @SuppressWarnings("unchecked")
    static void addBlockTags(Function<TagKey<Block>, TagAppender<Block, Block>> tag) {
        tag.apply(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD);
        tag.apply(BCTags.Blocks.BOOKCASES);
        tag.apply(BCTags.Blocks.DISPLAY_CASES);
        tag.apply(BCTags.Blocks.FANCY_CLOCKS);
        tag.apply(BCTags.Blocks.FANCY_CRAFTERS);
        tag.apply(BCTags.Blocks.FANCY_SIGNS);
        tag.apply(BCTags.Blocks.GRANDFATHER_CLOCKS);
        tag.apply(BCTags.Blocks.LABELS);
        tag.apply(BCTags.Blocks.POTION_SHELVES);
        tag.apply(BCTags.Blocks.SEATS);
        tag.apply(BCTags.Blocks.SEAT_BACKS);
        tag.apply(BCTags.Blocks.SHELVES);
        tag.apply(BCTags.Blocks.TABLES);
        tag.apply(BCTags.Blocks.TOOL_RACKS);
        tag.apply(BCTags.Blocks.FANCY_ARMOR_STANDS).addTag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.IRON_FANCY_ARMOR_STAND.get());
        tag.apply(BCTags.Blocks.FANCY_LAMPS).addTags(BCTags.Blocks.FANCY_LAMPS_GOLD, BCTags.Blocks.FANCY_LAMPS_IRON, BCTags.Blocks.FANCY_LAMPS_COPPER);
        tag.apply(BCTags.Blocks.FANCY_LAMPS_GOLD).add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get()).addAll(BCBlocks.FANCY_GOLD_LAMP.stream());
        tag.apply(BCTags.Blocks.FANCY_LAMPS_IRON).add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get()).addAll(BCBlocks.FANCY_IRON_LAMP.stream());
        tag.apply(BCTags.Blocks.FANCY_LAMPS_COPPER)
                .addAll(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWaxed().stream().map(DeferredBlock::get))
                .addAll(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWeathering().stream().map(DeferredBlock::get))
                .addAll(BCBlocks.FANCY_COPPER_LAMP.getWaxed().stream().flatMap(GroupedHolder::stream))
                .addAll(BCBlocks.FANCY_COPPER_LAMP.getWeathering().stream().flatMap(GroupedHolder::stream));
        tag.apply(BCTags.Blocks.FANCY_LANTERNS).addTags(BCTags.Blocks.FANCY_LANTERNS_GOLD, BCTags.Blocks.FANCY_LANTERNS_IRON, BCTags.Blocks.FANCY_LANTERNS_COPPER);
        tag.apply(BCTags.Blocks.FANCY_LANTERNS_GOLD)
                .add(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get())
                .addAll(BCBlocks.FANCY_GOLD_LANTERN.stream())
                .addOptional(BCBlocks.SOUL_FANCY_GOLD_LANTERN.get());
        tag.apply(BCTags.Blocks.FANCY_LANTERNS_IRON)
                .add(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get())
                .addAll(BCBlocks.FANCY_IRON_LANTERN.stream())
                .addOptional(BCBlocks.SOUL_FANCY_IRON_LANTERN.get());
        tag.apply(BCTags.Blocks.FANCY_LANTERNS_COPPER)
                .addAll(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWaxed().stream().map(DeferredBlock::get))
                .addAll(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWeathering().stream().map(DeferredBlock::get))
                .addAll(BCBlocks.FANCY_COPPER_LANTERN.getWaxed().stream().flatMap(GroupedHolder::stream))
                .addAll(BCBlocks.FANCY_COPPER_LANTERN.getWeathering().stream().flatMap(GroupedHolder::stream));
        tag.apply(BCTags.Blocks.PRINTING_TABLES).add(BCBlocks.PRINTING_TABLE.get(), BCBlocks.IRON_PRINTING_TABLE.get());
        tag.apply(BCTags.Blocks.TYPEWRITERS).add(BCBlocks.CLEAR_TYPEWRITER.get()).addAll(BCBlocks.TYPEWRITER.values());
    }
}
