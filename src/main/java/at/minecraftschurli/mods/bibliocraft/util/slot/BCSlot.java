package at.minecraftschurli.mods.bibliocraft.util.slot;

import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class BCSlot extends ResourceHandlerSlot {
    public BCSlot(BCBlockEntity blockEntity, int slot, int x, int y) {
        BCItemHandler itemHandler = blockEntity.getItemHandler();
        super(itemHandler, itemHandler::set, slot, x, y);
    }
}
