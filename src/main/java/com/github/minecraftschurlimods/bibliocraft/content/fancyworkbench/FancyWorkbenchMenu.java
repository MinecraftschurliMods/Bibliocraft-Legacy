package com.github.minecraftschurlimods.bibliocraft.content.fancyworkbench;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyWorkbenchMenu extends BCMenu<FancyWorkbenchBlockEntity> {
    public FancyWorkbenchMenu(int id, Inventory inventory, FancyWorkbenchBlockEntity blockEntity) {
        super(BCMenus.FANCY_WORKBENCH.get(), id, inventory, blockEntity);
    }

    public FancyWorkbenchMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.FANCY_WORKBENCH.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new FancyWorkbenchSlot(blockEntity, x + y * 3, 30 + x * 18, 17 + y * 18));
            }
        }
        addSlot(new NoPlaceSlot(blockEntity, 9, 124, 35));
        for (int i = 0; i < 8; i++) {
            addSlot(new Slot(blockEntity, i + 10, 17 + i * 18, 78));
        }
        addInventorySlots(inventory, 8, 110);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index); //TODO
    }

    public void setSlotDisabled(int slot, boolean disabled) {
        if (slot > 8) return;
        blockEntity.setSlotDisabled(slot, disabled);
        broadcastChanges();
    }
    
    public boolean isSlotDisabled(int slot) {
        return blockEntity.isSlotDisabled(slot);
    }

    private static class NoPlaceSlot extends Slot {
        public NoPlaceSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
