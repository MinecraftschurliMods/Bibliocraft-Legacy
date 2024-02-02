package com.github.minecraftschurlimods.bibliocraft.api;

import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/**
 * The registry for {@link BibliocraftWoodType}s. Register a new wood type by calling {@code BibliocraftWoodTypeRegistry.get().register(...)} from your mod's constructor.
 * Make sure to put this call behind a {@code ModList.isLoaded("bibliocraft")} check, and in a separate class, to prevent accidental classloading (like you would with client classes).
 */
public interface BibliocraftWoodTypeRegistry {
    /**
     * Registers a new wood type. Must be called during the mod constructor.
     *
     * @param id         The id of the wood type. Should be the id of the mod the wood type comes from, and the name of the wood type.
     * @param woodType   The vanilla {@link WoodType} associated with this wood type.
     * @param properties A supplier for the {@link BlockBehaviour.Properties} associated with this wood type. Typically, this is a copy of the wood type's planks' properties.
     * @param texture    The location of the wood type's planks texture, for use in datagen.
     * @param family     The {@link BlockFamily} for the associated wood type, for use in datagen.
     */
    void register(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family);

    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    BibliocraftWoodType get(ResourceLocation id);

    /**
     * @return An unmodifiable list of all registered wood types. Must only be called after registration is finished.
     */
    List<BibliocraftWoodType> getAll();

    /**
     * @return The only instance of this class.
     */
    static BibliocraftWoodTypeRegistry get() {
        return BibliocraftWoodTypeRegistryImpl.get();
    }

    /**
     * @param id The id of the wood type to get.
     * @return The wood type with the given id. May return null if no wood type with the given id exists.
     */
    @Nullable
    default BibliocraftWoodType get(String id) {
        return get(new ResourceLocation(id));
    }
}
