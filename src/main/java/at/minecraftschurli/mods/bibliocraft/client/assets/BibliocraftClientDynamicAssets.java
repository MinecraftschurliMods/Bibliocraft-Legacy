package at.minecraftschurli.mods.bibliocraft.client.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.dynassetgen.api.DynamicPacks;
import net.minecraft.resources.Identifier;

import java.util.List;

public final class BibliocraftClientDynamicAssets {
    private BibliocraftClientDynamicAssets() {}

    public static void register(DynamicPacks packs, String namespace, List<BibliocraftWoodType> woodTypes) {
        packs.assets(namespace)
            .onlyIfModsLoaded(namespace)
            .add(Identifier.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "wood_types"), new WoodTypeResources(namespace, woodTypes));
    }
}
