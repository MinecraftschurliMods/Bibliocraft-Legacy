package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.client.assets.BibliocraftClientDynamicAssets;
import at.minecraftschurli.mods.dynassetgen.api.DynamicPacks;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class BibliocraftDynamicAssets {
    private static @Nullable DynamicPacks packs;

    private BibliocraftDynamicAssets() {}

    public static void init(IEventBus modBus) {
        packs = DynamicPacks.create(Identifier.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "compat"), modBus);
    }

    private static void register(DynamicPacks packs, String namespace, List<BibliocraftWoodType> woodTypes) {
        packs.data(namespace)
            .onlyIfModsLoaded(namespace)
            .add(Identifier.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "wood_types"), new WoodTypeData(namespace, woodTypes));
    }

    public static void register(Map<String, List<BibliocraftWoodType>> byNamespace) {
        byNamespace.forEach((namespace, woodTypesForNamespace) -> {
            if (packs == null) {
                throw new IllegalStateException("Tried to register BibliocraftDynamicAssets before init() was called!");
            }
            BibliocraftDynamicAssets.register(packs, namespace, woodTypesForNamespace);
            if (FMLEnvironment.getDist().isClient()) {
                BibliocraftClientDynamicAssets.register(packs, namespace, woodTypesForNamespace);
            }
        });
    }
}
