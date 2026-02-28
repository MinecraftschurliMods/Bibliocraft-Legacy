package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;

public abstract class WrappingCustomBlockStateModelBuilder extends CustomBlockStateModelBuilder {
    protected final MultiVariant wrapped;

    protected WrappingCustomBlockStateModelBuilder(MultiVariant wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public CustomBlockStateModelBuilder with(VariantMutator variantMutator) {
        return this;
    }

    @Override
    public CustomBlockStateModelBuilder with(UnbakedMutator variantMutator) {
        return this;
    }

    @Override
    public CustomUnbakedBlockStateModel toUnbaked() {
        return null;
    }
}
