package com.github.minecraftschurlimods.bibliocraft.util.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Registration utility that holds variants of a {@link DeferredHolder} for a given list of {@link WoodType}s and each {@link DyeColor}.
 *
 * @param <R> The first type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code Block}.
 * @param <T> The second type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code DoorBlock}.
 */
@SuppressWarnings("unused")
public class ColoredWoodTypeDeferredHolder<R, T extends R> {
    private final Map<BibliocraftWoodType, ColoredDeferredHolder<R, T>> map = new LinkedHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param register The registry to use.
     * @param suffix   The suffix to use for the registry names to use. Will be combined with the wood type and the color for the final registry name.
     * @param creator  A function of {@link BibliocraftWoodType} and {@link DyeColor} to {@code T}, responsible for actually creating the {@link DeferredHolder}.
     */
    public ColoredWoodTypeDeferredHolder(DeferredRegister<R> register, String suffix, BiFunction<BibliocraftWoodType, DyeColor, ? extends T> creator) {
        for (BibliocraftWoodType type : BibliocraftWoodType.getAll()) {
            map.put(type, new ColoredDeferredHolder<>(register, type.getRegistrationPrefix() + "_" + suffix, color -> creator.apply(type, color)));
        }
    }

    /**
     * @param type The {@link BibliocraftWoodType} to get the {@link ColoredDeferredHolder} for.
     * @return The {@link ColoredDeferredHolder} for the given {@link BibliocraftWoodType}.
     */
    public ColoredDeferredHolder<R, T> element(BibliocraftWoodType type) {
        return map.get(type);
    }

    /**
     * @param type  The {@link BibliocraftWoodType} to get the {@link DeferredHolder} for.
     * @param color The {@link DyeColor} to get the {@link DeferredHolder} for.
     * @return The {@link DeferredHolder} for the given {@link BibliocraftWoodType}.
     */
    public DeferredHolder<R, T> holder(BibliocraftWoodType type, DyeColor color) {
        return map.get(type).holder(color);
    }

    /**
     * @param type  The {@link BibliocraftWoodType} to get the value of the {@link DeferredHolder} for.
     * @param color The {@link DyeColor} to get the value of the {@link DeferredHolder} for.
     * @return The value of the {@link DeferredHolder} for the given {@link BibliocraftWoodType}. This is equivalent to calling {@code holder(type).get()}.
     */
    public T get(BibliocraftWoodType type, DyeColor color) {
        return map.get(type).get(color);
    }

    /**
     * @param type  The {@link BibliocraftWoodType} to get the id of the {@link DeferredHolder} for.
     * @param color The {@link DyeColor} to get the id of the {@link DeferredHolder} for.
     * @return The id of the {@link DeferredHolder} for the given {@link BibliocraftWoodType}. This is equivalent to calling {@code holder(type).getId()}.
     */
    public ResourceLocation id(BibliocraftWoodType type, DyeColor color) {
        return map.get(type).id(color);
    }

    /**
     * @return An immutable collection of all {@link ColoredDeferredHolder}s in this object.
     */
    public Collection<ColoredDeferredHolder<R, T>> elements() {
        return map.values();
    }

    /**
     * @return An immutable collection of all {@link DeferredHolder}s in this object.
     */
    public Collection<DeferredHolder<R, T>> holders() {
        return elements().stream().flatMap(holder -> holder.holders().stream()).toList();
    }

    /**
     * @return An immutable collection of values of all {@link DeferredHolder}s in this object.
     */
    public Collection<T> values() {
        return elements().stream().flatMap(holder -> holder.values().stream()).toList();
    }

    /**
     * @return An immutable collection of ids of all {@link DeferredHolder}s in this object.
     */
    public Collection<ResourceLocation> ids() {
        return elements().stream().flatMap(holder -> holder.ids().stream()).toList();
    }

    /**
     * @return An immutable map of all {@link BibliocraftWoodType} to {@link DeferredHolder} associations in this object.
     */
    public Map<BibliocraftWoodType, ColoredDeferredHolder<R, T>> map() {
        return Collections.unmodifiableMap(map);
    }
}
