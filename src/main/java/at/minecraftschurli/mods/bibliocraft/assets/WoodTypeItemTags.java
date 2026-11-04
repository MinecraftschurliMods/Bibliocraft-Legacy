package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// Item tags of the Bibliocraft items of the given wood types.
final class WoodTypeItemTags extends ItemTagsProvider {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, List<BibliocraftWoodType> woodTypes) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID);
        this.woodTypes = woodTypes;
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        for (BibliocraftWoodType woodType : woodTypes) {
            tag(BCTags.Items.BOOKCASES).add(BCItems.BOOKCASE.get(woodType));
            tag(BCTags.Items.FANCY_ARMOR_STANDS_WOOD).add(BCItems.FANCY_ARMOR_STAND.get(woodType));
            tag(BCTags.Items.FANCY_CLOCKS).add(BCItems.FANCY_CLOCK.get(woodType));
            tag(BCTags.Items.FANCY_CRAFTERS).add(BCItems.FANCY_CRAFTER.get(woodType));
            tag(BCTags.Items.FANCY_SIGNS).add(BCItems.FANCY_SIGN.get(woodType));
            tag(BCTags.Items.GRANDFATHER_CLOCKS).add(BCItems.GRANDFATHER_CLOCK.get(woodType));
            tag(BCTags.Items.LABELS).add(BCItems.LABEL.get(woodType));
            tag(BCTags.Items.POTION_SHELVES).add(BCItems.POTION_SHELF.get(woodType));
            tag(BCTags.Items.SHELVES).add(BCItems.SHELF.get(woodType));
            tag(BCTags.Items.TABLES).add(BCItems.TABLE.get(woodType));
            tag(BCTags.Items.TOOL_RACKS).add(BCItems.TOOL_RACK.get(woodType));
            tag(BCTags.Items.DISPLAY_CASES).addAll(BCItems.DISPLAY_CASE.group(woodType).stream());
            tag(BCTags.Items.SEATS).addAll(BCItems.SEAT.group(woodType).stream());
            tag(BCTags.Items.SEAT_BACKS_SMALL).addAll(BCItems.SMALL_SEAT_BACK.group(woodType).stream());
            tag(BCTags.Items.SEAT_BACKS_RAISED).addAll(BCItems.RAISED_SEAT_BACK.group(woodType).stream());
            tag(BCTags.Items.SEAT_BACKS_FLAT).addAll(BCItems.FLAT_SEAT_BACK.group(woodType).stream());
            tag(BCTags.Items.SEAT_BACKS_TALL).addAll(BCItems.TALL_SEAT_BACK.group(woodType).stream());
            tag(BCTags.Items.SEAT_BACKS_FANCY).addAll(BCItems.FANCY_SEAT_BACK.group(woodType).stream());
        }
    }

    @Override
    public String getName() {
        return "Bibliocraft wood type item tags";
    }
}
