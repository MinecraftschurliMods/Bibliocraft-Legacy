package com.github.minecraftschurlimods.bibliocraft.content.toolrack;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ToolRackMenu extends BCMenu<ToolRackBlockEntity> {
    public ToolRackMenu(int id, Inventory inventory, ToolRackBlockEntity blockEntity) {
        super(BCMenus.TOOL_RACK.get(), id, inventory, blockEntity);
    }

    public ToolRackMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.TOOL_RACK.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        addSlot(new BCSlot(blockEntity, 0, 53, 15));
        addSlot(new BCSlot(blockEntity, 1, 107, 15));
        addSlot(new BCSlot(blockEntity, 2, 53, 53));
        addSlot(new BCSlot(blockEntity, 3, 107, 53));
        addInventorySlots(inventory, 8, 84);
    }
}
