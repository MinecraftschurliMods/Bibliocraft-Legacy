package at.minecraftschurli.mods.bibliocraft.content.label;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class LabelMenu extends BCMenu<LabelBlockEntity> {
    public LabelMenu(int id, Inventory inventory, LabelBlockEntity blockEntity) {
        super(BCMenus.LABEL.get(), id, inventory, blockEntity);
    }

    public LabelMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.LABEL.get(), id, inventory, data);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        addSlot(new Slot(blockEntity, 0, 80, 45));
        addSlot(new Slot(blockEntity, 1, 35, 26));
        addSlot(new Slot(blockEntity, 2, 125, 26));
        addInventorySlots(inventory, 8, 84);
    }
}
