package at.minecraftschurli.mods.bibliocraft.content.toolrack;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
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
        addItemHandlerSlot(0, 53, 15);
        addItemHandlerSlot(1, 107, 15);
        addItemHandlerSlot(2, 53, 53);
        addItemHandlerSlot(3, 107, 53);
        addInventorySlots(inventory, 8, 84);
    }
}
