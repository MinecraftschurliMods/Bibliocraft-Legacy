package at.minecraftschurli.mods.bibliocraft.util.block;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.Consumer;

public class BCItemHandler extends ItemStacksResourceHandler {
    static final String ITEMS_TAG = "items";
    private final ValidityPredicate validityPredicate;
    private final CapacityProvider capacityProvider;
    private final ChangeListener changeListener;

    public BCItemHandler(int size, ValidityPredicate validityPredicate, CapacityProvider capacityProvider, ChangeListener changeListener) {
        super(size);
        this.validityPredicate = validityPredicate;
        this.capacityProvider = capacityProvider;
        this.changeListener = changeListener;
    }

    /// Overridden for legacy compat
    @Override
    public void deserialize(ValueInput input) {
        if (input.keySet().contains(StacksResourceHandler.VALUE_IO_KEY)) {
            super.deserialize(input);
        } else {
            loadLegacyInventory(input);
        }
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return capacityProvider.getCapacity(resource);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return validityPredicate.isValid(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        changeListener.onChanged();
    }

    public boolean isEmpty(int index) {
        return stacks.get(index).isEmpty();
    }

    protected void fillFromComponent(ItemContainerContents containerContents) {
        modifyContents(containerContents::copyInto);
    }

    private void loadLegacyInventory(ValueInput input) {
        input.child(ITEMS_TAG).ifPresent(i -> modifyContents(stacks -> ContainerHelper.loadAllItems(i, stacks)));
    }

    protected void modifyContents(Consumer<NonNullList<ItemStack>> modifier) {
        NonNullList<ItemStack> stacks = copyToList();
        modifier.accept(stacks);
        setStacks(stacks);
    }

    public LimitedAccessItemHandler forOutput(IntList outputs) {
        return new LimitedAccessItemHandler(this, (index, mode) -> mode == LimitedAccessItemHandler.AccessPredicate.Mode.EXTRACT && outputs.contains(index));
    }

    public LimitedAccessItemHandler forInput(IntList inputs) {
        return new LimitedAccessItemHandler(this, (index, mode) -> mode == LimitedAccessItemHandler.AccessPredicate.Mode.INSERT && inputs.contains(index));
    }

    @FunctionalInterface
    public interface ValidityPredicate {
        boolean isValid(int slot, ItemResource resource);
    }

    @FunctionalInterface
    public interface CapacityProvider {
        int getCapacity(ItemResource resource);
    }

    @FunctionalInterface
    public interface ChangeListener {
        void onChanged();
    }
}
