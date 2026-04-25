package at.minecraftschurli.mods.bibliocraft.util.block;

import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

// TODO PR this to neo?
public class LimitedAccessItemHandler extends DelegatingResourceHandler<ItemResource> {
    private final AccessPredicate accessPredicate;

    public LimitedAccessItemHandler(ResourceHandler<ItemResource> delegate, AccessPredicate accessPredicate) {
        super(delegate);
        this.accessPredicate = accessPredicate;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (accessPredicate.canAccess(convertIndex(index), AccessPredicate.Mode.EXTRACT)) {
            return super.extract(index, resource, amount, transaction);
        }
        return 0;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (accessPredicate.canAccess(convertIndex(index), AccessPredicate.Mode.INSERT)) {
            return super.insert(index, resource, amount, transaction);
        }
        return 0;
    }

    @FunctionalInterface
    public interface AccessPredicate {
        boolean canAccess(int index, AccessPredicate.Mode mode);

        enum Mode {
            INSERT, EXTRACT
        }
    }
}
