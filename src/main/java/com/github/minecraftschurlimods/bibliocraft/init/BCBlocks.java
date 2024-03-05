package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.WallDisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlock;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlock;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBlock;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackBlock;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

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
    DeferredHolder<Block, CookieJarBlock>       COOKIE_JAR             = BCRegistries.BLOCKS.register("cookie_jar",             () -> new CookieJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    DeferredHolder<Block, FancyArmorStandBlock> IRON_FANCY_ARMOR_STAND = BCRegistries.BLOCKS.register("iron_fancy_armor_stand", () -> new FancyArmorStandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    DeferredHolder<Block, SwordPedestalBlock>   SWORD_PEDESTAL         = BCRegistries.BLOCKS.register("sword_pedestal",         () -> new SwordPedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE).noOcclusion()));
    //TODO Clock
    //TODO Cookie Jar
    //TODO Desk
    //TODO Desk Bell
    //TODO Dinner Plate
    //TODO Disc Rack
    //TODO Fancy Lamp
    //TODO Fancy Lantern
    //TODO Fancy Sign
    //TODO Fancy Workbench
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
        return new WoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, wood -> creator.apply(wood.getProperties().get().noOcclusion()));
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
        return new ColoredWoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, (wood, color) -> creator.apply(wood.getProperties().get().noOcclusion()));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
