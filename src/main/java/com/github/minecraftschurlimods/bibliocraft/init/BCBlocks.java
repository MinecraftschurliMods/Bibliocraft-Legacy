package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandBlock;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.block.shelf.ShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackBlock;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface BCBlocks {
    List<WoodType> WOOD_TYPES = List.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.JUNGLE, WoodType.ACACIA, WoodType.DARK_OAK, WoodType.CRIMSON, WoodType.WARPED, WoodType.MANGROVE, WoodType.BAMBOO, WoodType.CHERRY);
    Map<WoodType, Block> PLANKS = Util.make(new HashMap<>(), map -> {
        map.put(WoodType.OAK,      Blocks.OAK_PLANKS);
        map.put(WoodType.SPRUCE,   Blocks.SPRUCE_PLANKS);
        map.put(WoodType.BIRCH,    Blocks.BIRCH_PLANKS);
        map.put(WoodType.JUNGLE,   Blocks.JUNGLE_PLANKS);
        map.put(WoodType.ACACIA,   Blocks.ACACIA_PLANKS);
        map.put(WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS);
        map.put(WoodType.CRIMSON,  Blocks.CRIMSON_PLANKS);
        map.put(WoodType.WARPED,   Blocks.WARPED_PLANKS);
        map.put(WoodType.MANGROVE, Blocks.MANGROVE_PLANKS);
        map.put(WoodType.BAMBOO,   Blocks.BAMBOO_PLANKS);
        map.put(WoodType.CHERRY,   Blocks.CHERRY_PLANKS);
    });

    WoodTypeDeferredHolder<Block, BookcaseBlock>        BOOKCASE          = woodenBlock("bookcase",          BookcaseBlock::new);
    WoodTypeDeferredHolder<Block, FancyArmorStandBlock> FANCY_ARMOR_STAND = woodenBlock("fancy_armor_stand", FancyArmorStandBlock::new);
    WoodTypeDeferredHolder<Block, PotionShelfBlock>     POTION_SHELF      = woodenBlock("potion_shelf",      PotionShelfBlock::new);
    WoodTypeDeferredHolder<Block, ShelfBlock>           SHELF             = woodenBlock("shelf",             ShelfBlock::new);
    WoodTypeDeferredHolder<Block, ToolRackBlock>        TOOL_RACK         = woodenBlock("tool_rack",         ToolRackBlock::new);
    //TODO Clock
    //TODO Cookie Jar
    //TODO Desk
    //TODO Desk Bell
    //TODO Dinner Plate
    //TODO Disc Rack
    //TODO Display Case
    //TODO Fancy Lamp
    //TODO Fancy Lantern
    //TODO Fancy Sign
    //TODO Fancy Workbench
    //TODO Label
    //TODO Map Frame
    //TODO Painting Frame
    //TODO Painting Press
    //TODO Printing Press
    //TODO Seat
    //TODO Sword Pedestal
    //TODO Table
    //TODO Typesetting Table
    //TODO Typewriter

    static <T extends Block> WoodTypeDeferredHolder<Block, T> woodenBlock(String suffix, Function<BlockBehaviour.Properties, T> creator) {
        return new WoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, WOOD_TYPES, wood -> creator.apply(BlockBehaviour.Properties.ofFullCopy(PLANKS.get(wood)).noOcclusion()));
    }

    static void init() {}
}
