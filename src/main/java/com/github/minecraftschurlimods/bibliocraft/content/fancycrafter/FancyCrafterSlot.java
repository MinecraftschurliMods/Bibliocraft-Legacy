package com.github.minecraftschurlimods.bibliocraft.content.fancycrafter;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyCrafterSlot extends Slot {
    private final FancyCrafterBlockEntity blockEntity;

    public FancyCrafterSlot(FancyCrafterBlockEntity container, int slot, int x, int y) {
        super(container, slot, x, y);
        blockEntity = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && !blockEntity.isSlotDisabled(getContainerSlot());
    }
}
