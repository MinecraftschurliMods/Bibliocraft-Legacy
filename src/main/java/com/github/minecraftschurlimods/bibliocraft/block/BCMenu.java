package com.github.minecraftschurlimods.bibliocraft.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public abstract class BCMenu<T extends BCBlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;

    public BCMenu(MenuType<?> type, int id, Inventory inventory, T blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
        addSlots(inventory, blockEntity);
    }

    @SuppressWarnings("unchecked")
    public BCMenu(MenuType<?> type, int id, Inventory inventory, FriendlyByteBuf data) {
        this(type, id, inventory, (T) Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())));
    }

    protected abstract void addSlots(Inventory inventory, T blockEntity);

    protected void addInventorySlots(Inventory inventory, int x, int y) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, x + i * 18, y + 58));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, i * 9 + j + 9, x + j * 18, y + i * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int slotCount = blockEntity.items.getSlots();
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < slotCount) { // If slot is a BE slot
            // Try moving to the hotbar or inventory
            if (!moveItemStackTo(slot.getItem(), slotCount, slotCount + 36, false)) return ItemStack.EMPTY;
        } else if (index < slotCount + 9) { // If slot is a hotbar slot
            // Try moving to the BE
            for (int i = 0; i < slotCount; i++) {
                Slot beSlot = slots.get(i);
                if (!beSlot.hasItem() && beSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the inventory
            if (!moveItemStackTo(stack, slotCount + 9, slotCount + 36, false)) return ItemStack.EMPTY;
        } else if (index < slotCount + 36) { // If slot is an inventory slot
            // Try moving to the BE
            for (int i = 0; i < slotCount; i++) {
                Slot beSlot = slots.get(i);
                if (!beSlot.hasItem() && beSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the hotbar
            if (!moveItemStackTo(stack, slotCount, slotCount + 9, false)) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    public static class TagLimitedSlot extends Slot {
        private final TagKey<Item> tag;

        public TagLimitedSlot(Container container, int index, int x, int y, TagKey<Item> tag) {
            super(container, index, x, y);
            this.tag = tag;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(tag);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
