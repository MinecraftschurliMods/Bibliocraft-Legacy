package at.minecraftschurli.mods.bibliocraft.content.shelf;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ShelfMenu extends BCMenu<ShelfBlockEntity> {
    public ShelfMenu(int id, Inventory inventory, ShelfBlockEntity blockEntity) {
        super(BCMenus.SHELF.get(), id, inventory, blockEntity);
    }

    public ShelfMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.SHELF.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        addItemHandlerSlot(0, 53, 15);
        addItemHandlerSlot(1, 107, 15);
        addItemHandlerSlot(2, 53, 53);
        addItemHandlerSlot(3, 107, 53);
        addInventorySlots(inventory, 8, 84);
    }
}
