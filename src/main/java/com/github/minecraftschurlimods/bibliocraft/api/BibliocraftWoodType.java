package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

/**
 * This class represents a wood type in Bibliocraft. All wooden blocks exist in every wood type.
 * Register a new wood type by calling {@link BibliocraftWoodTypeRegistry#register(ResourceLocation, WoodType, Supplier, ResourceLocation, BlockFamily)} from your mod's constructor.
 * Make sure to put this call behind a {@code ModList.isLoaded("bibliocraft")} check, and in a separate class, to prevent accidental classloading (like you would with client classes).
 */
public interface BibliocraftWoodType {
    /**
     * @return The id of this wood type.
     */
    ResourceLocation getId();

    /**
     * @return The vanilla {@link WoodType} of this wood type.
     */
    WoodType getWoodType();

    /**
     * @return A supplier for the {@link BlockBehaviour.Properties} of this wood type.
     */
    Supplier<BlockBehaviour.Properties> getProperties();

    /**
     * @return The location of the block texture of this wood type, for use in datagen.
     */
    ResourceLocation getTexture();

    /**
     * @return The {@link BlockFamily} of this wood type, for use in datagen.
     */
    BlockFamily getFamily();

    /**
     * @return The namespace of the id of this wood type.
     */
    default String getNamespace() {
        return getId().getNamespace();
    }

    /**
     * @return The path of the id of this wood type.
     */
    default String getPath() {
        return getId().getPath();
    }

    /**
     * @return The wood type prefix used for registration. Keeps the mod id for cases when two mods add identically named wood types.
     */
    default String getRegistrationPrefix() {
        return getNamespace().equals("minecraft") ? getPath() : getId().toString().replace(':', '_');
    }
}
