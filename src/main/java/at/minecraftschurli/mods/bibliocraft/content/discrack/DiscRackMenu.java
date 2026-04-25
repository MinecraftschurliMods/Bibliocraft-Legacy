package at.minecraftschurli.mods.bibliocraft.content.discrack;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
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
            addItemHandlerSlot(i, 8 + i * 18, 34);
        }
        addInventorySlots(inventory, 8, 84);
    }
}
