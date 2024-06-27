package com.github.minecraftschurlimods.bibliocraft.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public interface BCSoundEvents {
    Holder<SoundEvent> DESK_BELL = BCRegistries.SOUND_EVENTS.register("desk_bell", SoundEvent::createVariableRangeEvent);

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
