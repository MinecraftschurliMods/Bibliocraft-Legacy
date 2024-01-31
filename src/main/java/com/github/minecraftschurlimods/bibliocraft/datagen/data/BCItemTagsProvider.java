package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public final class BCItemTagsProvider extends ItemTagsProvider {
    public BCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> lookup, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookup, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        woodenTag(BCTags.Items.BOOKCASES, BCItems.BOOKCASE);
        coloredWoodenTag(BCTags.Items.DISPLAY_CASES, BCItems.DISPLAY_CASE);
        woodenTag(BCTags.Items.FANCY_ARMOR_STANDS_WOOD, BCItems.FANCY_ARMOR_STAND);
        woodenTag(BCTags.Items.LABELS, BCItems.LABEL);
        woodenTag(BCTags.Items.POTION_SHELVES, BCItems.POTION_SHELF);
        coloredWoodenTag(BCTags.Items.SEATS, BCItems.SEAT);
        woodenTag(BCTags.Items.SHELVES, BCItems.SHELF);
        woodenTag(BCTags.Items.TOOL_RACKS, BCItems.TOOL_RACK);
        tag(BCTags.Items.FANCY_ARMOR_STANDS).addTag(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.IRON_FANCY_ARMOR_STAND.get());
        tag(BCTags.Items.BOOKCASE_BOOKS).addTags(ItemTags.BOOKSHELF_BOOKS, ItemTags.LECTERN_BOOKS).add(BCItems.REDSTONE_BOOK.get()).addOptional(new ResourceLocation("patchouli", "guide_book"));
        tag(BCTags.Items.POTION_SHELF_POTIONS).add(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.GLASS_BOTTLE, Items.EXPERIENCE_BOTTLE, Items.HONEY_BOTTLE, Items.DRAGON_BREATH);
        tag(BCTags.Items.SWORD_PEDESTAL_SWORDS).addTag(ItemTags.SWORDS);
        tag(BCTags.Items.TOOL_RACK_TOOLS).addTag(Tags.Items.TOOLS);
    }

    /**
     * Adds a tag containing all values of one or multiple {@link WoodTypeDeferredHolder}s.
     *
     * @param tag     The {@link TagKey} to use.
     * @param holders The {@link WoodTypeDeferredHolder}s to add.
     */
    @SafeVarargs
    private void woodenTag(TagKey<Item> tag, WoodTypeDeferredHolder<Item, ? extends Item>... holders) {
        tag(tag).add(Arrays.stream(holders).flatMap(e -> e.values().stream()).toList().toArray(new Item[0]));
    }

    /**
     * Adds a tag containing all values of one or multiple {@link ColoredWoodTypeDeferredHolder}s.
     *
     * @param tag     The {@link TagKey} to use.
     * @param holders The {@link ColoredWoodTypeDeferredHolder}s to add.
     */
    @SuppressWarnings("SameParameterValue")
    @SafeVarargs
    private void coloredWoodenTag(TagKey<Item> tag, ColoredWoodTypeDeferredHolder<Item, ? extends Item>... holders) {
        tag(tag).add(Arrays.stream(holders).flatMap(e -> e.values().stream()).toList().toArray(new Item[0]));
    }
}
