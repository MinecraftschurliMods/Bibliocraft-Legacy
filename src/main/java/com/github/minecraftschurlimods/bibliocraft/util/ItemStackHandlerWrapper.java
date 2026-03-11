package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemStackHandlerWrapper extends ItemStackHandler {
    private final ItemStackHandler wrapped;

    public ItemStackHandlerWrapper(ItemStackHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void setSize(int size) {
        wrapped.setSize(size);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return wrapped.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        wrapped.deserializeNBT(provider, nbt);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        wrapped.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return wrapped.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return wrapped.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return wrapped.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return wrapped.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return wrapped.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return wrapped.isItemValid(slot, stack);
    }
}
