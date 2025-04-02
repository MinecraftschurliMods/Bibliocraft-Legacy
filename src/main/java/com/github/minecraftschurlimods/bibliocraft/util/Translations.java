package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * Utility class holding various translation keys and components.
 */
public interface Translations {
    String CLOCK_ADD_TRIGGER_KEY                   = "gui."  + BibliocraftApi.MOD_ID + ".clock.add_trigger";
    String CLOCK_DELETE_TRIGGER_KEY                = "gui."  + BibliocraftApi.MOD_ID + ".clock.delete_trigger";
    String CLOCK_EDIT_TRIGGER_KEY                  = "gui."  + BibliocraftApi.MOD_ID + ".clock.edit_trigger";
    String CLOCK_EMIT_REDSTONE_KEY                 = "gui."  + BibliocraftApi.MOD_ID + ".clock.emit_redstone";
    String CLOCK_EMIT_SOUND_KEY                    = "gui."  + BibliocraftApi.MOD_ID + ".clock.emit_sound";
    String CLOCK_HOURS_KEY                         = "gui."  + BibliocraftApi.MOD_ID + ".clock.hours";
    String CLOCK_HOURS_HINT_KEY                    = "gui."  + BibliocraftApi.MOD_ID + ".clock.hours_hint";
    String CLOCK_MINUTES_KEY                       = "gui."  + BibliocraftApi.MOD_ID + ".clock.minutes";
    String CLOCK_MINUTES_HINT_KEY                  = "gui."  + BibliocraftApi.MOD_ID + ".clock.minutes_hint";
    String CLOCK_TICK_KEY                          = "gui."  + BibliocraftApi.MOD_ID + ".clock.tick";
    String CLOCK_TIME_KEY                          = "gui."  + BibliocraftApi.MOD_ID + ".clock.time";
    String CLOCK_TIME_SEPARATOR_KEY                = "gui."  + BibliocraftApi.MOD_ID + ".clock.time_separator";
    String CLOCK_TITLE_KEY                         = "gui."  + BibliocraftApi.MOD_ID + ".clock.title";
    String CLOCK_TRIGGERS_KEY                      = "gui."  + BibliocraftApi.MOD_ID + ".clock.triggers";
    String FANCY_TEXT_AREA_ALIGNMENT_KEY           = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.alignment";
    String FANCY_TEXT_AREA_BOLD_KEY                = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.bold";
    String FANCY_TEXT_AREA_COLOR_HINT_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.color_hint";
    String FANCY_TEXT_AREA_ITALIC_KEY              = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.italic";
    String FANCY_TEXT_AREA_MODE_KEY                = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.mode";
    String FANCY_TEXT_AREA_OBFUSCATED_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.obfuscated";
    String FANCY_TEXT_AREA_SCALE_DOWN_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.scale_down";
    String FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP_KEY  = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.scale_down.tooltip";
    String FANCY_TEXT_AREA_SCALE_UP_KEY            = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.scale_up";
    String FANCY_TEXT_AREA_SCALE_UP_TOOLTIP_KEY    = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.scale_up.tooltip";
    String FANCY_TEXT_AREA_STRIKETHROUGH_KEY       = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.strikethrough";
    String FANCY_TEXT_AREA_UNDERLINED_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.underlined";
    String STOCKROOM_CATALOG_LOCATE_KEY            = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.locate";
    String STOCKROOM_CATALOG_REMOVE_KEY            = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.remove";
    String STOCKROOM_CATALOG_SEARCH_KEY            = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.search";
    String STOCKROOM_CATALOG_SHOW_CONTAINERS_KEY   = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.show_containers";
    String STOCKROOM_CATALOG_SHOW_ITEMS_KEY        = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.show_items";
    String LOCK_AND_KEY_NO_CUSTOM_NAME_KEY         = "item." + BibliocraftApi.MOD_ID + ".lock_and_key.no_custom_name";
    String REDSTONE_BOOK_TEXT_KEY                  = "item." + BibliocraftApi.MOD_ID + ".redstone_book.text";
    String REDSTONE_BOOK_TITLE_KEY                 = "item." + BibliocraftApi.MOD_ID + ".redstone_book.title";
    String SLOTTED_BOOK_TEXT_KEY                   = "item." + BibliocraftApi.MOD_ID + ".slotted_book.text";
    String SLOTTED_BOOK_TITLE_KEY                  = "item." + BibliocraftApi.MOD_ID + ".slotted_book.title";
    String ALL_COLORS_KEY                          = "jei."  + BibliocraftApi.MOD_ID + ".all_colors";
    String ALL_COLORS_AND_WOOD_TYPES_KEY           = "jei."  + BibliocraftApi.MOD_ID + ".all_colors_and_wood_types";
    String ALL_WOOD_TYPES_KEY                      = "jei."  + BibliocraftApi.MOD_ID + ".all_wood_types";
    String FANCY_TEXT_AREA_BOLD_SHORT_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.bold.short";
    String FANCY_TEXT_AREA_ITALIC_SHORT_KEY        = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.italic.short";
    String FANCY_TEXT_AREA_OBFUSCATED_SHORT_KEY    = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.obfuscated.short";
    String FANCY_TEXT_AREA_STRIKETHROUGH_SHORT_KEY = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.strikethrough.short";
    String FANCY_TEXT_AREA_UNDERLINED_SHORT_KEY    = "gui."  + BibliocraftApi.MOD_ID + ".fancy_text_area.underlined.short";

