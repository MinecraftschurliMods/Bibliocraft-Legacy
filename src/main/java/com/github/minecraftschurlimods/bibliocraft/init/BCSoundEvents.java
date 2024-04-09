package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public interface BCSoundEvents {
    Supplier<SoundEvent> DESK_BELL = BCRegistries.SOUND_EVENTS.register("desk_bell", () -> SoundEvent.createVariableRangeEvent(BCUtil.modLoc("desk_bell")));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}