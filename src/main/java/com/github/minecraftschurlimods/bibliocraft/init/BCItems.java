package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardItem;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackItem;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseItem;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLightItem;
import com.github.minecraftschurlimods.bibliocraft.content.lockandkey.LockAndKeyItem;
import com.github.minecraftschurlimods.bibliocraft.content.plumbline.PlumbLineItem;
import com.github.minecraftschurlimods.bibliocraft.content.redstonebook.RedstoneBookItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.slottedbook.SlottedBookItem;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogItem;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.tapemeasure.TapeMeasureItem;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeBlockItem;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;

public interface BCItems {
    Item.Properties PROPERTIES = new Item.Properties();

    WoodTypeDeferredHolder<Item, BlockItem>           BOOKCASE          = woodenBlock("bookcase",      BCBlocks.BOOKCASE);
    WoodTypeDeferredHolder<Item, DoubleHighBlockItem> FANCY_ARMOR_STAND = new WoodTypeDeferredHolder<>(BCRegistries.ITEMS, "fancy_armor_stand", wood -> new DoubleHighBlockItem(BCBlocks.FANCY_ARMOR_STAND.get(wood), PROPERTIES));
    WoodTypeDeferredHolder<Item, BlockItem>           FANCY_CLOCK       = woodenBlock("fancy_clock",   BCBlocks.FANCY_CLOCK);
    WoodTypeDeferredHolder<Item, BlockItem>           FANCY_CRAFTER     = woodenBlock("fancy_crafter", BCBlocks.FANCY_CRAFTER);
    WoodTypeDeferredHolder<Item, BlockItem>           LABEL             = woodenBlock("label",         BCBlocks.LABEL);
    WoodTypeDeferredHolder<Item, BlockItem>           POTION_SHELF      = woodenBlock("potion_shelf",  BCBlocks.POTION_SHELF);
    WoodTypeDeferredHolder<Item, BlockItem>           SHELF             = woodenBlock("shelf",         BCBlocks.SHELF);
    WoodTypeDeferredHolder<Item, BlockItem>           TABLE             = woodenBlock("table",         BCBlocks.TABLE);
    WoodTypeDeferredHolder<Item, BlockItem>           TOOL_RACK         = woodenBlock("tool_rack",     BCBlocks.TOOL_RACK);
    ColoredWoodTypeDeferredHolder<Item, BlockItem>    DISPLAY_CASE      = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "display_case",     DisplayCaseItem::new);
    ColoredWoodTypeDeferredHolder<Item, BlockItem>    SEAT              = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "seat",             (wood, color) -> new ColoredWoodTypeBlockItem(BCBlocks.SEAT, wood, color));
    ColoredWoodTypeDeferredHolder<Item, SeatBackItem> SMALL_SEAT_BACK   = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "small_seat_back",  (wood, color) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.SMALL));
    ColoredWoodTypeDeferredHolder<Item, SeatBackItem> RAISED_SEAT_BACK  = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "raised_seat_back", (wood, color) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.RAISED));
    ColoredWoodTypeDeferredHolder<Item, SeatBackItem> FLAT_SEAT_BACK    = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "flat_seat_back",   (wood, color) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.FLAT));
    ColoredWoodTypeDeferredHolder<Item, SeatBackItem> TALL_SEAT_BACK    = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "tall_seat_back",   (wood, color) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.TALL));
    ColoredWoodTypeDeferredHolder<Item, SeatBackItem> FANCY_SEAT_BACK   = new ColoredWoodTypeDeferredHolder<>(BCRegistries.ITEMS, "fancy_seat_back",  (wood, color) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.FANCY));
    DeferredItem<FancyLightItem> CLEAR_FANCY_GOLD_LAMP             = BCRegistries.ITEMS.register(                    "fancy_gold_lamp",    () -> new FancyLightItem(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get()));
    ColoredDeferredHolder<Item, FancyLightItem> FANCY_GOLD_LAMP    = new ColoredDeferredHolder<>(BCRegistries.ITEMS, "fancy_gold_lamp",    color -> new FancyLightItem(BCBlocks.FANCY_GOLD_LAMP.get(color)));
    DeferredItem<FancyLightItem> CLEAR_FANCY_IRON_LAMP             = BCRegistries.ITEMS.register(                    "fancy_iron_lamp",    () -> new FancyLightItem(BCBlocks.CLEAR_FANCY_IRON_LAMP.get()));
    ColoredDeferredHolder<Item, FancyLightItem> FANCY_IRON_LAMP    = new ColoredDeferredHolder<>(BCRegistries.ITEMS, "fancy_iron_lamp",    color -> new FancyLightItem(BCBlocks.FANCY_IRON_LAMP.get(color)));
    DeferredItem<FancyLightItem> CLEAR_FANCY_GOLD_LANTERN          = BCRegistries.ITEMS.register(                    "fancy_gold_lantern", () -> new FancyLightItem(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get()));
    ColoredDeferredHolder<Item, FancyLightItem> FANCY_GOLD_LANTERN = new ColoredDeferredHolder<>(BCRegistries.ITEMS, "fancy_gold_lantern", color -> new FancyLightItem(BCBlocks.FANCY_GOLD_LANTERN.get(color)));
    DeferredItem<FancyLightItem> CLEAR_FANCY_IRON_LANTERN          = BCRegistries.ITEMS.register(                    "fancy_iron_lantern", () -> new FancyLightItem(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get()));
    ColoredDeferredHolder<Item, FancyLightItem> FANCY_IRON_LANTERN = new ColoredDeferredHolder<>(BCRegistries.ITEMS, "fancy_iron_lantern", color -> new FancyLightItem(BCBlocks.FANCY_IRON_LANTERN.get(color)));
    DeferredItem<ClipboardItem>        CLIPBOARD              = BCRegistries.ITEMS.register("clipboard", ClipboardItem::new);
    DeferredItem<BlockItem>            COOKIE_JAR             = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.COOKIE_JAR);
    DeferredItem<BlockItem>            DESK_BELL              = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.DESK_BELL);
    DeferredItem<BlockItem>            DINNER_PLATE           = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.DINNER_PLATE);
    DeferredItem<DiscRackItem>         DISC_RACK              = BCRegistries.ITEMS.registerItem("disc_rack", DiscRackItem::new);
    DeferredItem<BlockItem>            GOLD_CHAIN             = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.GOLD_CHAIN);
    DeferredItem<BlockItem>            GOLD_LANTERN           = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.GOLD_LANTERN);
    DeferredItem<BlockItem>            GOLD_SOUL_LANTERN      = BCRegistries.ITEMS.registerSimpleBlockItem(BCBlocks.GOLD_SOUL_LANTERN);
    DeferredItem<DoubleHighBlockItem>  IRON_FANCY_ARMOR_STAND = BCRegistries.ITEMS.register("iron_fancy_armor_stand", () -> new DoubleHighBlockItem(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), PROPERTIES));
    DeferredItem<BlockItem>            SWORD_PEDESTAL         = BCRegistries.ITEMS.register("sword_pedestal", () -> new BlockItem(BCBlocks.SWORD_PEDESTAL.get(), new Item.Properties().component(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR)));
    DeferredItem<LockAndKeyItem>       LOCK_AND_KEY           = BCRegistries.ITEMS.registerItem("lock_and_key", LockAndKeyItem::new);
    DeferredItem<PlumbLineItem>        PLUMB_LINE             = BCRegistries.ITEMS.registerItem("plumb_line", PlumbLineItem::new);
    DeferredItem<RedstoneBookItem>     REDSTONE_BOOK          = BCRegistries.ITEMS.registerItem("redstone_book", RedstoneBookItem::new);
    DeferredItem<SlottedBookItem>      SLOTTED_BOOK           = BCRegistries.ITEMS.registerItem("slotted_book", SlottedBookItem::new);
    DeferredItem<StockroomCatalogItem> STOCKROOM_CATALOG      = BCRegistries.ITEMS.registerItem("stockroom_catalog", StockroomCatalogItem::new);
    DeferredItem<TapeMeasureItem>      TAPE_MEASURE           = BCRegistries.ITEMS.registerItem("tape_measure", TapeMeasureItem::new);
    DeferredItem<Item>                 TAPE_REEL              = BCRegistries.ITEMS.registerSimpleItem("tape_reel");
    //TODO Big Book
    //TODO Hand Drill
    //TODO Screw Gun
    //TODO Monocle
    //TODO Reading Glasses
    //TODO Tinted Glasses
    //TODO Atlas
    //TODO Drafting Compass
    //TODO Painting Canvas
    //TODO Print Press Chase
    //TODO Print Press Plate
    //TODO Recipe Book

    /**
     * Helper method to register a {@code WoodTypeDeferredHolder<Item, BlockItem>} for a {@code WoodTypeDeferredHolder<Block, ?>}.
     *
     * @param name  The name of the {@link WoodTypeDeferredHolder}.
     * @param block The {@code WoodTypeDeferredHolder<Block, ?>} to use as a base.
     * @return A {@code WoodTypeDeferredHolder<Item, BlockItem>} that represents the blocks in the given {@code WoodTypeDeferredHolder<Block, ?>}.
     */
    static WoodTypeDeferredHolder<Item, BlockItem> woodenBlock(String name, WoodTypeDeferredHolder<Block, ?> block) {
        return new WoodTypeDeferredHolder<>(BCRegistries.ITEMS, name, wood -> new BlockItem(block.get(wood), PROPERTIES));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
