package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenAPI;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

@SuppressWarnings("SameParameterValue")
public class BCEnglishLanguageProvider extends LanguageProvider {
    public BCEnglishLanguageProvider(PackOutput output) {
        super(output, Bibliocraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        BibliocraftDatagenAPI.get().generateEnglishTranslations(this);
        add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), "Iron Fancy Armor Stand");
        add(BCBlocks.SWORD_PEDESTAL.get(), "Sword Pedestal");
        add(BCItems.REDSTONE_BOOK.get(), "Redstone: Volume 1");
        add("container", "bookcase", "Bookcase");
        add("container", "fancy_armor_stand", "Armor Stand");
        add("container", "label", "Label");
        add("container", "potion_shelf", "Potion Shelf");
        add("container", "shelf", "Shelf");
        add("container", "tool_rack", "Tool Rack");
        add("itemGroup." + Bibliocraft.MOD_ID, "Bibliocraft");
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
        add(type + "." + Bibliocraft.MOD_ID + "." + name, translation);
    }
}
