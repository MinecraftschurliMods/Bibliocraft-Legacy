package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookContent;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookItem;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.WrittenBigBookContent;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardItem;
import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockItem;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackItem;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseItem;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignItem;
import com.github.minecraftschurlimods.bibliocraft.content.lockandkey.LockAndKeyItem;
import com.github.minecraftschurlimods.bibliocraft.content.plumbline.PlumbLineItem;
import com.github.minecraftschurlimods.bibliocraft.content.redstonebook.RedstoneBookItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackItem;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackType;
import com.github.minecraftschurlimods.bibliocraft.content.slottedbook.SlottedBookItem;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogItem;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.tapemeasure.TapeMeasureItem;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPage;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPageItem;
import com.github.minecraftschurlimods.bibliocraft.util.block.ColoredWoodTypeBlockItem;
import com.github.minecraftschurlimods.bibliocraft.util.holder.CopperSet;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import com.mojang.datafixers.util.Function3;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface BCItems {
    List<GroupedHolder<BibliocraftWoodType, Item, ? extends Item>> WOODEN = new ArrayList<>();
    List<GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ? extends Item>> WOODEN_COLORED = new ArrayList<>();
    List<GroupedHolder<DyeColor, Item, ? extends Item>> COLORED = new ArrayList<>();
    List<DeferredItem<?>> OTHER = new ArrayList<>();

    // @formatter:off
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           BOOKCASE          = woodenBlock("bookcase",           BCBlocks.BOOKCASE);
    GroupedHolder<BibliocraftWoodType, Item, DoubleHighBlockItem> FANCY_ARMOR_STAND = woodenBlock("fancy_armor_stand",  DoubleHighBlockItem::new, BCBlocks.FANCY_ARMOR_STAND);
    GroupedHolder<BibliocraftWoodType, Item, FancyClockItem>      FANCY_CLOCK       = specialWoodenBlock("fancy_clock", FancyClockItem::new);
    GroupedHolder<BibliocraftWoodType, Item, FancySignItem>       FANCY_SIGN        = specialWoodenBlock("fancy_sign",  FancySignItem::new);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           FANCY_CRAFTER     = woodenBlock("fancy_crafter",      BCBlocks.FANCY_CRAFTER);
    GroupedHolder<BibliocraftWoodType, Item, DoubleHighBlockItem> GRANDFATHER_CLOCK = woodenBlock("grandfather_clock",  DoubleHighBlockItem::new, BCBlocks.GRANDFATHER_CLOCK);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           LABEL             = woodenBlock("label",              BCBlocks.LABEL);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           POTION_SHELF      = woodenBlock("potion_shelf",       BCBlocks.POTION_SHELF);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           SHELF             = woodenBlock("shelf",              BCBlocks.SHELF);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           TABLE             = woodenBlock("table",              BCBlocks.TABLE);
    GroupedHolder<BibliocraftWoodType, Item, BlockItem>           TOOL_RACK         = woodenBlock("tool_rack",          BCBlocks.TOOL_RACK);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, BlockItem>    DISPLAY_CASE      = coloredWoodItem("display_case",     DisplayCaseItem::new);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, BlockItem>    SEAT              = coloredWoodItem("seat",             (wood, color, p) -> new ColoredWoodTypeBlockItem(BCBlocks.SEAT, wood, color, p));
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> SMALL_SEAT_BACK   = coloredWoodItem("small_seat_back",  (wood, color, p) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.SMALL, p));
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> RAISED_SEAT_BACK  = coloredWoodItem("raised_seat_back", (wood, color, p) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.RAISED, p));
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> FLAT_SEAT_BACK    = coloredWoodItem("flat_seat_back",   (wood, color, p) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.FLAT, p));
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> TALL_SEAT_BACK    = coloredWoodItem("tall_seat_back",   (wood, color, p) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.TALL, p));
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, SeatBackItem> FANCY_SEAT_BACK   = coloredWoodItem("fancy_seat_back",  (wood, color, p) -> new SeatBackItem(BCBlocks.SEAT_BACK, wood, color, SeatBackType.FANCY, p));
    DeferredItem<BlockItem>            CLEAR_FANCY_GOLD_LAMP = registerBlockItem(BCBlocks.CLEAR_FANCY_GOLD_LAMP);
    GroupedHolder<DyeColor, Item, BlockItem> FANCY_GOLD_LAMP = coloredBlock(BCBlocks.FANCY_GOLD_LAMP);
    DeferredItem<BlockItem>            CLEAR_FANCY_IRON_LAMP = registerBlockItem(BCBlocks.CLEAR_FANCY_IRON_LAMP);
    GroupedHolder<DyeColor, Item, BlockItem> FANCY_IRON_LAMP = coloredBlock(BCBlocks.FANCY_IRON_LAMP);
    CopperSet<DeferredItem<BlockItem>, DeferredItem<BlockItem>> CLEAR_FANCY_COPPER_LAMP = CopperSet.forBlockItems(BCBlocks.CLEAR_FANCY_COPPER_LAMP, BCItems::registerBlockItem, BCItems::registerBlockItem);
    CopperSet<GroupedHolder<DyeColor, Item, BlockItem>, GroupedHolder<DyeColor, Item, BlockItem>> FANCY_COPPER_LAMP = CopperSet.forBlockItems(BCBlocks.FANCY_COPPER_LAMP, BCItems::coloredBlock, BCItems::coloredBlock);
    DeferredItem<BlockItem>            CLEAR_FANCY_GOLD_LANTERN = registerBlockItem(BCBlocks.CLEAR_FANCY_GOLD_LANTERN);
    GroupedHolder<DyeColor, Item, BlockItem> FANCY_GOLD_LANTERN = coloredBlock(BCBlocks.FANCY_GOLD_LANTERN);
    DeferredItem<BlockItem>             SOUL_FANCY_GOLD_LANTERN = registerBlockItem(BCBlocks.SOUL_FANCY_GOLD_LANTERN);
    DeferredItem<BlockItem>            CLEAR_FANCY_IRON_LANTERN = registerBlockItem(BCBlocks.CLEAR_FANCY_IRON_LANTERN);
    GroupedHolder<DyeColor, Item, BlockItem> FANCY_IRON_LANTERN = coloredBlock(BCBlocks.FANCY_IRON_LANTERN);
    DeferredItem<BlockItem>             SOUL_FANCY_IRON_LANTERN = registerBlockItem(BCBlocks.SOUL_FANCY_IRON_LANTERN);
    CopperSet<DeferredItem<BlockItem>, DeferredItem<BlockItem>> CLEAR_FANCY_COPPER_LANTERN = CopperSet.forBlockItems(BCBlocks.CLEAR_FANCY_COPPER_LANTERN, BCItems::registerBlockItem, BCItems::registerBlockItem);
    CopperSet<GroupedHolder<DyeColor, Item, BlockItem>, GroupedHolder<DyeColor, Item, BlockItem>> FANCY_COPPER_LANTERN = CopperSet.forBlockItems(BCBlocks.FANCY_COPPER_LANTERN, BCItems::coloredBlock, BCItems::coloredBlock);
    DeferredItem<BlockItem>            CLEAR_TYPEWRITER       = registerBlockItem(BCBlocks.CLEAR_TYPEWRITER);
    GroupedHolder<DyeColor, Item, BlockItem> TYPEWRITER       = coloredBlock(BCBlocks.TYPEWRITER);
    DeferredItem<ClipboardItem>        CLIPBOARD              = registerItem("clipboard", ClipboardItem::new, p -> p.stacksTo(1).component(BCDataComponents.CLIPBOARD_CONTENT.get(), ClipboardContent.DEFAULT).useBlockDescriptionPrefix());
    DeferredItem<BlockItem>            COOKIE_JAR             = registerBlockItem(BCBlocks.COOKIE_JAR);
    DeferredItem<BlockItem>            DESK_BELL              = registerBlockItem(BCBlocks.DESK_BELL);
    DeferredItem<BlockItem>            DINNER_PLATE           = registerBlockItem(BCBlocks.DINNER_PLATE);
    DeferredItem<DiscRackItem>         DISC_RACK              = registerItem("disc_rack", DiscRackItem::new);
    DeferredItem<DoubleHighBlockItem>  IRON_FANCY_ARMOR_STAND = registerBlockItem(BCBlocks.IRON_FANCY_ARMOR_STAND, DoubleHighBlockItem::new, Item.Properties::useBlockDescriptionPrefix);
    DeferredItem<BlockItem>            GOLD_CHAIN             = registerBlockItem(BCBlocks.GOLD_CHAIN);
    DeferredItem<BlockItem>            GOLD_LANTERN           = registerBlockItem(BCBlocks.GOLD_LANTERN);
    DeferredItem<BlockItem>            GOLD_SOUL_LANTERN      = registerBlockItem(BCBlocks.GOLD_SOUL_LANTERN);
    DeferredItem<BlockItem>            PRINTING_TABLE         = registerBlockItem(BCBlocks.PRINTING_TABLE);
    DeferredItem<BlockItem>            IRON_PRINTING_TABLE    = registerBlockItem(BCBlocks.IRON_PRINTING_TABLE);
    DeferredItem<BlockItem>            SWORD_PEDESTAL         = registerBlockItem(BCBlocks.SWORD_PEDESTAL, p -> p.component(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR));
    DeferredItem<BigBookItem>          BIG_BOOK               = registerItem("big_book", BigBookItem::new, p -> p.component(DataComponents.MAX_STACK_SIZE, 1).component(BCDataComponents.BIG_BOOK_CONTENT, BigBookContent.DEFAULT));
    DeferredItem<BigBookItem>          WRITTEN_BIG_BOOK       = registerItem("written_big_book", BigBookItem::new, p -> p.component(DataComponents.MAX_STACK_SIZE, 1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, WrittenBigBookContent.DEFAULT));
    DeferredItem<LockAndKeyItem>       LOCK_AND_KEY           = registerItem("lock_and_key", LockAndKeyItem::new);
    DeferredItem<PlumbLineItem>        PLUMB_LINE             = registerItem("plumb_line", PlumbLineItem::new);
    DeferredItem<RedstoneBookItem>     REDSTONE_BOOK          = registerItem("redstone_book", RedstoneBookItem::new);
    DeferredItem<SlottedBookItem>      SLOTTED_BOOK           = registerItem("slotted_book", SlottedBookItem::new);
    DeferredItem<StockroomCatalogItem> STOCKROOM_CATALOG      = registerItem("stockroom_catalog", StockroomCatalogItem::new);
    DeferredItem<TapeMeasureItem>      TAPE_MEASURE           = registerItem("tape_measure", TapeMeasureItem::new);
    DeferredItem<Item>                 TAPE_REEL              = registerSimpleItem("tape_reel");
    DeferredItem<TypewriterPageItem>   TYPEWRITER_PAGE        = registerItem("typewriter_page", TypewriterPageItem::new, p -> p.stacksTo(1).component(BCDataComponents.TYPEWRITER_PAGE, TypewriterPage.DEFAULT));
    //TODO Hand Drill
    //TODO Screw Gun
    //TODO Monocle
    //TODO Reading Glasses
    //TODO Tinted Glasses
    //TODO Atlas
    //TODO Drafting Compass
    //TODO Painting Canvas
    //TODO Recipe Book
    // @formatter:on

    private static <T extends Item> GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, T> coloredWoodItem(String name, Function3<BibliocraftWoodType, DyeColor, Item.Properties, T> factory) {
        GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, T> holder = GroupedHolder.forItemNested(BCRegistries.ITEMS, (wood, color) -> color.getName() + "_" + wood.getRegistrationPrefix() + "_", name, factory, ($1, $2) -> UnaryOperator.identity(), BibliocraftApi.getWoodTypeRegistry().getAll(), List.of(DyeColor.values()));
        WOODEN_COLORED.add(holder);
        return holder;
    }

    private static GroupedHolder<DyeColor, Item, BlockItem> coloredBlock(GroupedHolder<DyeColor, Block, ?> block) {
        return coloredBlock(block.getName(), BlockItem::new, block);
    }

    private static <T extends Item> GroupedHolder<DyeColor, Item, T> coloredBlock(String name, BiFunction<Block, Item.Properties, T> creator, GroupedHolder<DyeColor, Block, ?> block) {
        GroupedHolder<DyeColor, Item, T> holder = GroupedHolder.forItem(BCRegistries.ITEMS, color -> color.getName() + "_", name, (wood, properties) -> creator.apply(block.get(wood), properties.useBlockDescriptionPrefix()), $ -> UnaryOperator.identity(), block.keys());
        COLORED.add(holder);
        return holder;
    }

    private static GroupedHolder<BibliocraftWoodType, Item, BlockItem> woodenBlock(String name, GroupedHolder<BibliocraftWoodType, Block, ?> block) {
        return woodenBlock(name, BlockItem::new, block);
    }

    private static <T extends Item> GroupedHolder<BibliocraftWoodType, Item, T> woodenBlock(String name, BiFunction<Block, Item.Properties, T> creator, GroupedHolder<BibliocraftWoodType, Block, ?> block) {
        GroupedHolder<BibliocraftWoodType, Item, T> holder = GroupedHolder.forItem(BCRegistries.ITEMS, wood -> wood.getRegistrationPrefix() + "_", name, (wood, properties) -> creator.apply(block.get(wood), properties.useBlockDescriptionPrefix()), $ -> UnaryOperator.identity(), block.keys());
        WOODEN.add(holder);
        return holder;
    }

    private static <T extends Item> GroupedHolder<BibliocraftWoodType, Item, T> specialWoodenBlock(String name, BiFunction<BibliocraftWoodType, Item.Properties, T> creator) {
        GroupedHolder<BibliocraftWoodType, Item, T> holder = GroupedHolder.forItem(BCRegistries.ITEMS, wood -> wood.getRegistrationPrefix() + "_", name, creator, $ -> UnaryOperator.identity(), BibliocraftApi.getWoodTypeRegistry().getAll());
        WOODEN.add(holder);
        return holder;
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory, UnaryOperator<Item.Properties> properties) {
        DeferredItem<T> item = BCRegistries.ITEMS.registerItem(name, factory, properties);
        OTHER.add(item);
        return item;
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory) {
        DeferredItem<T> item = BCRegistries.ITEMS.registerItem(name, factory);
        OTHER.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSimpleItem(String name) {
        DeferredItem<Item> item = BCRegistries.ITEMS.registerSimpleItem(name);
        OTHER.add(item);
        return item;
    }

    private static DeferredItem<BlockItem> registerBlockItem(Holder<Block> block, UnaryOperator<Item.Properties> properties) {
        DeferredItem<BlockItem> item = BCRegistries.ITEMS.registerSimpleBlockItem(block, properties);
        OTHER.add(item);
        return item;
    }

    private static DeferredItem<BlockItem> registerBlockItem(Holder<Block> block) {
        DeferredItem<BlockItem> item = BCRegistries.ITEMS.registerSimpleBlockItem(block);
        OTHER.add(item);
        return item;
    }

    private static <I extends BlockItem> DeferredItem<I> registerBlockItem(Holder<Block> block, BiFunction<Block, Item.Properties, I> factory, UnaryOperator<Item.Properties> properties) {
        DeferredItem<I> item = BCRegistries.ITEMS.registerItem(block.unwrapKey().orElseThrow().location().getPath(), p -> factory.apply(block.value(), p), properties);
        OTHER.add(item);
        return item;
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
