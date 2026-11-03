package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import net.neoforged.fml.ModList;

import java.util.List;

/// The cache key of the runtime generators of one wood type namespace. It changes when Bibliocraft, the namespace's mod or
/// any of its wood types changes, so the cached output is regenerated.
///
/// @param bibliocraftVersion  the version of Bibliocraft, or an empty string if it is not loaded
/// @param namespaceModVersion the version of the namespace's mod, or an empty string if it is not loaded
/// @param woodTypes           the `id|texture` of every wood type
public record WoodTypeKey(String bibliocraftVersion, String namespaceModVersion, List<String> woodTypes) {
    /// Creates the key for the wood types of one namespace.
    ///
    /// @param namespace the namespace of the wood types
    /// @param woodTypes the wood types
    /// @return the cache key
    public static WoodTypeKey of(String namespace, List<BibliocraftWoodType> woodTypes) {
        return new WoodTypeKey(
                versionOf(BibliocraftApi.MOD_ID),
                versionOf(namespace),
                woodTypes.stream().map(woodType -> woodType.id() + "|" + woodType.texture()).toList()
        );
    }

    private static String versionOf(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("");
    }
}
