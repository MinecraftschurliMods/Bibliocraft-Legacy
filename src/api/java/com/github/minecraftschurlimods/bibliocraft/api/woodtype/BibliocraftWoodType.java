package com.github.minecraftschurlimods.bibliocraft.api.woodtype;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

/**
 * Holds all information Bibliocraft needs about a wood type.
 *
 * @param id The id of the wood type.
 * @param woodType   The corresponding vanilla {@link WoodType}.
 * @param properties A supplier for the wood type's {@link BlockBehaviour.Properties}.
 * @param texture    The location of the wood type's plank texture. Used in datagen.
 * @param family     The corresponding {@link BlockFamily}. Used in datagen.
 */
public record BibliocraftWoodType(
        ResourceLocation id,
        WoodType woodType,
        Supplier<BlockBehaviour.Properties> properties,
        ResourceLocation texture,
        BlockFamily family
) {
    /**
     * @return The namespace of the id of this wood type.
     */
    public String getNamespace() {
        return id().getNamespace();
    }

    /**
     * @return The path of the id of this wood type.
     */
    public String getPath() {
        return id().getPath();
    }

    /**
     * @return The wood type prefix used for registration. Keeps the mod id for cases when two mods add identically named wood types.
     */
    public String getRegistrationPrefix() {
        return getNamespace().equals("minecraft") ? getPath() : id().toString().replace(':', '_');
    }
}
