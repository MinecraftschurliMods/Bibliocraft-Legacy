package com.github.minecraftschurlimods.bibliocraft.content.shelf;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ShelfBlockEntity extends BCMenuBlockEntity {
    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SHELF.get(), 4, defaultName("shelf"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ShelfMenu(id, inventory, this);
    }
}
