package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.deskbell.DeskBellBlock;
import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.WallDiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.WallDisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLampBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLanternBlock;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlock;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBlock;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackBlock;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BCBlocks {
    WoodTypeDeferredHolder<Block, BookcaseBlock>        BOOKCASE          = woodenBlock("bookcase",          BookcaseBlock::new);
    WoodTypeDeferredHolder<Block, FancyArmorStandBlock> FANCY_ARMOR_STAND = woodenBlock("fancy_armor_stand", FancyArmorStandBlock::new);
    WoodTypeDeferredHolder<Block, LabelBlock>           LABEL             = woodenBlock("label",             LabelBlock::new);
    WoodTypeDeferredHolder<Block, PotionShelfBlock>     POTION_SHELF      = woodenBlock("potion_shelf",      PotionShelfBlock::new);
    WoodTypeDeferredHolder<Block, ShelfBlock>           SHELF             = woodenBlock("shelf",             ShelfBlock::new);
    WoodTypeDeferredHolder<Block, TableBlock>           TABLE             = woodenBlock("table",             TableBlock::new);
    WoodTypeDeferredHolder<Block, ToolRackBlock>        TOOL_RACK         = woodenBlock("tool_rack",         ToolRackBlock::new);
    ColoredWoodTypeDeferredHolder<Block, DisplayCaseBlock>     DISPLAY_CASE      = coloredWoodenBlock("display_case",      DisplayCaseBlock::new);
    ColoredWoodTypeDeferredHolder<Block, WallDisplayCaseBlock> WALL_DISPLAY_CASE = coloredWoodenBlock("wall_display_case", WallDisplayCaseBlock::new);
    ColoredWoodTypeDeferredHolder<Block, SeatBlock>            SEAT              = coloredWoodenBlock("seat",              SeatBlock::new);
    ColoredWoodTypeDeferredHolder<Block, SeatBackBlock>        SEAT_BACK         = coloredWoodenBlock("seat_back",         SeatBackBlock::new);
    DeferredBlock<FancyLampBlock>                   CLEAR_FANCY_GOLD_LAMP     = BCRegistries.BLOCKS.register("fancy_gold_lamp",    () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLampBlock>    FANCY_GOLD_LAMP           = coloredBlock(                "fancy_gold_lamp",    () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<FancyLampBlock>                   CLEAR_FANCY_IRON_LAMP     = BCRegistries.BLOCKS.register("fancy_iron_lamp",    () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLampBlock>    FANCY_IRON_LAMP           = coloredBlock(                "fancy_iron_lamp",    () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<FancyLanternBlock>                CLEAR_FANCY_GOLD_LANTERN  = BCRegistries.BLOCKS.register("fancy_gold_lantern", () -> new FancyLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLanternBlock> FANCY_GOLD_LANTERN        = coloredBlock(                "fancy_gold_lantern", () -> new FancyLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<FancyLanternBlock>                CLEAR_FANCY_IRON_LANTERN  = BCRegistries.BLOCKS.register("fancy_iron_lantern", () -> new FancyLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLanternBlock> FANCY_IRON_LANTERN        = coloredBlock(                "fancy_iron_lantern", () -> new FancyLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<CookieJarBlock>       COOKIE_JAR             = BCRegistries.BLOCKS.register("cookie_jar",             () -> new CookieJarBlock      (BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    DeferredBlock<DeskBellBlock>        DESK_BELL              = BCRegistries.BLOCKS.register("desk_bell",              () -> new DeskBellBlock       (BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    DeferredBlock<DinnerPlateBlock>     DINNER_PLATE           = BCRegistries.BLOCKS.register("dinner_plate",           () -> new DinnerPlateBlock    (BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion()));
    DeferredBlock<DiscRackBlock>        DISC_RACK              = BCRegistries.BLOCKS.register("disc_rack",              () -> new DiscRackBlock       (BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion()));
    DeferredBlock<WallDiscRackBlock>    WALL_DISC_RACK         = BCRegistries.BLOCKS.register("wall_disc_rack",         () -> new WallDiscRackBlock   (BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion()));
    DeferredBlock<ChainBlock>           GOLD_CHAIN             = BCRegistries.BLOCKS.register("gold_chain",             () -> new ChainBlock          (BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN)));
    DeferredBlock<LanternBlock>         GOLD_LANTERN           = BCRegistries.BLOCKS.register("gold_lantern",           () -> new LanternBlock        (BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    DeferredBlock<LanternBlock>         GOLD_SOUL_LANTERN      = BCRegistries.BLOCKS.register("gold_soul_lantern",      () -> new LanternBlock        (BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_LANTERN)));
    DeferredBlock<FancyArmorStandBlock> IRON_FANCY_ARMOR_STAND = BCRegistries.BLOCKS.register("iron_fancy_armor_stand", () -> new FancyArmorStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    DeferredBlock<SwordPedestalBlock>   SWORD_PEDESTAL         = BCRegistries.BLOCKS.register("sword_pedestal",         () -> new SwordPedestalBlock  (BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE).noOcclusion()));
    //TODO Clipboard
    //TODO Fancy Sign
    //TODO Fancy Workbench
    //TODO Clock
    //TODO Desk
    //TODO Map Frame
    //TODO Painting Frame
    //TODO Painting Press
    //TODO Printing Press
    //TODO Typesetting Table
    //TODO Typewriter

    /**
     * Registration helper method for {@link WoodTypeDeferredHolder}s.
     *
     * @param suffix  The suffix for the {@link WoodTypeDeferredHolder}.
     * @param creator An adapted creator function for the {@link WoodTypeDeferredHolder}. Passes in a copy of the wood type's associated plank block properties.
     * @return A {@code WoodTypeDeferredHolder<Block, T>}.
     * @param <T> The type of the block registered.
     */
    static <T extends Block> WoodTypeDeferredHolder<Block, T> woodenBlock(String suffix, Function<BlockBehaviour.Properties, T> creator) {
        return new WoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, wood -> creator.apply(wood.properties().get().noOcclusion()));
    }

    /**
     * Registration helper method for {@link ColoredDeferredHolder}s.
     *
     * @param suffix   The suffix for the {@link ColoredDeferredHolder}.
     * @param supplier A supplier for the {@link ColoredDeferredHolder}.
     * @return A {@code ColoredDeferredHolder<Block, T>}.
     * @param <T> The type of the block registered.
     */
    static <T extends Block> ColoredDeferredHolder<Block, T> coloredBlock(String suffix, Supplier<T> supplier) {
        return new ColoredDeferredHolder<>(BCRegistries.BLOCKS, suffix, color -> supplier.get());
    }

    /**
     * Registration helper method for {@link ColoredWoodTypeDeferredHolder}s.
     *
     * @param suffix  The suffix for the {@link ColoredWoodTypeDeferredHolder}.
     * @param creator An adapted creator function for the {@link ColoredWoodTypeDeferredHolder}. Passes in a copy of the wood type's associated plank block properties.
     * @return A {@code WoodTypeDeferredHolder<Block, T>}.
     * @param <T> The type of the block registered.
     */
    static <T extends Block> ColoredWoodTypeDeferredHolder<Block, T> coloredWoodenBlock(String suffix, Function<BlockBehaviour.Properties, T> creator) {
        return new ColoredWoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, (wood, color) -> creator.apply(wood.properties().get().noOcclusion()));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
