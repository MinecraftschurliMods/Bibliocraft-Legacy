package com.github.minecraftschurlimods.bibliocraft.content.discrack;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class DiscRackMenu extends BCMenu<DiscRackBlockEntity> {
    public DiscRackMenu(int id, Inventory inventory, DiscRackBlockEntity blockEntity) {
        super(BCMenus.DISC_RACK.get(), id, inventory, blockEntity);
    }

    public DiscRackMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.DISC_RACK.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            addSlot(new BCSlot(blockEntity, i, 8 + i * 18, 34));
        }
        addInventorySlots(inventory, 8, 84);
    }
}