    Component CLOCK_ADD_TRIGGER                    = Component.translatable(CLOCK_ADD_TRIGGER_KEY);
    Component CLOCK_DELETE_TRIGGER                 = Component.translatable(CLOCK_DELETE_TRIGGER_KEY);
    Component CLOCK_EDIT_TRIGGER                   = Component.translatable(CLOCK_EDIT_TRIGGER_KEY);
    Component CLOCK_EMIT_REDSTONE                  = Component.translatable(CLOCK_EMIT_REDSTONE_KEY);
    Component CLOCK_EMIT_SOUND                     = Component.translatable(CLOCK_EMIT_SOUND_KEY);
    Component CLOCK_HOURS                          = Component.translatable(CLOCK_HOURS_KEY);
    Component CLOCK_HOURS_HINT                     = Component.translatable(CLOCK_HOURS_HINT_KEY);
    Component CLOCK_MINUTES                        = Component.translatable(CLOCK_MINUTES_KEY);
    Component CLOCK_MINUTES_HINT                   = Component.translatable(CLOCK_MINUTES_HINT_KEY);
    Component CLOCK_TICK                           = Component.translatable(CLOCK_TICK_KEY);
    Component CLOCK_TIME                           = Component.translatable(CLOCK_TIME_KEY);
    Component CLOCK_TIME_SEPARATOR                 = Component.translatable(CLOCK_TIME_SEPARATOR_KEY);
    Component CLOCK_TITLE                          = Component.translatable(CLOCK_TITLE_KEY);
    Component CLOCK_TRIGGERS                       = Component.translatable(CLOCK_TRIGGERS_KEY);
    Component FANCY_TEXT_AREA_ALIGNMENT            = Component.translatable(FANCY_TEXT_AREA_ALIGNMENT_KEY);
    Component FANCY_TEXT_AREA_BOLD                 = Component.translatable(FANCY_TEXT_AREA_BOLD_KEY);
    Component FANCY_TEXT_AREA_COLOR_HINT           = Component.translatable(FANCY_TEXT_AREA_COLOR_HINT_KEY);
    Component FANCY_TEXT_AREA_ITALIC               = Component.translatable(FANCY_TEXT_AREA_ITALIC_KEY);
    Component FANCY_TEXT_AREA_MODE                 = Component.translatable(FANCY_TEXT_AREA_MODE_KEY);
    Component FANCY_TEXT_AREA_OBFUSCATED           = Component.translatable(FANCY_TEXT_AREA_OBFUSCATED_KEY);
    Component FANCY_TEXT_AREA_SCALE_DOWN           = Component.translatable(FANCY_TEXT_AREA_SCALE_DOWN_KEY);
    Component FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP   = Component.translatable(FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP_KEY);
    Component FANCY_TEXT_AREA_SCALE_UP             = Component.translatable(FANCY_TEXT_AREA_SCALE_UP_KEY);
    Component FANCY_TEXT_AREA_SCALE_UP_TOOLTIP     = Component.translatable(FANCY_TEXT_AREA_SCALE_UP_TOOLTIP_KEY);
    Component FANCY_TEXT_AREA_STRIKETHROUGH        = Component.translatable(FANCY_TEXT_AREA_STRIKETHROUGH_KEY);
    Component FANCY_TEXT_AREA_UNDERLINED           = Component.translatable(FANCY_TEXT_AREA_UNDERLINED_KEY);
    Component FANCY_TEXT_AREA_BOLD_SHORT           = Component.translatable(FANCY_TEXT_AREA_BOLD_SHORT_KEY).withStyle(ChatFormatting.BOLD);
    Component FANCY_TEXT_AREA_ITALIC_SHORT         = Component.translatable(FANCY_TEXT_AREA_ITALIC_SHORT_KEY).withStyle(ChatFormatting.ITALIC);
    Component FANCY_TEXT_AREA_OBFUSCATED_SHORT     = Component.translatable(FANCY_TEXT_AREA_OBFUSCATED_SHORT_KEY).withStyle(ChatFormatting.OBFUSCATED);
    Component FANCY_TEXT_AREA_STRIKETHROUGH_SHORT  = Component.translatable(FANCY_TEXT_AREA_STRIKETHROUGH_SHORT_KEY).withStyle(ChatFormatting.STRIKETHROUGH);
    Component FANCY_TEXT_AREA_UNDERLINED_SHORT     = Component.translatable(FANCY_TEXT_AREA_UNDERLINED_SHORT_KEY).withStyle(ChatFormatting.UNDERLINE);

    String STOCKROOM_CATALOG_COUNT_KEY             = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.count";
    String STOCKROOM_CATALOG_DISTANCE_KEY          = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.distance";
    String STOCKROOM_CATALOG_SORT_KEY              = "gui."  + BibliocraftApi.MOD_ID + ".stockroom_catalog.sort";
    String LOCK_AND_KEY_LOCKED_KEY                 = "item." + BibliocraftApi.MOD_ID + ".lock_and_key.locked";
    String LOCK_AND_KEY_UNLOCKED_KEY               = "item." + BibliocraftApi.MOD_ID + ".lock_and_key.unlocked";
    String PLUMB_LINE_DISTANCE_KEY                 = "item." + BibliocraftApi.MOD_ID + ".plumb_line.distance";
    String STOCKROOM_CATALOG_ADD_CONTAINER_KEY     = "item." + BibliocraftApi.MOD_ID + ".stockroom_catalog.add_container";
    String STOCKROOM_CATALOG_REMOVE_CONTAINER_KEY  = "item." + BibliocraftApi.MOD_ID + ".stockroom_catalog.remove_container";
    String TAPE_MEASURE_DISTANCE_KEY               = "item." + BibliocraftApi.MOD_ID + ".tape_measure.distance";
}
