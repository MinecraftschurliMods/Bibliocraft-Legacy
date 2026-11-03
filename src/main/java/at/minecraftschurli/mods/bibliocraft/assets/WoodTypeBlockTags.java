package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// Block tags of the Bibliocraft blocks of the given wood types.
final class WoodTypeBlockTags extends BlockTagsProvider {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, List<BibliocraftWoodType> woodTypes) {
        super(output, lookupProvider, BibliocraftApi.MOD_ID);
        this.woodTypes = woodTypes;
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        for (BibliocraftWoodType woodType : woodTypes) {
            tag(BCTags.Blocks.BOOKCASES).add(BCBlocks.BOOKCASE.get(woodType));
            tag(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD).add(BCBlocks.FANCY_ARMOR_STAND.get(woodType));
            tag(BCTags.Blocks.FANCY_CLOCKS).add(BCBlocks.FANCY_CLOCK.get(woodType));
            tag(BCTags.Blocks.FANCY_CLOCKS).add(BCBlocks.WALL_FANCY_CLOCK.get(woodType));
            tag(BCTags.Blocks.FANCY_CRAFTERS).add(BCBlocks.FANCY_CRAFTER.get(woodType));
            tag(BCTags.Blocks.FANCY_SIGNS).add(BCBlocks.FANCY_SIGN.get(woodType));
            tag(BCTags.Blocks.FANCY_SIGNS).add(BCBlocks.WALL_FANCY_SIGN.get(woodType));
            tag(BCTags.Blocks.GRANDFATHER_CLOCKS).add(BCBlocks.GRANDFATHER_CLOCK.get(woodType));
            tag(BCTags.Blocks.LABELS).add(BCBlocks.LABEL.get(woodType));
            tag(BCTags.Blocks.POTION_SHELVES).add(BCBlocks.POTION_SHELF.get(woodType));
            tag(BCTags.Blocks.SHELVES).add(BCBlocks.SHELF.get(woodType));
            tag(BCTags.Blocks.TABLES).add(BCBlocks.TABLE.get(woodType));
            tag(BCTags.Blocks.TOOL_RACKS).add(BCBlocks.TOOL_RACK.get(woodType));
            tag(BCTags.Blocks.DISPLAY_CASES).addAll(BCBlocks.DISPLAY_CASE.group(woodType).stream());
            tag(BCTags.Blocks.DISPLAY_CASES).addAll(BCBlocks.WALL_DISPLAY_CASE.group(woodType).stream());
            tag(BCTags.Blocks.SEATS).addAll(BCBlocks.SEAT.group(woodType).stream());
            tag(BCTags.Blocks.SEAT_BACKS).addAll(BCBlocks.SEAT_BACK.group(woodType).stream());
        }
    }

    @Override
    public String getName() {
        return "Bibliocraft wood type block tags";
    }
}
