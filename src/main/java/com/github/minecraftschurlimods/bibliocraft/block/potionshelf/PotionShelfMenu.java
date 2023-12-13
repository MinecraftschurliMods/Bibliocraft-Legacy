package com.github.minecraftschurlimods.bibliocraft.block.potionshelf;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
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
                addSlot(new TagLimitedSlot(blockEntity, i * 4 + j, 53 + j * 18, 15 + i * 19, BCTags.Items.POTION_SHELF_POTIONS));
            }
        }
        addInventorySlots(inventory, 8, 84);
    }
}
