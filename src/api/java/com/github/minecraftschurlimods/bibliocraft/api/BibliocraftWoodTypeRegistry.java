package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * The registry for {@link BibliocraftWoodType}s. Register a new wood type by subscribing to {@link RegisterBibliocraftWoodTypesEvent} and calling {@code register(...)} on it.
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
        return get(new ResourceLocation(id));
    }

    /**
     * @param woodType The vanilla wood type to get the wood type for.
     * @return The wood type for the given vanilla wood type. May return null if no wood type for it exists.
     */
    @Nullable
    default BibliocraftWoodType get(WoodType woodType) {
        return get(woodType.name());
    }

    /**
     * @return An unmodifiable list of all registered wood types. Must only be called after registration is finished.
     */
    Collection<BibliocraftWoodType> getAll();
}
