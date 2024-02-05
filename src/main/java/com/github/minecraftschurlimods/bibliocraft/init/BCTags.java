package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface BCTags {
    interface Blocks {
        TagKey<Block> BOOKCASES               = tag("bookcases");
        TagKey<Block> DISPLAY_CASES           = tag("display_cases");
        TagKey<Block> FANCY_ARMOR_STANDS_WOOD = tag("fancy_armor_stands/wood");
        TagKey<Block> FANCY_ARMOR_STANDS      = tag("fancy_armor_stands");
        TagKey<Block> LABELS                  = tag("labels");
        TagKey<Block> POTION_SHELVES          = tag("potion_shelves");
        TagKey<Block> SEATS                   = tag("seats");
        TagKey<Block> SEAT_BACKS              = tag("seat_backs");
        TagKey<Block> SHELVES                 = tag("shelves");
        TagKey<Block> TOOL_RACKS              = tag("tool_racks");

        /**
         * @param name The path of the tag.
         * @return A {@link TagKey<Block>} with this mod's namespace and the given path.
         */
        static TagKey<Block> tag(String name) {
            return TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation(Bibliocraft.MOD_ID, name));
        }
    }

    interface Items {
        TagKey<Item> BOOKCASES               = tag("bookcases");
        TagKey<Item> DISPLAY_CASES           = tag("display_cases");
        TagKey<Item> FANCY_ARMOR_STANDS_WOOD = tag("fancy_armor_stands/wood");
        TagKey<Item> FANCY_ARMOR_STANDS      = tag("fancy_armor_stands");
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
        TagKey<Item> TOOL_RACKS              = tag("tool_racks");
        TagKey<Item> BOOKCASE_BOOKS          = tag("bookcase_books");
        TagKey<Item> POTION_SHELF_POTIONS    = tag("potion_shelf_potions");
        TagKey<Item> SWORD_PEDESTAL_SWORDS   = tag("sword_pedestal_swords");
        TagKey<Item> TOOL_RACK_TOOLS         = tag("tool_rack_tools");

        /**
         * @param name The path of the tag.
         * @return A {@link TagKey<Item>} with this mod's namespace and the given path.
         */
        static TagKey<Item> tag(String name) {
            return TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation(Bibliocraft.MOD_ID, name));
        }
    }
}
