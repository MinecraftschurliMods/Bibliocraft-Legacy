package com.github.minecraftschurlimods.bibliocraft.api.woodtype;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The registry for {@link BibliocraftWoodType}s. Get via {@link BibliocraftApi#getWoodTypeRegistry()}.
 * Register a new wood type by subscribing to {@link RegisterBibliocraftWoodTypesEvent} and calling {@code register(...)} on it.
 * Make sure to put this call behind a {@code ModList.isLoaded("bibliocraft")} check, and in a separate class, to prevent accidental classloading (like you would with client classes).
 */
public interface BibliocraftWoodTypeRegistry {
    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    BibliocraftWoodType get(ResourceLocation id);

    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    default BibliocraftWoodType get(String id) {
        return get(ResourceLocation.parse(id));
    }

    /**
     * @return An unmodifiable list of all registered wood types. Must only be called after registration is finished.
     */
    Collection<BibliocraftWoodType> getAll();
}
