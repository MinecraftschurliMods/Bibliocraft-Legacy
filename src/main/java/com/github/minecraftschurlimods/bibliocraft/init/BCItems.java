package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BCItems {
    Item.Properties PROPERTIES = new Item.Properties();

    WoodTypeDeferredHolder<Item, BlockItem> BOOKCASE     = woodenBlock("bookcase",     BCBlocks.BOOKCASE);
    WoodTypeDeferredHolder<Item, BlockItem> POTION_SHELF = woodenBlock("potion_shelf", BCBlocks.POTION_SHELF);
    WoodTypeDeferredHolder<Item, BlockItem> SHELF        = woodenBlock("shelf",        BCBlocks.SHELF);
    WoodTypeDeferredHolder<Item, BlockItem> TOOL_RACK    = woodenBlock("tool_rack",    BCBlocks.TOOL_RACK);
    //TODO Atlas
    //TODO Big Book
    //TODO Clipboard
    //TODO Drafting Compass
    //TODO Eternal Compass
    //TODO Framing Board
    //TODO Framing Saw
    //TODO Framing Sheet
    //TODO Hand Drill
    //TODO Lock and Key
    //TODO Monocle
    //TODO Painting Canvas
    //TODO Plumb Line
    //TODO Print Press Chase
    //TODO Print Press Plate
    //TODO Reading Glasses
    //TODO Recipe Book
    //TODO Redstone Book
    //TODO Seat Back
    //TODO Screw Gun
    //TODO Slotted Book
    //TODO Stockroom Catalog
    //TODO Tape Measure
    //TODO Tinted Glasses
    //TODO Waypoint Compass

    static WoodTypeDeferredHolder<Item, BlockItem> woodenBlock(String name, WoodTypeDeferredHolder<Block, ?> block) {
        return new WoodTypeDeferredHolder<>(BCRegistries.ITEMS, name, BCBlocks.WOOD_TYPES, wood -> new BlockItem(block.holder(wood).get(), PROPERTIES));
    }

    static Supplier<BlockItem> blockItem(String name, Supplier<? extends Block> block) {
        return BCRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), PROPERTIES));
    }

    static void init() {}
}
