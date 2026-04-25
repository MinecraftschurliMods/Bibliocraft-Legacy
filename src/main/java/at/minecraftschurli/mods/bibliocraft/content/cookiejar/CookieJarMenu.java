package at.minecraftschurli.mods.bibliocraft.content.cookiejar;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
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

    /// @return The block entity this menu is associated with.
    public CookieJarBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int j = 0; j < 4; j++) {
            addItemHandlerSlot(j, j * 32 + 32, 22);
            addItemHandlerSlot(j + 4, j * 32 + 32, 51);
        }
        addInventorySlots(inventory, 8, 84);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        blockEntity.stopOpen(player);
    }
}
