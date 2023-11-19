package com.github.minecraftschurlimods.bibliocraft.block.bookcase;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BookcaseMenu extends BCMenu<BookcaseBlockEntity> {
    public BookcaseMenu(int id, Inventory inventory, BookcaseBlockEntity blockEntity) {
        super(BCMenuTypes.BOOKCASE.get(), id, inventory, blockEntity);
    }

    public BookcaseMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenuTypes.BOOKCASE.get(), id, inventory, buf);
    }

    @Override
    public void addSlots(Inventory inventory, BookcaseBlockEntity blockEntity) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                addSlot(new Slot(blockEntity, i * 8 + j, 17 + j * 18, 17 + i * 25));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
