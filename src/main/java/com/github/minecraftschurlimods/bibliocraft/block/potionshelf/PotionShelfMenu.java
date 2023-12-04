package com.github.minecraftschurlimods.bibliocraft.block.potionshelf;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PotionShelfMenu extends BCMenu<PotionShelfBlockEntity> {
    public PotionShelfMenu(int id, Inventory inventory, PotionShelfBlockEntity blockEntity) {
        super(BCMenuTypes.POTION_SHELF.get(), id, inventory, blockEntity);
    }

    public PotionShelfMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenuTypes.POTION_SHELF.get(), id, inventory, buf);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < 12) { // If slot is a potion shelf slot
            // Try moving to the hotbar or inventory (slots 12-47), return if successful
            if (!moveItemStackTo(slot.getItem(), 12, 48, false)) return ItemStack.EMPTY;
        } else if (index < 21) { // If slot is a hotbar slot
            // Try moving to the potion shelf (slots 0-11), return if successful
            for (int i = 0; i < 12; i++) {
                Slot potionShelfSlot = slots.get(i);
                if (!potionShelfSlot.hasItem() && potionShelfSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the inventory (slots 21-47), return if successful
            if (!moveItemStackTo(stack, 21, 48, false)) return ItemStack.EMPTY;
        } else if (index < 48) { // If slot is an inventory slot
            // Try moving to the potion shelf (slots 0-11), return if successful
            for (int i = 0; i < 12; i++) {
                Slot potionShelfSlot = slots.get(i);
                if (!potionShelfSlot.hasItem() && potionShelfSlot.mayPlace(stack) && !moveItemStackTo(stack, i, i + 1, false))
                    return ItemStack.EMPTY;
            }
            // Try moving to the hotbar (slots 12-20), return if successful
            if (!moveItemStackTo(stack, 12, 21, false)) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    @Override
    protected void addSlots(Inventory inventory, PotionShelfBlockEntity blockEntity) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                addSlot(new PotionShelfSlot(blockEntity, i * 4 + j, 53 + j * 18, 15 + i * 19));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }

    public static class PotionShelfSlot extends Slot {
        public PotionShelfSlot(PotionShelfBlockEntity blockEntity, int index, int x, int y) {
            super(blockEntity, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(BCTags.Items.POTION_SHELF_POTIONS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
