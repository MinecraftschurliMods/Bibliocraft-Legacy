package at.minecraftschurli.mods.bibliocraft.client.assets;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.assets.WoodTypeKey;
import at.minecraftschurli.mods.dynassetgen.api.DatagenGenerator;
import at.minecraftschurli.mods.dynassetgen.api.GenerationContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// Generates the models and translations of the wood types of one namespace.
///
/// @param namespace the namespace of the wood types
/// @param woodTypes the wood types to generate
record WoodTypeResources(String namespace, List<BibliocraftWoodType> woodTypes) implements DatagenGenerator {
    WoodTypeResources {
        woodTypes = List.copyOf(woodTypes);
    }

    @Override
    public void gatherData(DataGenerator.PackGenerator pack, CompletableFuture<HolderLookup.Provider> registries) {
        pack.addProvider(output -> new WoodTypeModels(output, woodTypes));
        pack.addProvider(output -> new WoodTypeLanguage(output, woodTypes));
    }

    @Override
    public Object createCacheKey(GenerationContext ctx) {
        return WoodTypeKey.of(namespace, woodTypes);
    }
}
