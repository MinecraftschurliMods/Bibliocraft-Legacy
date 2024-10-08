package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
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
        BibliocraftApi.getDatagenHelper().generateEnglishTranslations(this);
        add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get(), "Fancy Gold Lamp");
        add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get(), "Fancy Iron Lamp");
        add(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get(), "Fancy Gold Lantern");
        add(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get(), "Fancy Iron Lantern");
        for (DyeColor color : DyeColor.values()) {
            addDefaultBlock(BCBlocks.FANCY_GOLD_LAMP.holder(color));
            addDefaultBlock(BCBlocks.FANCY_IRON_LAMP.holder(color));
            addDefaultBlock(BCBlocks.FANCY_GOLD_LANTERN.holder(color));
            addDefaultBlock(BCBlocks.FANCY_IRON_LANTERN.holder(color));
        }
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
        addDefaultBlock(BCBlocks.SWORD_PEDESTAL);
        add(BCItems.LOCK_AND_KEY.get(), "Lock and Key");
        addDefaultItem(BCItems.PLUMB_LINE);
        add(BCItems.REDSTONE_BOOK.get(), "Redstone: Volume 1");
        add("container", "bookcase", "Bookcase");
        add("container", "cookie_jar", "Cookie Jar");
        add("container", "disc_rack", "Disc Rack");
        add("container", "fancy_armor_stand", "Armor Stand");
        add("container", "label", "Label");
        add("container", "potion_shelf", "Potion Shelf");
        add("container", "shelf", "Shelf");
        add("container", "tool_rack", "Tool Rack");
        add("itemGroup." + BibliocraftApi.MOD_ID, "Bibliocraft");
        add(Translations.LOCK_AND_KEY_LOCKED, "Successfully locked %s!");
        add(Translations.LOCK_AND_KEY_UNLOCKED, "Successfully unlocked %s!");
        add(Translations.LOCK_AND_KEY_NO_CUSTOM_NAME, "You must rename this lock and key before you can apply it to a block!");
        add(Translations.PLUMB_LINE_DISTANCE, "Depth: %s");
        add(Translations.REDSTONE_BOOK_TITLE, "Redstone: Volume 1");
        add(Translations.REDSTONE_BOOK_TEXT, "When putting this book into a bookcase, the bookcase will emit a redstone signal. The strength of the signal depends on the slot it is placed in. Slot 1 (top left) yields no signal. Slot 2 yields a signal strength of 1. Each slot after that increases the signal strength by one, all the way to slot 16 (bottom right), which yields a signal strength of 15.");
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
}
