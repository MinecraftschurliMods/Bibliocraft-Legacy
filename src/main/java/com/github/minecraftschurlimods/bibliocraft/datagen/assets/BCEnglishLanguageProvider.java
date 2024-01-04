package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class BCEnglishLanguageProvider extends LanguageProvider {
    private static final Map<WoodType, String> WOOD_TYPE_NAMES = Util.make(new HashMap<>(), map -> {
        map.put(WoodType.OAK, "Oak");
        map.put(WoodType.SPRUCE, "Spruce");
        map.put(WoodType.BIRCH, "Birch");
        map.put(WoodType.JUNGLE, "Jungle");
        map.put(WoodType.ACACIA, "Acacia");
        map.put(WoodType.DARK_OAK, "Dark Oak");
        map.put(WoodType.CRIMSON, "Crimson");
        map.put(WoodType.WARPED, "Warped");
        map.put(WoodType.MANGROVE, "Mangrove");
        map.put(WoodType.BAMBOO, "Bamboo");
        map.put(WoodType.CHERRY, "Cherry");
    });
    private static final Map<DyeColor, String> COLOR_NAMES = Util.make(new HashMap<>(), map -> {
        map.put(DyeColor.BLACK, "Black");
        map.put(DyeColor.BLUE, "Blue");
        map.put(DyeColor.BROWN, "Brown");
        map.put(DyeColor.CYAN, "Cyan");
        map.put(DyeColor.GRAY, "Gray");
        map.put(DyeColor.GREEN, "Green");
        map.put(DyeColor.LIGHT_BLUE, "Light Blue");
        map.put(DyeColor.LIGHT_GRAY, "Light Gray");
        map.put(DyeColor.LIME, "Lime");
        map.put(DyeColor.MAGENTA, "Magenta");
        map.put(DyeColor.ORANGE, "Orange");
        map.put(DyeColor.PINK, "Pink");
        map.put(DyeColor.PURPLE, "Purple");
        map.put(DyeColor.RED, "Red");
        map.put(DyeColor.WHITE, "White");
        map.put(DyeColor.YELLOW, "Yellow");
    });

    public BCEnglishLanguageProvider(PackOutput output) {
        super(output, Bibliocraft.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addWoodenBlock(BCBlocks.BOOKCASE, "Bookcase");
        addColoredWoodenBlock(BCBlocks.DISPLAY_CASE, "Display Case");
        addColoredWoodenBlock(BCBlocks.WALL_DISPLAY_CASE, "Display Case");
        addWoodenBlock(BCBlocks.FANCY_ARMOR_STAND, "Fancy Armor Stand");
        addWoodenBlock(BCBlocks.LABEL, "Label");
        addWoodenBlock(BCBlocks.POTION_SHELF, "Potion Shelf");
        addWoodenBlock(BCBlocks.SHELF, "Shelf");
        addWoodenBlock(BCBlocks.TOOL_RACK, "Tool Rack");
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

    /**
     * Adds translations for all variants of a {@link WoodTypeDeferredHolder}.
     *
     * @param block  The {@link WoodTypeDeferredHolder} to add the translations for.
     * @param suffix The suffix of the translated string.
     */
    private void addWoodenBlock(WoodTypeDeferredHolder<Block, ? extends Block> block, String suffix) {
        block.map().forEach((wood, holder) -> addBlock(holder, WOOD_TYPE_NAMES.get(wood) + " " + suffix));
    }

    /**
     * Adds translations for all variants of a {@link ColoredWoodTypeDeferredHolder}.
     *
     * @param block  The {@link ColoredWoodTypeDeferredHolder} to add the translations for.
     * @param suffix The suffix of the translated string.
     */
    private void addColoredWoodenBlock(ColoredWoodTypeDeferredHolder<Block, ? extends Block> block, String suffix) {
        block.map().forEach((wood, colorHolder) -> colorHolder.map().forEach((color, holder) -> addBlock(holder, COLOR_NAMES.get(color) + " " + WOOD_TYPE_NAMES.get(wood) + " " + suffix)));
    }
}
