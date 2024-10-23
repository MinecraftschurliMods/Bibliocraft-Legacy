package com.github.minecraftschurlimods.bibliocraft.content.slottedbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlottedBookSlot extends Slot {
    public SlottedBookSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.is(BCItems.SLOTTED_BOOK) && super.mayPlace(stack);
    }
}
