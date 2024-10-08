package com.github.minecraftschurlimods.bibliocraft.api;

import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.SequencedMap;
import java.util.function.Supplier;

/**
 * Register your own {@link BibliocraftWoodType}s here.
 *
 * This event is not cancelable. This event is fired on the {@link net.neoforged.fml.common.EventBusSubscriber.Bus#MOD mod event bus}.
 */
@SuppressWarnings("JavadocBlankLines")
public class RegisterBibliocraftWoodTypesEvent extends Event implements IModBusEvent {
    private final SequencedMap<ResourceLocation, BibliocraftWoodType> values;

    @ApiStatus.Internal
    public RegisterBibliocraftWoodTypesEvent(SequencedMap<ResourceLocation, BibliocraftWoodType> values) {
        this.values = values;
    }

    /**
     * Registers a new {@link BibliocraftWoodType}.
     *
     * @param id         The id of the wood type. Should be the id of the mod the wood type comes from, and the name of the wood type.
     * @param woodType   The vanilla {@link WoodType} associated with this wood type.
     * @param properties A supplier for the {@link BlockBehaviour.Properties} associated with this wood type. Typically, this is a copy of the wood type's planks' properties.
     * @param texture    The location of the wood type's planks texture, for use in datagen.
     * @param family     The {@link BlockFamily} for the associated wood type, for use in datagen.
     */
    public void register(ResourceLocation id, WoodType woodType, Supplier<BlockBehaviour.Properties> properties, ResourceLocation texture, BlockFamily family) {
        if (values.containsKey(id))
            throw new IllegalStateException("Wood type " + id + " is already registered");
        values.put(id, new BibliocraftWoodType(id, woodType, properties, texture, family));
    }
}
