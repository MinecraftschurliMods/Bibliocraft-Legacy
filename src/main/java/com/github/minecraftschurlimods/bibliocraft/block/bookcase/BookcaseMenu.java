package com.github.minecraftschurlimods.bibliocraft.block.bookcase;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
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
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < 16) { // If slot is a bookcase slot
            // Try moving to the hotbar or inventory (slots 16-51), return if successful
            if (!moveItemStackTo(slot.getItem(), 16, 52, false)) return ItemStack.EMPTY;
        } else if (index < 25) { // If slot is a hotbar slot
            // Try moving to the bookcase (slots 0-15), return if successful
            for (int i = 0; i < 16; i++) {
                Slot bookcaseSlot = slots.get(i);
                if (!bookcaseSlot.hasItem() && bookcaseSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the inventory (slots 25-51), return if successful
            if (!moveItemStackTo(stack, 25, 52, false)) return ItemStack.EMPTY;
        } else if (index < 52) { // If slot is an inventory slot
            // Try moving to the bookcase (slots 0-15), return if successful
            for (int i = 0; i < 16; i++) {
                Slot bookcaseSlot = slots.get(i);
                if (!bookcaseSlot.hasItem() && bookcaseSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the hotbar (slots 16-24), return if successful
            if (!moveItemStackTo(stack, 16, 25, false)) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    @Override
    protected void addSlots(Inventory inventory, BookcaseBlockEntity blockEntity) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                addSlot(new BookcaseSlot(blockEntity, i * 8 + j, 17 + j * 18, 17 + i * 25));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }

    public static class BookcaseSlot extends Slot {
        public BookcaseSlot(BookcaseBlockEntity blockEntity, int index, int x, int y) {
            super(blockEntity, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(BCTags.Items.BOOKCASE_BOOKS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
