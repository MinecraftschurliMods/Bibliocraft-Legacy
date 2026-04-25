package at.minecraftschurli.mods.bibliocraft.util.block;

import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class BCInputItemHandler extends BCItemHandler {
    private final IntList slots;

    public BCInputItemHandler(int size, BCBlockEntity blockEntity, IntList slots) {
        super(size, blockEntity);
        this.slots = slots;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return slots.contains(index) ? blockEntity.getItemHandler().insert(index, resource, amount, transaction) : 0;
    }
}
