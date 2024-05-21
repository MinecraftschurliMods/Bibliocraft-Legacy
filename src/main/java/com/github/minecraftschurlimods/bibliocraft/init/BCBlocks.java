package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.deskbell.DeskBellBlock;
import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.WallDiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.WallDisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLampBlock;
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
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.minecraftschurlimods.bibliocraft.init.BCRegistries.*;

public interface BCBlocks {
    Supplier<MapCodec<BookcaseBlock>>        BOOKCASE_TYPE          = BLOCK_TYPES.register("bookcase", () -> BookcaseBlock.CODEC);
    Supplier<MapCodec<FancyArmorStandBlock>> FANCY_ARMOR_STAND_TYPE = BLOCK_TYPES.register("fancy_armor_stand", () -> FancyArmorStandBlock.CODEC);
    Supplier<MapCodec<LabelBlock>>           LABEL_TYPE             = BLOCK_TYPES.register("label", () -> LabelBlock.CODEC);
    Supplier<MapCodec<PotionShelfBlock>>     POTION_SHELF_TYPE      = BLOCK_TYPES.register("potion_shelf", () -> PotionShelfBlock.CODEC);
    Supplier<MapCodec<ShelfBlock>>           SHELF_TYPE             = BLOCK_TYPES.register("shelf", () -> ShelfBlock.CODEC);
    Supplier<MapCodec<TableBlock>>           TABLE_TYPE             = BLOCK_TYPES.register("table", () -> TableBlock.CODEC);
    Supplier<MapCodec<ToolRackBlock>>        TOOL_RACK_TYPE         = BLOCK_TYPES.register("tool_rack", () -> ToolRackBlock.CODEC);
    Supplier<MapCodec<DisplayCaseBlock>>     DISPLAY_CASE_TYPE      = BLOCK_TYPES.register("display_case", () -> DisplayCaseBlock.CODEC);
    Supplier<MapCodec<WallDisplayCaseBlock>> WALL_DISPLAY_CASE_TYPE = BLOCK_TYPES.register("wall_display_case", () -> WallDisplayCaseBlock.CODEC);
    Supplier<MapCodec<SeatBlock>>            SEAT_TYPE              = BLOCK_TYPES.register("seat", () -> SeatBlock.CODEC);
    Supplier<MapCodec<SeatBackBlock>>        SEAT_BACK_TYPE         = BLOCK_TYPES.register("seat_back", () -> SeatBackBlock.CODEC);
    Supplier<MapCodec<CookieJarBlock>>       COOKIE_JAR_TYPE        = BLOCK_TYPES.register("cookie_jar", () -> CookieJarBlock.CODEC);
    Supplier<MapCodec<DeskBellBlock>>        DESK_BELL_TYPE         = BLOCK_TYPES.register("desk_bell", () -> DeskBellBlock.CODEC);
    Supplier<MapCodec<DinnerPlateBlock>>     DINNER_PLATE_TYPE      = BLOCK_TYPES.register("dinner_plate", () -> DinnerPlateBlock.CODEC);
    Supplier<MapCodec<SwordPedestalBlock>>   SWORD_PEDESTAL_TYPE    = BLOCK_TYPES.register("sword_pedestal", () -> SwordPedestalBlock.CODEC);
    WoodTypeDeferredHolder<Block, BookcaseBlock>        BOOKCASE          = woodenBlock("bookcase", BookcaseBlock::new);
    WoodTypeDeferredHolder<Block, FancyArmorStandBlock> FANCY_ARMOR_STAND = woodenBlock("fancy_armor_stand", FancyArmorStandBlock::new);
    WoodTypeDeferredHolder<Block, LabelBlock>           LABEL             = woodenBlock("label", LabelBlock::new);
    WoodTypeDeferredHolder<Block, PotionShelfBlock>     POTION_SHELF      = woodenBlock("potion_shelf", PotionShelfBlock::new);
    WoodTypeDeferredHolder<Block, ShelfBlock>           SHELF             = woodenBlock("shelf", ShelfBlock::new);
    WoodTypeDeferredHolder<Block, TableBlock>           TABLE             = woodenBlock("table", TableBlock::new);
    WoodTypeDeferredHolder<Block, ToolRackBlock>        TOOL_RACK         = woodenBlock("tool_rack", ToolRackBlock::new);
    ColoredWoodTypeDeferredHolder<Block, DisplayCaseBlock>     DISPLAY_CASE      = coloredWoodenBlock("display_case", DisplayCaseBlock::new);
    ColoredWoodTypeDeferredHolder<Block, WallDisplayCaseBlock> WALL_DISPLAY_CASE = coloredWoodenBlock("wall_display_case", WallDisplayCaseBlock::new);
    ColoredWoodTypeDeferredHolder<Block, SeatBlock>            SEAT              = coloredWoodenBlock("seat", SeatBlock::new);
    ColoredWoodTypeDeferredHolder<Block, SeatBackBlock>        SEAT_BACK         = coloredWoodenBlock("seat_back", SeatBackBlock::new);
    DeferredBlock<FancyLampBlock>                CLEAR_FANCY_GOLD_LAMP  = BCRegistries.BLOCKS.register("clear_fancy_gold_lamp", () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLampBlock> FANCY_GOLD_LAMP        = coloredBlock("fancy_gold_lamp", () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<FancyLampBlock>                CLEAR_FANCY_IRON_LAMP  = BCRegistries.BLOCKS.register("clear_fancy_iron_lamp", () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).lightLevel($ -> 15).noOcclusion()));
    ColoredDeferredHolder<Block, FancyLampBlock> FANCY_IRON_LAMP        = coloredBlock("fancy_iron_lamp", () -> new FancyLampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).lightLevel($ -> 15).noOcclusion()));
    DeferredBlock<CookieJarBlock>                COOKIE_JAR             = BLOCKS.register("cookie_jar", () -> new CookieJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    DeferredBlock<DeskBellBlock>                 DESK_BELL              = BLOCKS.register("desk_bell", () -> new DeskBellBlock(BCSoundEvents.DESK_BELL, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    DeferredBlock<DinnerPlateBlock>              DINNER_PLATE           = BLOCKS.register("dinner_plate", () -> new DinnerPlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion()));
    DeferredBlock<DiscRackBlock>                 DISC_RACK              = BCRegistries.BLOCKS.register("disc_rack", () -> new DiscRackBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion()));
    DeferredBlock<WallDiscRackBlock>             WALL_DISC_RACK         = BCRegistries.BLOCKS.register("wall_disc_rack", () -> new WallDiscRackBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion()));
    DeferredBlock<FancyArmorStandBlock>          IRON_FANCY_ARMOR_STAND = BLOCKS.register("iron_fancy_armor_stand", () -> new FancyArmorStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    DeferredBlock<SwordPedestalBlock>            SWORD_PEDESTAL         = BLOCKS.register("sword_pedestal", () -> new SwordPedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE).noOcclusion()));
    //TODO Clipboard
    //TODO Fancy Lantern
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
        return new WoodTypeDeferredHolder<>(BLOCKS, suffix, wood -> creator.apply(wood.properties().get().noOcclusion()));
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
        return new ColoredWoodTypeDeferredHolder<>(BLOCKS, suffix, (wood, color) -> creator.apply(wood.properties().get().noOcclusion()));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
