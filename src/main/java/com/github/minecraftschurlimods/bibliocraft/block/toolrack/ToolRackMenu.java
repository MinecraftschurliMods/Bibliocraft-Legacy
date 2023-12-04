package com.github.minecraftschurlimods.bibliocraft.block.toolrack;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ToolRackMenu extends BCMenu<ToolRackBlockEntity> {
    public ToolRackMenu(int id, Inventory inventory, ToolRackBlockEntity blockEntity) {
        super(BCMenuTypes.TOOL_RACK.get(), id, inventory, blockEntity);
    }

    public ToolRackMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenuTypes.TOOL_RACK.get(), id, inventory, data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return null;
    }

    @Override
    protected void addSlots(Inventory inventory, ToolRackBlockEntity blockEntity) {
        addSlot(new ToolRackSlot(blockEntity, 0, 53, 15));
        addSlot(new ToolRackSlot(blockEntity, 1, 107, 15));
        addSlot(new ToolRackSlot(blockEntity, 2, 53, 53));
        addSlot(new ToolRackSlot(blockEntity, 3, 107, 53));
        addInventorySlots(inventory, 8, 84);
    }

    public static class ToolRackSlot extends Slot {
        public ToolRackSlot(ToolRackBlockEntity blockEntity, int index, int x, int y) {
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
