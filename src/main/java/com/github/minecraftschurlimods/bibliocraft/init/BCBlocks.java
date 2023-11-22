package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlock;
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
import java.util.function.Supplier;

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

    WoodTypeDeferredHolder<Block, BookcaseBlock> BOOKCASE = woodenBlock("bookcase", p -> new BookcaseBlock(p.noOcclusion()));
    //TODO Clock
    //TODO Cookie Jar
    //TODO Desk
    //TODO Desk Bell
    //TODO Dinner Plate
    //TODO Disc Rack
    //TODO Display Case
    //TODO Fancy Armor Stand
    //TODO Fancy Lamp
    //TODO Fancy Lantern
    //TODO Fancy Sign
    //TODO Fancy Workbench
    //TODO Label
    //TODO Map Frame
    //TODO Potion Shelf
    //TODO Painting Frame
    //TODO Painting Press
    //TODO Printing Press
    //TODO Seat
    //TODO Shelf
    //TODO Sword Pedestal
    //TODO Table
    //TODO Tool Rack
    //TODO Typesetting Table
    //TODO Typewriter

    static <T extends Block> WoodTypeDeferredHolder<Block, T> woodenBlock(String suffix, Function<BlockBehaviour.Properties, T> creator) {
        return new WoodTypeDeferredHolder<>(BCRegistries.BLOCKS, suffix, WOOD_TYPES, wood -> creator.apply(BlockBehaviour.Properties.copy(PLANKS.get(wood))));
    }

    static void init() {}
}
