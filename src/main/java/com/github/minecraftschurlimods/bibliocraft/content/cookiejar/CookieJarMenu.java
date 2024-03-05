package com.github.minecraftschurlimods.bibliocraft.content.cookiejar;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenu;
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

    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        blockEntity.stopOpen(player);
    }
}
