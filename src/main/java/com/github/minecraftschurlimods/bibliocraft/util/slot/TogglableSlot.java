package com.github.minecraftschurlimods.bibliocraft.util.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Represents a slot that can be disabled or enabled. If disabled, items will not be placed inside the slot.
 *
 * @param <T> The type of the owning block entity.
 */
public class TogglableSlot<T extends Container & HasTogglableSlots> extends Slot {
    public final T container;

    public TogglableSlot(T container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && !container.isSlotDisabled(getContainerSlot());
    }
}
