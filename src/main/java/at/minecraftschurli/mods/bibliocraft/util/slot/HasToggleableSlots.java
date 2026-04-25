package at.minecraftschurli.mods.bibliocraft.util.slot;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/// Represents that the implementer has [ToggleableSlot]s.
public interface HasToggleableSlots {
    /// @param slot The index of the [ToggleableSlot] to query.
    /// @return Whether the [ToggleableSlot] with the given index is disabled.
    boolean isSlotDisabled(int slot);

    /// Sets the disabled flag of the [ToggleableSlot] at the given index.
    ///
    /// @param slot     The index of the [ToggleableSlot] to update.
    /// @param disabled The disabled flag to set.
    void setSlotDisabled(int slot, boolean disabled);

    /// @param slot The index of the [ToggleableSlot].
    /// @return Whether the [ToggleableSlot] can be disabled or not.
    boolean canDisableSlot(int slot);
}
