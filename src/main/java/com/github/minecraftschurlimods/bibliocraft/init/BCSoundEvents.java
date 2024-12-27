package com.github.minecraftschurlimods.bibliocraft.init;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public interface BCSoundEvents {
    Holder<SoundEvent> CLOCK_CHIME        = BCRegistries.SOUND_EVENTS.register("clock_chime",        SoundEvent::createVariableRangeEvent);
    Holder<SoundEvent> CLOCK_TICK         = BCRegistries.SOUND_EVENTS.register("clock_tick",         SoundEvent::createVariableRangeEvent);
    Holder<SoundEvent> CLOCK_TOCK         = BCRegistries.SOUND_EVENTS.register("clock_tock",         SoundEvent::createVariableRangeEvent);
    Holder<SoundEvent> DESK_BELL          = BCRegistries.SOUND_EVENTS.register("desk_bell",          SoundEvent::createVariableRangeEvent);
    Holder<SoundEvent> TAPE_MEASURE_CLOSE = BCRegistries.SOUND_EVENTS.register("tape_measure_close", SoundEvent::createVariableRangeEvent);
    Holder<SoundEvent> TAPE_MEASURE_OPEN  = BCRegistries.SOUND_EVENTS.register("tape_measure_open",  SoundEvent::createVariableRangeEvent);

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
