package com.github.minecraftschurlimods.bibliocraft.util.slot;

/**
 * Represents that the implementer has {@link ToggleableSlot}s.
 */
public interface HasToggleableSlots {
    /**
     * @param slot The index of the {@link ToggleableSlot} to query.
     * @return Whether the {@link ToggleableSlot} with the given index is disabled.
     */
    boolean isSlotDisabled(int slot);

    /**
     * Sets the disabled flag of the {@link ToggleableSlot} at the given index.
     *
     * @param slot     The index of the {@link ToggleableSlot} to update.
     * @param disabled The disabled flag to set.
     */
    void setSlotDisabled(int slot, boolean disabled);

    /**
     * @param slot The index of the {@link ToggleableSlot}.
     * @return Whether the {@link ToggleableSlot} can be disabled or not.
     */
    boolean canDisableSlot(int slot);
}
