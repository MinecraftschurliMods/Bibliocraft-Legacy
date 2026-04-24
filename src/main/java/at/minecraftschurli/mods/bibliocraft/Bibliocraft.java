package at.minecraftschurli.mods.bibliocraft;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.init.BCRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = BibliocraftApi.MOD_ID)
public final class Bibliocraft {
    public Bibliocraft(IEventBus modBus) {
        BCRegistries.register(modBus);
    }
}
