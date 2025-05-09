package com.github.minecraftschurlimods.bibliocraft.content.bookcase;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class BookcaseMenu extends BCMenu<BookcaseBlockEntity> {
    public BookcaseMenu(int id, Inventory inventory, BookcaseBlockEntity blockEntity) {
        super(BCMenus.BOOKCASE.get(), id, inventory, blockEntity);
    }

    public BookcaseMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.BOOKCASE.get(), id, inventory, buf);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                addSlot(new BCSlot(blockEntity, i * 8 + j, 17 + j * 18, 17 + i * 25));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }
}
