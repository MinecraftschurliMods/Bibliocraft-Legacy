package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PrintingTableMenu extends BCMenu<PrintingTableBlockEntity> {
    public PrintingTableMenu(int id, Inventory inventory, PrintingTableBlockEntity blockEntity) {
        super(BCMenus.PRINTING_TABLE.get(), id, inventory, blockEntity);
    }

    public PrintingTableMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.PRINTING_TABLE.get(), id, inventory, buf);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new Slot(blockEntity, j + i * 3, 12 + j * 18, 17 + i * 18));
            }
        }
        addSlot(new Slot(blockEntity, 9, 102, 35));
        addSlot(new ResultSlot(blockEntity, 10, 148, 35));
        addInventorySlots(inventory, 8, 84);
    }

    private static class ResultSlot extends Slot {
        private final PrintingTableBlockEntity blockEntity;

        public ResultSlot(PrintingTableBlockEntity blockEntity, int slot, int x, int y) {
            super(blockEntity, slot, x, y);
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            //TODO take in block entity
        }
    }
}
