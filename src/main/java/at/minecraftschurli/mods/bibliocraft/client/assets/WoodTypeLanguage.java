package at.minecraftschurli.mods.bibliocraft.client.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.util.DatagenUtil;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;

/// English translations of the Bibliocraft blocks and items of the given wood types.
final class WoodTypeLanguage extends LanguageProvider {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeLanguage(PackOutput output, List<BibliocraftWoodType> woodTypes) {
        super(output, BibliocraftApi.MOD_ID, "en_us");
        this.woodTypes = woodTypes;
    }

    @Override
    protected void addTranslations() {
        for (BibliocraftWoodType woodType : woodTypes) {
            woodenBlockTranslation(woodType, BCBlocks.BOOKCASE, "Bookcase");
            woodenBlockTranslation(woodType, BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
            woodenBlockTranslation(woodType, BCBlocks.FANCY_CLOCK, "Fancy Clock");
            woodenBlockTranslation(woodType, BCBlocks.WALL_FANCY_CLOCK, "Fancy Clock");
            woodenBlockTranslation(woodType, BCBlocks.FANCY_CRAFTER, "Fancy Crafter");
            woodenBlockTranslation(woodType, BCBlocks.FANCY_SIGN, "Fancy Sign");
            woodenBlockTranslation(woodType, BCBlocks.WALL_FANCY_SIGN, "Fancy Sign");
            woodenBlockTranslation(woodType, BCBlocks.GRANDFATHER_CLOCK, "Grandfather Clock");
            woodenBlockTranslation(woodType, BCBlocks.LABEL, "Label");
            woodenBlockTranslation(woodType, BCBlocks.POTION_SHELF, "Potion Shelf");
            woodenBlockTranslation(woodType, BCBlocks.SHELF, "Storage Shelf");
            woodenBlockTranslation(woodType, BCBlocks.TABLE, "Table");
            woodenBlockTranslation(woodType, BCBlocks.TOOL_RACK, "Tool Rack");
            for (DyeColor color : DyeColor.values()) {
                coloredWoodenBlockTranslation(woodType, color, BCBlocks.DISPLAY_CASE, "Display Case");
                coloredWoodenBlockTranslation(woodType, color, BCBlocks.WALL_DISPLAY_CASE, "Display Case");
                coloredWoodenBlockTranslation(woodType, color, BCBlocks.SEAT, "Seat");
                coloredWoodenBlockTranslation(woodType, color, BCBlocks.SEAT_BACK, "Seat Back");
                coloredWoodenItemTranslation(woodType, color, BCItems.SMALL_SEAT_BACK, "Small Seat Back");
                coloredWoodenItemTranslation(woodType, color, BCItems.RAISED_SEAT_BACK, "Raised Seat Back");
                coloredWoodenItemTranslation(woodType, color, BCItems.FLAT_SEAT_BACK, "Flat Seat Back");
                coloredWoodenItemTranslation(woodType, color, BCItems.TALL_SEAT_BACK, "Tall Seat Back");
                coloredWoodenItemTranslation(woodType, color, BCItems.FANCY_SEAT_BACK, "Fancy Seat Back");
            }
        }
    }

    @Override
    public String getName() {
        return "Bibliocraft wood type translations";
    }

    /// Adds an English (en_us) translation to the given [LanguageProvider].
    ///
    /// @param woodType The [BibliocraftWoodType] that is currently being processed.
    /// @param holder   The [GroupedHolder] to add the translation for.
    /// @param suffix   The suffix of the translation.
    private void woodenBlockTranslation(BibliocraftWoodType woodType, GroupedHolder<BibliocraftWoodType, Block, ?> holder, String suffix) {
        add(holder.get(woodType), DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /// Adds an English (en_us) translation to the given [LanguageProvider].
    ///
    /// @param woodType The [BibliocraftWoodType] that is currently being processed.
    /// @param color    The [DyeColor] that is currently being processed.
    /// @param holder   The [GroupedHolder.Nested] to add the translation for.
    /// @param suffix   The suffix of the translation.
    private void coloredWoodenBlockTranslation(BibliocraftWoodType woodType, DyeColor color, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, ?> holder, String suffix) {
        add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }

    /// Adds an English (en_us) translation to the given [LanguageProvider].
    ///
    /// @param woodType The [BibliocraftWoodType] that is currently being processed.
    /// @param color    The [DyeColor] that is currently being processed.
    /// @param holder   The [GroupedHolder.Nested] to add the translation for.
    /// @param suffix   The suffix of the translation.
    private void coloredWoodenItemTranslation(BibliocraftWoodType woodType, DyeColor color, GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder, String suffix) {
        add(holder.get(woodType, color), DatagenUtil.toTranslation(color.getName()) + " " + DatagenUtil.toTranslation(woodType.getPath()) + " " + suffix);
    }
}
