package at.minecraftschurli.mods.bibliocraft.content.printingtable;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
import at.minecraftschurli.mods.bibliocraft.util.slot.HasToggleableSlots;
import at.minecraftschurli.mods.bibliocraft.util.slot.ToggleableSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class PrintingTableMenu extends BCMenu<PrintingTableBlockEntity> implements HasToggleableSlots {
    public PrintingTableMenu(int id, Inventory inventory, PrintingTableBlockEntity blockEntity) {
        super(BCMenus.PRINTING_TABLE.get(), id, inventory, blockEntity);
    }

    public PrintingTableMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.PRINTING_TABLE.get(), id, inventory, buf);
    }

    /// @return The block entity this menu is associated with.
    public PrintingTableBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    protected void addSlots(Inventory inventory) {
        BCItemHandler itemHandler = getItemHandler();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int slot = j + i * 3;
                addSlot(new ToggleableSlot(itemHandler, this::isSlotDisabled, slot, 17 + j * 18, 17 + i * 18) {
                    @Override
                    protected void setStackCopy(ItemStack stack) {
                        super.setStackCopy(stack);
                        blockEntity.setSlot(slot, stack);
                    }
                });
            }
        }
        addSlot(new ResourceHandlerSlot(itemHandler, itemHandler::set, 9, 90, 35) {
            @Override
            protected void setStackCopy(ItemStack stack) {
                super.setStackCopy(stack);
                blockEntity.setSlot(9, stack);
            }
        });
        addSlot(new ResultSlot(itemHandler, 10, 142, 35));
        addInventorySlots(inventory, 8, 84);
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        return blockEntity.isSlotDisabled(slot);
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        if (slot > 8) return;
        blockEntity.setSlotDisabled(slot, disabled);
        broadcastChanges();
    }

    @Override
    public boolean canDisableSlot(int slot) {
        return blockEntity.canDisableSlot(slot);
    }

    public static class ResultSlot extends ResourceHandlerSlot {
        public ResultSlot(BCItemHandler itemHandler, int slot, int x, int y) {
            super(itemHandler, itemHandler::set, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
