package at.minecraftschurli.mods.bibliocraft.util.slot;

import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.world.item.ItemStack;

/// Represents a slot that can be disabled or enabled. If disabled, items will not be placed inside the slot.
///
/// @param <T> The type of the owning block entity.
public class ToggleableSlot<T extends BCBlockEntity & HasToggleableSlots> extends BCSlot {
    public final T blockEntity;

    public ToggleableSlot(T blockEntity, int slot, int x, int y) {
        super(blockEntity, slot, x, y);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && !blockEntity.isSlotDisabled(getContainerSlot());
    }
}
