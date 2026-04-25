package at.minecraftschurli.mods.bibliocraft.util.block;

import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class BCOutputItemHandler extends BCItemHandler {
    private final IntList outputs;

    public BCOutputItemHandler(int size, BCBlockEntity blockEntity, IntList outputs) {
        super(size, blockEntity);
        this.outputs = outputs;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return outputs.contains(index) ? blockEntity.getItemHandler().extract(index, resource, amount, transaction) : 0;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }
}
