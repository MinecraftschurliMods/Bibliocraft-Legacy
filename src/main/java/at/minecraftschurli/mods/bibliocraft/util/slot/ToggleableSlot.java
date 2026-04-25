package at.minecraftschurli.mods.bibliocraft.util.slot;

import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.function.IntPredicate;

/// Represents a slot that can be disabled or enabled. If disabled, items will not be placed inside the slot.
public class ToggleableSlot extends ResourceHandlerSlot {
    private final IntPredicate slotDisabledPredicate;

    public ToggleableSlot(BCItemHandler itemHandler, IntPredicate slotDisabledPredicate, int slot, int x, int y) {
        super(itemHandler, itemHandler::set, slot, x, y);
        this.slotDisabledPredicate = slotDisabledPredicate;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && !slotDisabledPredicate.test(getContainerSlot());
    }
}
