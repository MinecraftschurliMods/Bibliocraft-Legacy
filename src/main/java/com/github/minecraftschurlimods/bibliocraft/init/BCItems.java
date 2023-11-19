package com.github.minecraftschurlimods.bibliocraft.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BCItems {
    Item.Properties PROPERTIES = new Item.Properties();

    Supplier<BlockItem> BOOKCASE = blockItem("bookcase", BCBlocks.BOOKCASE);
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

    static Supplier<BlockItem> blockItem(String name, Supplier<? extends Block> block) {
        return BCRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), PROPERTIES));
    }

    static void init() {}
}
