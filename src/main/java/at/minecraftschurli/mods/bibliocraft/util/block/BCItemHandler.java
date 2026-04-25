package at.minecraftschurli.mods.bibliocraft.util.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Consumer;

public class BCItemHandler extends StacksResourceHandler<ItemStack, ItemResource> {
    static final String ITEMS_TAG = "items";
    protected final BCBlockEntity blockEntity;

    public BCItemHandler(int size, BCBlockEntity blockEntity) {
        super(size, ItemStack.EMPTY, ItemStack.CODEC);
        this.blockEntity = blockEntity;
    }

    @Override
    public ItemResource getResourceFrom(ItemStack stack) {
        return ItemResource.of(stack);
    }

    @Override
    public int getAmountFrom(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    protected ItemStack getStackFrom(ItemResource resource, int amount) {
        return resource.toStack(amount);
    }

    @Override
    protected int getCapacity(int index, ItemResource resource) {
        return blockEntity.getCapacity(resource);
    }

    @Override
    protected ItemStack copyOf(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public boolean matches(ItemStack stack, ItemResource resource) {
        return resource.matches(stack);
    }

    @Override
    public void deserialize(ValueInput input) {
        int size = stacks.size();
        input.child(ITEMS_TAG).ifPresent(i -> modifyContents(stacks -> ContainerHelper.loadAllItems(i, stacks)));
        super.deserialize(input);
        size = Math.max(size, stacks.size());
        NonNullList<ItemStack> list = NonNullList.createWithCapacity(size);
        for (int i = 0; i < size; i++) {
            list.add(i < stacks.size() ? stacks.get(i) : ItemStack.EMPTY);
        }
        stacks = list;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return blockEntity.isValid(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
    }

    public boolean isEmpty(int index) {
        return stacks.get(index).isEmpty();
    }

    protected void fillFromComponent(ItemContainerContents containerContents) {
        modifyContents(containerContents::copyInto);
    }

    protected void modifyContents(Consumer<NonNullList<ItemStack>> modifier) {
        NonNullList<ItemStack> stacks = copyToList();
        modifier.accept(stacks);
        setStacks(stacks);
    }
}
