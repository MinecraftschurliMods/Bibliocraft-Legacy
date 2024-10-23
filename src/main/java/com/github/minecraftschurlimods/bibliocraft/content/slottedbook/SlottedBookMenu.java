package com.github.minecraftschurlimods.bibliocraft.content.slottedbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlottedBookMenu extends AbstractContainerMenu {
    public SlottedBookMenu(int id, Inventory inventory, ItemStack stack) {
        super(BCMenus.SLOTTED_BOOK.get(), id);
        Container container = new SlottedBookContainer(stack);
        addSlot(new SlottedBookSlot(container, 0, 80, 34));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, i * 9 + j + 9, 8 + j * 18, 141 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 199));
        }
    }

    public SlottedBookMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, inventory.player.getItemInHand(buf.readEnum(InteractionHand.class)));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int slotCount = 1;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < slotCount) { // If slot is a BE slot
            // Try moving to the hotbar or inventory
            if (!moveItemStackTo(slot.getItem(), slotCount, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 9) { // If slot is a hotbar slot
            // Try moving to the BE
            if (!moveItemStackTo(stack, 0, slotCount, false))
                return ItemStack.EMPTY;
            // Try moving to the inventory
            if (!moveItemStackTo(stack, slotCount + 9, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 36) { // If slot is an inventory slot
            // Try moving to the BE
            if (!moveItemStackTo(stack, 0, slotCount, false))
                return ItemStack.EMPTY;
            // Try moving to the hotbar
            if (!moveItemStackTo(stack, slotCount, slotCount + 9, false))
                return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
