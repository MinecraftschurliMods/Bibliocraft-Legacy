package com.github.minecraftschurlimods.bibliocraft.content.fancycrafter;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.util.slot.HasToggleableSlots;
import com.github.minecraftschurlimods.bibliocraft.util.slot.ToggleableSlot;
import com.github.minecraftschurlimods.bibliocraft.util.slot.ViewSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyCrafterMenu extends BCMenu<FancyCrafterBlockEntity> implements HasToggleableSlots {
    private final ContainerData containerData;

    public FancyCrafterMenu(int id, Inventory inventory, FancyCrafterBlockEntity blockEntity, ContainerData containerData) {
        super(BCMenus.FANCY_CRAFTER.get(), id, inventory, blockEntity);
        this.containerData = containerData;
        addDataSlots(containerData);
    }

    public FancyCrafterMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.FANCY_CRAFTER.get(), id, inventory, data);
        this.containerData = new SimpleContainerData(FancyCrafterBlockEntity.CRAFTING_SLOTS);
        addDataSlots(containerData);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new ToggleableSlot<>(blockEntity, x + y * 3, 30 + x * 18, 17 + y * 18));
            }
        }
        addSlot(new ViewSlot(blockEntity, FancyCrafterBlockEntity.CRAFTING_RESULT_SLOT_INDEX, 124, 35));
        for (int i = 0; i < FancyCrafterBlockEntity.STORAGE_SLOTS; i++) {
            addSlot(new Slot(blockEntity, i + FancyCrafterBlockEntity.CRAFTING_SLOTS + FancyCrafterBlockEntity.CRAFTING_RESULT_SLOTS, 17 + i * 18, 78));
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

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        int slotIndex = getSlot(slot).getSlotIndex();
        if (slotIndex >= FancyCrafterBlockEntity.CRAFTING_SLOTS || slotIndex < 0) return;
        containerData.set(slotIndex, disabled ? FancyCrafterBlockEntity.SLOT_DISABLED : FancyCrafterBlockEntity.SLOT_ENABLED);
        broadcastChanges();
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        int slotIndex = getSlot(slot).getSlotIndex();
        return containerData.get(slotIndex) == FancyCrafterBlockEntity.SLOT_DISABLED;
    }

    @Override
    public boolean canDisableSlot(int slotId) {
        Slot slot = getSlot(slotId);
        return slot instanceof ToggleableSlot && !slot.hasItem();
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SameParameterValue"})
    private boolean moveItemStackToEnabled(ItemStack stack, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!isSlotDisabled(i) && !moveItemStackTo(stack, i, i + 1, false))
                return false;
        }
        return true;
    }
}
