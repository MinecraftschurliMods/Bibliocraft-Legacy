package com.github.minecraftschurlimods.bibliocraft.util.content;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * Abstract superclass for most menus of this mod.
 *
 * @param <T> The block entity this menu is associated with.
 */
@SuppressWarnings("SameParameterValue")
public abstract class BCMenu<T extends BCMenuBlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;

    /**
     * @param type        The {@link MenuType} to use.
     * @param id          The id of the menu, provided by the game.
     * @param inventory   The {@link Inventory} to use, provided by the game.
     * @param blockEntity The block entity associated with the menu.
     */
    public BCMenu(MenuType<?> type, int id, Inventory inventory, T blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
        addSlots(inventory);
    }

    /**
     * @param type      The {@link MenuType} to use.
     * @param id        The id of the menu, provided by the game.
     * @param inventory The {@link Inventory} to use, provided by the game.
     * @param data      The {@link FriendlyByteBuf} to read the block entity data from.
     */
    @SuppressWarnings("unchecked")
    public BCMenu(MenuType<?> type, int id, Inventory inventory, FriendlyByteBuf data) {
        this(type, id, inventory, (T) Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())));
    }

    /**
     * Adds slots to this menu.
     *
     * @param inventory The player inventory to use.
     */
    protected abstract void addSlots(Inventory inventory);

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

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    public Component getDisplayName() {
        return blockEntity.getDisplayName();
    }

    /**
     * Helper method for adding the player inventory slots.
     *
     * @param inventory The player inventory to add slots for.
     * @param x         The x position of the inventory.
     * @param y         The y position of the inventory.
     */
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

    /**
     * Variant of {@link Slot} that defers placement checks to the container.
     */
    public static class BCSlot extends Slot {
        /**
         * @param container The {@link Container} this slot is in.
         * @param index     The slot index.
         * @param x         The x position.
         * @param y         The y position.
         */
        public BCSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return container.canPlaceItem(index, stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
