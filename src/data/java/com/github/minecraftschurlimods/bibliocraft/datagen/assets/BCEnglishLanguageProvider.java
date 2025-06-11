package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMode;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSorting;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

@SuppressWarnings("SameParameterValue")
public class BCEnglishLanguageProvider extends LanguageProvider {
    public BCEnglishLanguageProvider(PackOutput output) {
        super(output, BibliocraftApi.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get(), "Fancy Gold Lamp");
        add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get(), "Fancy Iron Lamp");
        add(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get(), "Fancy Gold Lantern");
        add(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get(), "Fancy Iron Lantern");
        add(BCBlocks.CLEAR_TYPEWRITER.get(), "Typewriter");
        for (DyeColor color : DyeColor.values()) {
            addDefaultBlock(BCBlocks.FANCY_GOLD_LAMP.holder(color));
            addDefaultBlock(BCBlocks.FANCY_IRON_LAMP.holder(color));
            addDefaultBlock(BCBlocks.FANCY_GOLD_LANTERN.holder(color));
            addDefaultBlock(BCBlocks.FANCY_IRON_LANTERN.holder(color));
            addDefaultBlock(BCBlocks.TYPEWRITER.holder(color));
        }
        addDefaultBlock(BCBlocks.SOUL_FANCY_GOLD_LANTERN);
        addDefaultBlock(BCBlocks.SOUL_FANCY_IRON_LANTERN);
        addDefaultBlock(BCBlocks.CLIPBOARD);
        addDefaultBlock(BCBlocks.COOKIE_JAR);
        addDefaultBlock(BCBlocks.DESK_BELL);
        addDefaultBlock(BCBlocks.DINNER_PLATE);
        addDefaultBlock(BCBlocks.DISC_RACK);
        add(BCBlocks.WALL_DISC_RACK.get(), "Disc Rack");
        addDefaultBlock(BCBlocks.GOLD_CHAIN);
        addDefaultBlock(BCBlocks.GOLD_LANTERN);
        addDefaultBlock(BCBlocks.GOLD_SOUL_LANTERN);
        addDefaultBlock(BCBlocks.IRON_FANCY_ARMOR_STAND);
        addDefaultBlock(BCBlocks.IRON_PRINTING_TABLE);
        addDefaultBlock(BCBlocks.SWORD_PEDESTAL);
        addDefaultItem(BCItems.BIG_BOOK);
        add(BCItems.WRITTEN_BIG_BOOK.get(), "Big Book");
        add(BCItems.LOCK_AND_KEY.get(), "Lock and Key");
        addDefaultItem(BCItems.PLUMB_LINE);
        addDefaultItem(BCItems.REDSTONE_BOOK);
        addDefaultItem(BCItems.SLOTTED_BOOK);
        addDefaultItem(BCItems.STOCKROOM_CATALOG);
        addDefaultItem(BCItems.TAPE_MEASURE);
        addDefaultItem(BCItems.TAPE_REEL);
        addDefaultItem(BCItems.TYPEWRITER_PAGE);
        addSubtitle("clock.chime", "Clock chimes");
        addSubtitle("clock.tick", "Clock ticks");
        addSubtitle("clock.tock", "Clock tocks");
        addSubtitle("desk_bell", "Desk Bell rings");
        addSubtitle("display_case.close", "Display Case closes");
        addSubtitle("display_case.open", "Display Case opens");
        addSubtitle("tape_measure.close", "Tape Measure retracts");
        addSubtitle("tape_measure.open", "Tape Measure extends");
        addSubtitle("typewriter.add_paper", "Typewriter paper is added");
        addSubtitle("typewriter.chime", "Typewriter chimes");
        addSubtitle("typewriter.take_page", "Typewriter page is taken");
        addSubtitle("typewriter.type", "Typewriter types");
        addSubtitle("typewriter.typing", "Typewriter types");
        add(BCTags.Blocks.BOOKCASES, "Bookcases");
        add(BCTags.Blocks.DISPLAY_CASES, "Display Cases");
        add(BCTags.Blocks.FANCY_ARMOR_STANDS_WOOD, "Wooden Fancy Armor Stands");
        add(BCTags.Blocks.FANCY_ARMOR_STANDS, "Fancy Armor Stands");
        add(BCTags.Blocks.FANCY_CLOCKS, "Fancy Clocks");
        add(BCTags.Blocks.FANCY_SIGNS, "Fancy Signs");
        add(BCTags.Blocks.GRANDFATHER_CLOCKS, "Grandfather Clocks");
        add(BCTags.Blocks.LABELS, "Labels");
        add(BCTags.Blocks.POTION_SHELVES, "Potion Shelves");
        add(BCTags.Blocks.PRINTING_TABLES_WOOD, "Wooden Printing Tables");
        add(BCTags.Blocks.PRINTING_TABLES, "Printing Tables");
        add(BCTags.Blocks.SEATS, "Seats");
        add(BCTags.Blocks.SEAT_BACKS, "Seat Backs");
        add(BCTags.Blocks.SHELVES, "Shelves");
        add(BCTags.Blocks.TABLES, "Tables");
        add(BCTags.Blocks.TOOL_RACKS, "Tool Racks");
        add(BCTags.Blocks.FANCY_LAMPS, "Fancy Lamps");
        add(BCTags.Blocks.FANCY_LAMPS_GOLD, "Gold Fancy Lamps");
        add(BCTags.Blocks.FANCY_LAMPS_IRON, "Iron Fancy Lamps");
        add(BCTags.Blocks.FANCY_LANTERNS, "Fancy Lanterns");
        add(BCTags.Blocks.FANCY_LANTERNS_GOLD, "Gold Fancy Lanterns");
        add(BCTags.Blocks.FANCY_LANTERNS_IRON, "Iron Fancy Lanterns");
        add(BCTags.Blocks.TYPEWRITERS, "Typewriters");
        add(BCTags.Items.BOOKCASES, "Bookcases");
        add(BCTags.Items.DISPLAY_CASES, "Display Cases");
        add(BCTags.Items.FANCY_ARMOR_STANDS_WOOD, "Wooden Fancy Armor Stands");
        add(BCTags.Items.FANCY_ARMOR_STANDS, "Fancy Armor Stands");
        add(BCTags.Items.FANCY_CLOCKS, "Fancy Clocks");
        add(BCTags.Items.FANCY_SIGNS, "Fancy Signs");
        add(BCTags.Items.GRANDFATHER_CLOCKS, "Grandfather Clocks");
        add(BCTags.Items.LABELS, "Labels");
        add(BCTags.Items.POTION_SHELVES, "Potion Shelves");
        add(BCTags.Items.PRINTING_TABLES_WOOD, "Wooden Printing Tables");
        add(BCTags.Items.PRINTING_TABLES, "Printing Tables");
        add(BCTags.Items.SEATS, "Seats");
        add(BCTags.Items.SEAT_BACKS, "Seat Backs");
        add(BCTags.Items.SHELVES, "Shelves");
        add(BCTags.Items.TABLES, "Tables");
        add(BCTags.Items.TOOL_RACKS, "Tool Racks");
        add(BCTags.Items.FANCY_LAMPS, "Fancy Lamps");
        add(BCTags.Items.FANCY_LAMPS_GOLD, "Gold Fancy Lamps");
        add(BCTags.Items.FANCY_LAMPS_IRON, "Iron Fancy Lamps");
        add(BCTags.Items.FANCY_LANTERNS, "Fancy Lanterns");
        add(BCTags.Items.FANCY_LANTERNS_GOLD, "Gold Fancy Lanterns");
        add(BCTags.Items.FANCY_LANTERNS_IRON, "Iron Fancy Lanterns");
        add(BCTags.Items.TYPEWRITERS, "Typewriters");
        add(BCTags.Items.BOOKCASE_BOOKS, "Books for Bookcases");
        add(BCTags.Items.COOKIE_JAR_COOKIES, "Cookies for Cookie Jars");
        add(BCTags.Items.DISC_RACK_DISCS, "Discs for Disc Racks");
        add(BCTags.Items.FANCY_SIGN_WAX, "Wax for Fancy Signs");
        add(BCTags.Items.POTION_SHELF_POTIONS, "Potions for Potion Shelves");
        add(BCTags.Items.SWORD_PEDESTAL_SWORDS, "Swords for Sword Pedestals");
        add(BCTags.Items.TOOL_RACK_TOOLS, "Tools for Tool Racks");
        add(BCTags.Items.TYPEWRITER_PAPER, "Paper for Typewriters");
        add("container", "bookcase", "Bookcase");
        add("container", "cookie_jar", "Cookie Jar");
        add("container", "disc_rack", "Disc Rack");
        add("container", "fancy_armor_stand", "Armor Stand");
        add("container", "fancy_crafter", "Crafting");
        add("container", "label", "Label");
        add("container", "potion_shelf", "Potion Shelf");
        add("container", "printing_table", "Printing Table");
        add("container", "shelf", "Shelf");
        add("container", "tool_rack", "Tool Rack");
        add("itemGroup." + BibliocraftApi.MOD_ID, "Bibliocraft");
        add(FormattedLine.Mode.NORMAL.getTranslationKey(), "Normal");
        add(FormattedLine.Mode.SHADOW.getTranslationKey(), "Shadow");
        add(FormattedLine.Mode.GLOWING.getTranslationKey(), "Glowing");
        add(FormattedLine.Alignment.LEFT.getTranslationKey(), "Left");
        add(FormattedLine.Alignment.CENTER.getTranslationKey(), "Center");
        add(FormattedLine.Alignment.RIGHT.getTranslationKey(), "Right");
        add(PrintingTableMode.BIND.getTranslationKey(), "Bind");
        add(PrintingTableMode.CLONE.getTranslationKey(), "Clone");
        add(PrintingTableMode.MERGE.getTranslationKey(), "Merge");
        add(StockroomCatalogSorting.Container.ALPHABETICAL_ASC.getTranslationKey(), "A-Z");
        add(StockroomCatalogSorting.Container.ALPHABETICAL_DESC.getTranslationKey(), "Z-A");
        add(StockroomCatalogSorting.Container.DISTANCE_ASC.getTranslationKey(), "<-->");
        add(StockroomCatalogSorting.Container.DISTANCE_DESC.getTranslationKey(), "-><-");
        add(StockroomCatalogSorting.Item.ALPHABETICAL_ASC.getTranslationKey(), "A-Z");
        add(StockroomCatalogSorting.Item.ALPHABETICAL_DESC.getTranslationKey(), "Z-A");
        add(StockroomCatalogSorting.Item.COUNT_ASC.getTranslationKey(), "1-99");
        add(StockroomCatalogSorting.Item.COUNT_DESC.getTranslationKey(), "99-1");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic", "Cosmetics");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic.tooltip", "Contains cosmetic options.");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic.enable_pride", "Enable Pride Cosmetics");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic.enable_pride.tooltip", "Whether to enable pride-themed cosmetics during pride month or not.");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic.enable_pride_always", "Always Enable Pride Cosmetics");
        add("config." + BibliocraftApi.MOD_ID + ".cosmetic.enable_pride_always.tooltip", "Whether to enable pride-themed cosmetics all year or only during pride month. Does nothing if enable_pride is false.");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility", "Compatibility");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.tooltip", "Contains compatibility options.");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei", "JEI");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.tooltip", "Contains compatibility options for the JEI mod.");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_wood_types", "Show Wood Types");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_wood_types.tooltip", "Whether to show blocks for all wood types in JEI, or just the default oak.");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_color_types", "Show Color Types");
        add("config." + BibliocraftApi.MOD_ID + ".compatibility.jei.show_color_types.tooltip", "Whether to show blocks for all color types in JEI, or just the default white.");
        BCUtil.getChatFormattingColors().forEach(color -> add("color." + color.getName(), DatagenUtil.toTranslation(color.getName())));
        add(Translations.TYPEWRITER_NO_PAPER_KEY, "You must insert paper before you can start typing!");
        add(Translations.CLOCK_ADD_TRIGGER_KEY, "Add Trigger");
        add(Translations.CLOCK_DELETE_TRIGGER_KEY, "Delete Trigger");
        add(Translations.CLOCK_EDIT_TRIGGER_KEY, "Edit Trigger");
        add(Translations.CLOCK_EMIT_REDSTONE_KEY, "Emit Redstone");
        add(Translations.CLOCK_EMIT_SOUND_KEY, "Emit Sound");
        add(Translations.CLOCK_HOURS_KEY, "Hours");
        add(Translations.CLOCK_HOURS_HINT_KEY, "hh");
        add(Translations.CLOCK_MINUTES_KEY, "Minutes");
        add(Translations.CLOCK_MINUTES_HINT_KEY, "mm");
        add(Translations.CLOCK_TICK_KEY, "Enable Ticking Sound");
        add(Translations.CLOCK_TIME_KEY, "Time:");
        add(Translations.CLOCK_TIME_SEPARATOR_KEY, ":");
        add(Translations.CLOCK_TITLE_KEY, "Clock");
        add(Translations.CLOCK_TRIGGERS_KEY, "Triggers");
        add(Translations.FANCY_SIGN_TITLE_KEY, "Fancy Sign");
        add(Translations.FANCY_TEXT_AREA_ALIGNMENT_KEY, "Toggle Alignment");
        add(Translations.FANCY_TEXT_AREA_BOLD_KEY, "Bold");
        add(Translations.FANCY_TEXT_AREA_BOLD_SHORT_KEY, "B");
        add(Translations.FANCY_TEXT_AREA_COLOR_HINT_KEY, "#RRGGBB");
        add(Translations.FANCY_TEXT_AREA_ITALIC_KEY, "Italic");
        add(Translations.FANCY_TEXT_AREA_ITALIC_SHORT_KEY, "I");
        add(Translations.FANCY_TEXT_AREA_MODE_KEY, "Toggle Shadow/Glowing");
        add(Translations.FANCY_TEXT_AREA_NARRATION_KEY, "Text Area");
        add(Translations.FANCY_TEXT_AREA_OBFUSCATED_KEY, "Obfuscated");
        add(Translations.FANCY_TEXT_AREA_OBFUSCATED_SHORT_KEY, "O");
        add(Translations.FANCY_TEXT_AREA_SCALE_DOWN_KEY, "-");
        add(Translations.FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP_KEY, "Scale Down");
        add(Translations.FANCY_TEXT_AREA_SCALE_UP_KEY, "+");
        add(Translations.FANCY_TEXT_AREA_SCALE_UP_TOOLTIP_KEY, "Scale Up");
        add(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_KEY, "Strikethrough");
        add(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_SHORT_KEY, "S");
        add(Translations.FANCY_TEXT_AREA_UNDERLINED_KEY, "Underlined");
        add(Translations.FANCY_TEXT_AREA_UNDERLINED_SHORT_KEY, "U");
        add(Translations.PRINTING_TABLE_MODE_KEY, "Mode: %s");
        add(Translations.STOCKROOM_CATALOG_LOCATE_KEY, "Locate");
        add(Translations.STOCKROOM_CATALOG_REMOVE_KEY, "Remove");
        add(Translations.STOCKROOM_CATALOG_SEARCH_KEY, "Search");
        add(Translations.STOCKROOM_CATALOG_SHOW_CONTAINERS_KEY, "Show Containers");
        add(Translations.STOCKROOM_CATALOG_SHOW_ITEMS_KEY, "Show Items");
        add(Translations.TYPEWRITER_TITLE_KEY, "Typewriter");
        add(Translations.LOCK_AND_KEY_NO_CUSTOM_NAME_KEY, "You must rename this lock and key before you can apply it to a block!");
        add(Translations.REDSTONE_BOOK_TEXT_KEY, "When putting this book into a bookcase, the bookcase will emit a redstone signal. The strength of the signal depends on the slot it is placed in. Slot 1 (top left) yields no signal. Slot 2 yields a signal strength of 1. Each slot after that increases the signal strength by one, all the way to slot 16 (bottom right), which yields a signal strength of 15.");
        add(Translations.REDSTONE_BOOK_TITLE_KEY, "Redstone: Volume 1");
        add(Translations.SLOTTED_BOOK_TEXT_KEY, "This book can be used to hide your valuables among many books.");
        add(Translations.ALL_COLORS_KEY, "This block can be crafted in all colors.");
        add(Translations.ALL_COLORS_AND_WOOD_TYPES_KEY, "This block can be crafted in all colors and wood types.");
        add(Translations.ALL_WOOD_TYPES_KEY, "This block can be crafted in all wood types.");
        add(Translations.STOCKROOM_CATALOG_COUNT_KEY, "x%s");
        add(Translations.STOCKROOM_CATALOG_DISTANCE_KEY, "%s blocks away");
        add(Translations.STOCKROOM_CATALOG_SORT_KEY, "Sort: %s");
        add(Translations.LOCK_AND_KEY_LOCKED_KEY, "Successfully locked %s!");
        add(Translations.LOCK_AND_KEY_UNLOCKED_KEY, "Successfully unlocked %s!");
        add(Translations.PLUMB_LINE_DISTANCE_KEY, "Depth: %s");
        add(Translations.STOCKROOM_CATALOG_ADD_CONTAINER_KEY, "Started listing contents of %s in the Stockroom Catalog!");
        add(Translations.STOCKROOM_CATALOG_REMOVE_CONTAINER_KEY, "Stopped listing contents of %s in the Stockroom Catalog!");
        add(Translations.TAPE_MEASURE_DISTANCE_KEY, "Distance: %s blocks (x: %s, y: %s, z: %s)");
    }

    /**
     * Adds a translation with a translation key of the format "<type>.bibliocraft.<name>".
     *
     * @param type        The <type> part to use.
     * @param name        The <name> part to use.
     * @param translation The translated string.
     */
    private void add(String type, String name, String translation) {
        add(type + "." + BibliocraftApi.MOD_ID + "." + name, translation);
    }

    /**
     * Adds an automatically-generated translation for a block.
     *
     * @param block The block to add the translation for.
     */
    private void addDefaultBlock(DeferredHolder<Block, ?> block) {
        add(block.get(), DatagenUtil.toTranslation(block.getId().getPath()));
    }

    /**
     * Adds an automatically-generated translation for a item.
     *
     * @param item The item to add the translation for.
     */
    private void addDefaultItem(DeferredHolder<Item, ?> item) {
        add(item.get(), DatagenUtil.toTranslation(item.getId().getPath()));
    }

    /**
     * Adds a translation for a sound's subtitle.
     *
     * @param subtitle    The subtitle key to use.
     * @param translation The translation to use.
     */
    private void addSubtitle(String subtitle, String translation) {
        add("subtitles", subtitle, translation);
    }
}
