package com.github.minecraftschurlimods.bibliocraft.content.cookiejar;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class CookieJarMenu extends BCMenu<CookieJarBlockEntity> {
    public CookieJarMenu(int id, Inventory inventory, CookieJarBlockEntity blockEntity) {
        super(BCMenus.COOKIE_JAR.get(), id, inventory, blockEntity);
        blockEntity.startOpen(inventory.player);
    }

    public CookieJarMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.COOKIE_JAR.get(), id, inventory, buf);
        blockEntity.startOpen(inventory.player);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int j = 0; j < 4; j++) {
            addSlot(new BCSlot(blockEntity, j, j * 32 + 32, 22));
            addSlot(new BCSlot(blockEntity, j + 4, j * 32 + 32, 51));
        }
        addInventorySlots(inventory, 8, 84);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        blockEntity.stopOpen(player);
    }
}
