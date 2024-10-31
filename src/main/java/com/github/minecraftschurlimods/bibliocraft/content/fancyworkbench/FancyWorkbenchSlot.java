package com.github.minecraftschurlimods.bibliocraft.content.fancyworkbench;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyWorkbenchSlot extends Slot {
    private final FancyWorkbenchBlockEntity blockEntity;

    public FancyWorkbenchSlot(FancyWorkbenchBlockEntity container, int slot, int x, int y) {
        super(container, slot, x, y);
        blockEntity = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && !blockEntity.isSlotDisabled(getContainerSlot());
    }
}
