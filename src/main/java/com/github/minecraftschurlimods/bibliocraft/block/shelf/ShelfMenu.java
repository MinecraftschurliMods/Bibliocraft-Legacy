package com.github.minecraftschurlimods.bibliocraft.block.shelf;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ShelfMenu extends BCMenu<ShelfBlockEntity> {
    public ShelfMenu(int id, Inventory inventory, ShelfBlockEntity blockEntity) {
        super(BCMenus.SHELF.get(), id, inventory, blockEntity);
    }

    public ShelfMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.SHELF.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        addSlot(new Slot(blockEntity, 0, 53, 15));
        addSlot(new Slot(blockEntity, 1, 107, 15));
        addSlot(new Slot(blockEntity, 2, 53, 53));
        addSlot(new Slot(blockEntity, 3, 107, 53));
        addInventorySlots(inventory, 8, 84);
    }
}
