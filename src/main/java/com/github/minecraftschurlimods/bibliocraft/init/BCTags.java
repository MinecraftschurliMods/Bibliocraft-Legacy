package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface BCTags {
    interface Blocks {
        TagKey<Block> BOOKCASES = tag("bookcases");
        TagKey<Block> POTION_SHELVES = tag("potion_shelves");

        static TagKey<Block> tag(String name) {
            return TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation(Bibliocraft.MOD_ID, name));
        }
    }

    interface Items {
        TagKey<Item> BOOKCASES = tag("bookcases");
        TagKey<Item> POTION_SHELVES = tag("potion_shelves");
        TagKey<Item> BOOKCASE_BOOKS = tag("bookcase_books");

        static TagKey<Item> tag(String name) {
            return TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation(Bibliocraft.MOD_ID, name));
        }
    }
}
