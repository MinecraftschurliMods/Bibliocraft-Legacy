package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class BCEnglishLanguageProvider extends LanguageProvider {
    private static final Map<WoodType, String> WOOD_TYPE_NAMES = Util.make(new HashMap<>(), map -> {
        map.put(WoodType.OAK,      "Oak");
        map.put(WoodType.SPRUCE,   "Spruce");
        map.put(WoodType.BIRCH,    "Birch");
        map.put(WoodType.JUNGLE,   "Jungle");
        map.put(WoodType.ACACIA,   "Acacia");
        map.put(WoodType.DARK_OAK, "Dark Oak");
        map.put(WoodType.CRIMSON,  "Crimson");
        map.put(WoodType.WARPED,   "Warped");
        map.put(WoodType.MANGROVE, "Mangrove");
        map.put(WoodType.BAMBOO,   "Bamboo");
        map.put(WoodType.CHERRY,   "Cherry");
    });

    public BCEnglishLanguageProvider(PackOutput output) {
        super(output, Bibliocraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addWoodenBlock(BCBlocks.BOOKCASE, "Bookcase");
        addWoodenBlock(BCBlocks.POTION_SHELF, "Potion Shelf");
        add("container", "bookcase", "Bookcase");
        add("container", "potion_shelf", "Potion Shelf");
        add("itemGroup." + Bibliocraft.MOD_ID, "Bibliocraft");
    }

    private void add(String type, String name, String translation) {
        add(type + "." + Bibliocraft.MOD_ID + "." + name, translation);
    }

    private void addWoodenBlock(WoodTypeDeferredHolder<Block, ? extends Block> block, String suffix) {
        block.map().forEach((k, v) -> addBlock(v, WOOD_TYPE_NAMES.get(k) + " " + suffix));
    }
}
