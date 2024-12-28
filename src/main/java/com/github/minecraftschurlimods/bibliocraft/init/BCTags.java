package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface BCTags {
    interface Blocks {
        TagKey<Block> BOOKCASES               = tag("bookcases");
        TagKey<Block> DISPLAY_CASES           = tag("display_cases");
        TagKey<Block> FANCY_ARMOR_STANDS_WOOD = tag("fancy_armor_stands/wood");
        TagKey<Block> FANCY_ARMOR_STANDS      = tag("fancy_armor_stands");
        TagKey<Block> FANCY_CLOCKS            = tag("fancy_clocks");
        TagKey<Block> FANCY_CRAFTERS          = tag("fancy_crafters");
        TagKey<Block> LABELS                  = tag("labels");
        TagKey<Block> POTION_SHELVES          = tag("potion_shelves");
        TagKey<Block> SEATS                   = tag("seats");
        TagKey<Block> SEAT_BACKS              = tag("seat_backs");
        TagKey<Block> SHELVES                 = tag("shelves");
        TagKey<Block> TABLES                  = tag("tables");
        TagKey<Block> TOOL_RACKS              = tag("tool_racks");
        TagKey<Block> FANCY_LAMPS             = tag("fancy_lamps");
        TagKey<Block> FANCY_LAMPS_GOLD        = tag("fancy_lamps/gold");
        TagKey<Block> FANCY_LAMPS_IRON        = tag("fancy_lamps/iron");
        TagKey<Block> FANCY_LANTERNS          = tag("fancy_lanterns");
        TagKey<Block> FANCY_LANTERNS_GOLD     = tag("fancy_lanterns/gold");
        TagKey<Block> FANCY_LANTERNS_IRON     = tag("fancy_lanterns/iron");

        /**
         * @param name The path of the tag.
         * @return A {@link TagKey<Block>} with this mod's namespace and the given path.
         */
        static TagKey<Block> tag(String name) {
            return TagKey.create(BuiltInRegistries.BLOCK.key(), BCUtil.modLoc(name));
        }
    }

    interface Items {
        TagKey<Item> BOOKCASES               = tag("bookcases");
        TagKey<Item> DISPLAY_CASES           = tag("display_cases");
        TagKey<Item> FANCY_ARMOR_STANDS_WOOD = tag("fancy_armor_stands/wood");
        TagKey<Item> FANCY_ARMOR_STANDS      = tag("fancy_armor_stands");
        TagKey<Item> FANCY_CLOCKS            = tag("fancy_clocks");
        TagKey<Item> FANCY_CRAFTERS          = tag("fancy_crafters");
        TagKey<Item> LABELS                  = tag("labels");
        TagKey<Item> POTION_SHELVES          = tag("potion_shelves");
        TagKey<Item> SEATS                   = tag("seats");
        TagKey<Item> SEAT_BACKS_SMALL        = tag("seat_backs/small");
        TagKey<Item> SEAT_BACKS_RAISED       = tag("seat_backs/raised");
        TagKey<Item> SEAT_BACKS_FLAT         = tag("seat_backs/flat");
        TagKey<Item> SEAT_BACKS_TALL         = tag("seat_backs/tall");
        TagKey<Item> SEAT_BACKS_FANCY        = tag("seat_backs/fancy");
        TagKey<Item> SEAT_BACKS              = tag("seat_backs");
        TagKey<Item> SHELVES                 = tag("shelves");
        TagKey<Item> TABLES                  = tag("tables");
        TagKey<Item> TOOL_RACKS              = tag("tool_racks");
        TagKey<Item> FANCY_LAMPS             = tag("fancy_lamps");
        TagKey<Item> FANCY_LAMPS_GOLD        = tag("fancy_lamps/gold");
        TagKey<Item> FANCY_LAMPS_IRON        = tag("fancy_lamps/iron");
        TagKey<Item> FANCY_LANTERNS          = tag("fancy_lanterns");
        TagKey<Item> FANCY_LANTERNS_GOLD     = tag("fancy_lanterns/gold");
        TagKey<Item> FANCY_LANTERNS_IRON     = tag("fancy_lanterns/iron");
        TagKey<Item> BOOKCASE_BOOKS          = tag("bookcase_books");
        TagKey<Item> COOKIE_JAR_COOKIES      = tag("cookie_jar_cookies");
        TagKey<Item> POTION_SHELF_POTIONS    = tag("potion_shelf_potions");
        TagKey<Item> SWORD_PEDESTAL_SWORDS   = tag("sword_pedestal_swords");
        TagKey<Item> TOOL_RACK_TOOLS         = tag("tool_rack_tools");

        /**
         * @param name The path of the tag.
         * @return A {@link TagKey<Item>} with this mod's namespace and the given path.
         */
        static TagKey<Item> tag(String name) {
            return TagKey.create(BuiltInRegistries.ITEM.key(), BCUtil.modLoc(name));
        }
    }
}
