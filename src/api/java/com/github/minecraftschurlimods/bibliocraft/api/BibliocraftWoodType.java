package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

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
