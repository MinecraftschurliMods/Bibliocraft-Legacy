package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface BCBlocks {
    DeferredHolder<Block, BookcaseBlock> BOOKCASE = BCRegistries.BLOCKS.register("bookcase", () -> new BookcaseBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
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

    static void init() {}
}
