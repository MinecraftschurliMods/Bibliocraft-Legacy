package com.github.minecraftschurlimods.bibliocraft.util.slot;

/**
 * Represents that the implementer has {@link ToggleableSlot}s.
 */
public interface HasToggleableSlots {
    /**
     * @param index The index of the {@link ToggleableSlot} to query.
     * @return Whether the {@link ToggleableSlot} with the given index is disabled.
     */
    boolean isSlotDisabled(int index);

    /**
     * Sets the disabled flag of the {@link ToggleableSlot} at the given index.
     *
     * @param index    The index of the {@link ToggleableSlot} to update.
     * @param disabled The disabled flag to set.
     */
    void setSlotDisabled(int index, boolean disabled);

    /**
     * @param index The index of the {@link ToggleableSlot}.
     * @return Whether the {@link ToggleableSlot} can be disabled or not.
     */
    default boolean canDisableSlot(int index) {
        return true;
    }
}
