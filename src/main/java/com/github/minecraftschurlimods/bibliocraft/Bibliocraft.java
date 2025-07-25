package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = BibliocraftApi.MOD_ID)
public final class Bibliocraft {
    public Bibliocraft(IEventBus bus) {
        BCEventHandler.init(bus);
    }
}
