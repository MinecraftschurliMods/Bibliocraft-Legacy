package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public class BibliocraftWoodTypeImpl implements BibliocraftWoodType {
    private final ResourceLocation id;
    private final WoodType woodType;
    private final Supplier<BlockBehaviour.Properties> properties;
    private final ResourceLocation texture;
    private final BlockFamily family;

    /**
     * Private constructor. Call {@link BibliocraftWoodTypeRegistry#register(ResourceLocation, WoodType, Supplier, ResourceLocation, BlockFamily)} instead.
     */
    public BibliocraftWoodTypeImpl(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family) {
        this.id = id;
        this.woodType = woodType;
        this.properties = properties;
        this.texture = texture;
        this.family = family;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public WoodType getWoodType() {
        return woodType;
    }

    @Override
    public Supplier<BlockBehaviour.Properties> getProperties() {
        return properties;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public BlockFamily getFamily() {
        return family;
    }
}
