package com.github.minecraftschurlimods.bibliocraft.content.potionshelf;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class PotionShelfMenu extends BCMenu<PotionShelfBlockEntity> {
    public PotionShelfMenu(int id, Inventory inventory, PotionShelfBlockEntity blockEntity) {
        super(BCMenus.POTION_SHELF.get(), id, inventory, blockEntity);
    }

    public PotionShelfMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.POTION_SHELF.get(), id, inventory, buf);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                addSlot(new BCSlot(blockEntity, i * 4 + j, 53 + j * 18, 15 + i * 19));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }
}
