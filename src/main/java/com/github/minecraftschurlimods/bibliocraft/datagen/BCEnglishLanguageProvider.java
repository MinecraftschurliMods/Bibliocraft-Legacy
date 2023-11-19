package com.github.minecraftschurlimods.bibliocraft.datagen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class BCEnglishLanguageProvider extends LanguageProvider {
    public BCEnglishLanguageProvider(PackOutput output) {
        super(output, Bibliocraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(BCBlocks.BOOKCASE, "Bookcase");
        add("container", "bookcase", "Bookcase");
    }

    private void add(String type, String name, String translation) {
        add(type + "." + Bibliocraft.MOD_ID + "." + name, translation);
    }
}
