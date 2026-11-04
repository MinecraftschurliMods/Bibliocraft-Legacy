package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.dynassetgen.api.DatagenGenerator;
import at.minecraftschurli.mods.dynassetgen.api.GenerationContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// Generates the data of the wood types of one namespace: loot tables, recipes, and block and item tags.
///
/// @param namespace the namespace of the wood types
/// @param woodTypes the wood types to generate
record WoodTypeData(String namespace, List<BibliocraftWoodType> woodTypes) implements DatagenGenerator {
    WoodTypeData {
        woodTypes = List.copyOf(woodTypes);
    }

    @Override
    public void gatherData(DataGenerator.PackGenerator pack, CompletableFuture<HolderLookup.Provider> registries) {
        pack.addProvider(output -> new WoodTypeLootTables(output, registries, woodTypes));
        pack.addProvider(output -> new WoodTypeRecipeRunner(output, registries, woodTypes));
        pack.addProvider(output -> new WoodTypeBlockTags(output, registries, woodTypes));
        pack.addProvider(output -> new WoodTypeItemTags(output, registries, woodTypes));
    }

    @Override
    public Object createCacheKey(GenerationContext ctx) {
        return WoodTypeKey.of(namespace, woodTypes);
    }
}
