package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class BCItemTagsProvider extends ItemTagsProvider {
    public BCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookup, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        BibliocraftDatagenHelper.get().generateItemTags(this::tag);
        tag(BCTags.Items.FANCY_ARMOR_STANDS).addTag(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.IRON_FANCY_ARMOR_STAND.get());
        tag(BCTags.Items.SEAT_BACKS).addTags(BCTags.Items.SEAT_BACKS_SMALL, BCTags.Items.SEAT_BACKS_RAISED, BCTags.Items.SEAT_BACKS_FLAT, BCTags.Items.SEAT_BACKS_TALL, BCTags.Items.SEAT_BACKS_FANCY);
        tag(BCTags.Items.BOOKCASE_BOOKS).addTags(ItemTags.BOOKSHELF_BOOKS, ItemTags.LECTERN_BOOKS).add(BCItems.REDSTONE_BOOK.get()).addOptional(new ResourceLocation("patchouli", "guide_book"));
        tag(BCTags.Items.POTION_SHELF_POTIONS).add(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.GLASS_BOTTLE, Items.EXPERIENCE_BOTTLE, Items.HONEY_BOTTLE, Items.DRAGON_BREATH);
        tag(BCTags.Items.SWORD_PEDESTAL_SWORDS).addTag(ItemTags.SWORDS);
        tag(BCTags.Items.TOOL_RACK_TOOLS).addTag(Tags.Items.TOOLS);
    }
}
