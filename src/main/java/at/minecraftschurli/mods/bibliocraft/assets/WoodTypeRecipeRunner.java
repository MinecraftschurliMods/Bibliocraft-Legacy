package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/// Runs [WoodTypeRecipes] for the given wood types.
final class WoodTypeRecipeRunner extends RecipeProvider.Runner {
    private final List<BibliocraftWoodType> woodTypes;

    WoodTypeRecipeRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, List<BibliocraftWoodType> woodTypes) {
        super(packOutput, registries);
        this.woodTypes = woodTypes;
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new WoodTypeRecipes(registries, output, woodTypes);
    }

    @Override
    public String getName() {
        return "Bibliocraft wood type recipes";
    }
}
