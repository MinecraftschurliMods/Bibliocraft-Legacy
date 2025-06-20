package com.github.minecraftschurlimods.bibliocraft.util;

/**
 * Represents that the implementer has {@link TogglableSlot}s.
 */
public interface HasTogglableSlots {
    /**
     * @param index The index of the {@link TogglableSlot} to query.
     * @return Whether the {@link TogglableSlot} with the given index is disabled.
     */
    boolean isSlotDisabled(int index);

    /**
     * Sets the disabled flag of the {@link TogglableSlot} at the given index.
     *
     * @param index    The index of the {@link TogglableSlot} to update.
     * @param disabled The disabled flag to set.
     */
    void setSlotDisabled(int index, boolean disabled);
}
