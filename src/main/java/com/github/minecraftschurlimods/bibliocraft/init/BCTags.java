package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface BCTags {
    interface Items {
        TagKey<Item> BOOKCASE_BOOKS = tag("bookcase_books");

        static TagKey<Item> tag(String name) {
            return TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation(Bibliocraft.MOD_ID, name));
        }
    }
}
