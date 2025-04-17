package com.github.minecraftschurlimods.bibliocraft.content.fancycrafter;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyCrafterMenu extends BCMenu<FancyCrafterBlockEntity> {
    public FancyCrafterMenu(int id, Inventory inventory, FancyCrafterBlockEntity blockEntity) {
        super(BCMenus.FANCY_CRAFTER.get(), id, inventory, blockEntity);
    }

    public FancyCrafterMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.FANCY_CRAFTER.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new FancyCrafterSlot(blockEntity, x + y * 3, 30 + x * 18, 17 + y * 18));
            }
        }
        addSlot(new ViewSlot(blockEntity, 9, 124, 35));
        for (int i = 0; i < 8; i++) {
            addSlot(new Slot(blockEntity, i + 10, 17 + i * 18, 78));
        }
        addInventorySlots(inventory, 8, 110);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int slotCount = blockEntity.getContainerSize();
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < slotCount) { // If slot is a BE slot
            if (index == 9) return stack;
            // Try moving to the hotbar or inventory
            if (!moveItemStackTo(slot.getItem(), slotCount, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 9) { // If slot is a hotbar slot
            // Try moving to the crafting grid
            if (!moveItemStackToEnabled(stack, 0, 9))
                return ItemStack.EMPTY;
            // Try moving to the container
            if (!moveItemStackTo(stack, 10, slotCount, false))
                return ItemStack.EMPTY;
            // Try moving to the inventory
            if (!moveItemStackTo(stack, slotCount + 9, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 36) { // If slot is an inventory slot
            // Try moving to the crafting grid
            if (!moveItemStackToEnabled(stack, 0, 9))
                return ItemStack.EMPTY;
            // Try moving to the container
            if (!moveItemStackTo(stack, 10, slotCount, false))
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

    public void setSlotDisabled(int slot, boolean disabled) {
        if (slot > 8) return;
        blockEntity.setSlotDisabled(slot, disabled);
        broadcastChanges();
    }
    
    public boolean isSlotDisabled(int slot) {
        return blockEntity.isSlotDisabled(slot);
    }
    
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SameParameterValue"})
    private boolean moveItemStackToEnabled(ItemStack stack, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!blockEntity.isSlotDisabled(i) && !moveItemStackTo(stack, i, i + 1, false))
                return false;
        }
        return true;
    }

    private static class ViewSlot extends Slot {
        public ViewSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
}
