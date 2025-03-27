package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.NonClearingItemTagsProvider;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class BCItemTagsProvider extends NonClearingItemTagsProvider {
    public BCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookup, BibliocraftApi.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        copy(BCTags.Blocks.FANCY_ARMOR_STANDS, BCTags.Items.FANCY_ARMOR_STANDS);
        copy(BCTags.Blocks.FANCY_LAMPS, BCTags.Items.FANCY_LAMPS);
        copy(BCTags.Blocks.FANCY_LAMPS_GOLD, BCTags.Items.FANCY_LAMPS_GOLD);
        copy(BCTags.Blocks.FANCY_LAMPS_IRON, BCTags.Items.FANCY_LAMPS_IRON);
        copy(BCTags.Blocks.FANCY_LANTERNS, BCTags.Items.FANCY_LANTERNS);
        copy(BCTags.Blocks.FANCY_LANTERNS_GOLD, BCTags.Items.FANCY_LANTERNS_GOLD);
        copy(BCTags.Blocks.FANCY_LANTERNS_IRON, BCTags.Items.FANCY_LANTERNS_IRON);
        tag(BCTags.Items.SEAT_BACKS).addTags(BCTags.Items.SEAT_BACKS_SMALL, BCTags.Items.SEAT_BACKS_RAISED, BCTags.Items.SEAT_BACKS_FLAT, BCTags.Items.SEAT_BACKS_TALL, BCTags.Items.SEAT_BACKS_FANCY);
        tag(BCTags.Items.BOOKCASE_BOOKS).addTags(ItemTags.BOOKSHELF_BOOKS, ItemTags.LECTERN_BOOKS).addOptional(ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book"));
        tag(BCTags.Items.COOKIE_JAR_COOKIES).add(Items.COOKIE);
        tag(BCTags.Items.FANCY_SIGN_WAX).add(Items.HONEYCOMB);
        tag(BCTags.Items.POTION_SHELF_POTIONS).add(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.GLASS_BOTTLE, Items.EXPERIENCE_BOTTLE, Items.HONEY_BOTTLE, Items.DRAGON_BREATH);
        tag(BCTags.Items.SWORD_PEDESTAL_SWORDS).addTag(ItemTags.SWORDS);
        tag(BCTags.Items.TOOL_RACK_TOOLS).addTag(Tags.Items.TOOLS);
        tag(ItemTags.BOOKSHELF_BOOKS).add(BCItems.REDSTONE_BOOK.get(), BCItems.SLOTTED_BOOK.get(), BCItems.STOCKROOM_CATALOG.get());
        tag(ItemTags.DYEABLE).add(BCItems.SWORD_PEDESTAL.get());
    }
}
