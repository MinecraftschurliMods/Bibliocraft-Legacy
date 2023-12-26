package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BCItems {
    Item.Properties PROPERTIES = new Item.Properties();

    WoodTypeDeferredHolder<Item, BlockItem>           BOOKCASE          = woodenBlock("bookcase",     BCBlocks.BOOKCASE);
    WoodTypeDeferredHolder<Item, DoubleHighBlockItem> FANCY_ARMOR_STAND = new WoodTypeDeferredHolder<>(BCRegistries.ITEMS, "fancy_armor_stand", BCBlocks.WOOD_TYPES, wood -> new DoubleHighBlockItem(BCBlocks.FANCY_ARMOR_STAND.get(wood), PROPERTIES));
    WoodTypeDeferredHolder<Item, BlockItem>           POTION_SHELF      = woodenBlock("potion_shelf", BCBlocks.POTION_SHELF);
    WoodTypeDeferredHolder<Item, BlockItem>           SHELF             = woodenBlock("shelf",        BCBlocks.SHELF);
    WoodTypeDeferredHolder<Item, BlockItem>           TOOL_RACK         = woodenBlock("tool_rack",    BCBlocks.TOOL_RACK);
    Supplier<DoubleHighBlockItem> IRON_FANCY_ARMOR_STAND = BCRegistries.ITEMS.register("iron_fancy_armor_stand", () -> new DoubleHighBlockItem(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), PROPERTIES));
    Supplier<BlockItem>           SWORD_PEDESTAL         = BCRegistries.ITEMS.register("sword_pedestal",         () -> new BlockItem(BCBlocks.SWORD_PEDESTAL.get(), PROPERTIES));
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

    /**
     * Helper method to register a {@code WoodTypeDeferredHolder<Item, BlockItem>} for a {@code WoodTypeDeferredHolder<Block, ?>}.
     *
     * @param name  The name of the {@link WoodTypeDeferredHolder}.
     * @param block The {@code WoodTypeDeferredHolder<Block, ?>} to use as a base.
     * @return A {@code WoodTypeDeferredHolder<Item, BlockItem>} that represents the blocks in the given {@code WoodTypeDeferredHolder<Block, ?>}.
     */
    static WoodTypeDeferredHolder<Item, BlockItem> woodenBlock(String name, WoodTypeDeferredHolder<Block, ?> block) {
        return new WoodTypeDeferredHolder<>(BCRegistries.ITEMS, name, BCBlocks.WOOD_TYPES, wood -> new BlockItem(block.get(wood), PROPERTIES));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
