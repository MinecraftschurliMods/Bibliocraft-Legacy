package com.github.minecraftschurlimods.bibliocraft.util.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Specialization of {@link ItemStackHandler} that respects placement restrictions of a {@link BCBlockEntity}.
 */
public class BCItemHandler extends ItemStackHandler {
    private final BCBlockEntity blockEntity;

    public BCItemHandler(int size, BCBlockEntity blockEntity) {
        super(size);
        this.blockEntity = blockEntity;
    }

    public NonNullList<ItemStack> getItems() {
        return stacks;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        BlockState state = blockEntity.getBlockState();
        blockEntity.setChanged();
        blockEntity.level().sendBlockUpdated(blockEntity.getBlockPos(), state, state, Block.UPDATE_ALL);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!blockEntity.canPlaceItem(slot, stack)) return false;
        ItemStack stackInSlot = getStackInSlot(slot);
        return (stackInSlot.isEmpty() || ItemStack.isSameItemSameComponents(stackInSlot, stack)) && stackInSlot.getCount() + stack.getCount() <= blockEntity.getMaxStackSize();
    }
}
