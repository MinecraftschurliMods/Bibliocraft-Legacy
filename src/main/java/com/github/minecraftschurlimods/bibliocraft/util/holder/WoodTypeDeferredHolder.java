package com.github.minecraftschurlimods.bibliocraft.util.holder;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Registration utility that holds variants of a {@link DeferredHolder} for a given list of {@link BibliocraftWoodType}s.
 *
 * @param <R> The first type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code Block}.
 * @param <T> The second type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code DoorBlock}.
 */
@SuppressWarnings("unused")
public class WoodTypeDeferredHolder<R, T extends R> implements GroupingDeferredHolder<R, T> {
    private final Map<BibliocraftWoodType, DeferredHolder<R, T>> map = new LinkedHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param register The registry to use.
     * @param suffix   The suffix to use for the registry names to use. Will be combined with the wood type for the final registry name.
     * @param creator  A function of {@link BibliocraftWoodType} to {@code T}, responsible for actually creating the {@link DeferredHolder}.
     */
    public WoodTypeDeferredHolder(DeferredRegister<R> register, String suffix, Function<BibliocraftWoodType, ? extends T> creator) {
        for (BibliocraftWoodType type : BibliocraftApi.getWoodTypeRegistry().getAll()) {
            map.put(type, register.register(type.getRegistrationPrefix() + "_" + suffix, () -> creator.apply(type)));
        }
    }

    /**
     * @param type The {@link BibliocraftWoodType} to get the {@link DeferredHolder} for.
     * @return The {@link DeferredHolder} for the given {@link BibliocraftWoodType}.
     */
    public DeferredHolder<R, T> holder(BibliocraftWoodType type) {
        return map.get(type);
    }

    /**
     * @param type The {@link BibliocraftWoodType} to get the value of the {@link DeferredHolder} for.
     * @return The value of the {@link DeferredHolder} for the given {@link BibliocraftWoodType}. This is equivalent to calling {@code holder(type).get()}.
     */
    public T get(BibliocraftWoodType type) {
        DeferredHolder<R, T> holder = holder(type);
        if (holder == null) return null;
        return holder.get();
    }

    /**
     * @param type The {@link BibliocraftWoodType} to get the id of the {@link DeferredHolder} for.
     * @return The id of the {@link DeferredHolder} for the given {@link BibliocraftWoodType}. This is equivalent to calling {@code holder(type).getId()}.
     */
    public ResourceLocation id(BibliocraftWoodType type) {
        DeferredHolder<R, T> holder = holder(type);
        if (holder == null) return null;
        return holder.getId();
    }

    /**
     * @return An immutable map of all {@link BibliocraftWoodType} to {@link DeferredHolder} associations in this object.
     */
    public Map<BibliocraftWoodType, DeferredHolder<R, T>> map() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<DeferredHolder<R, T>> holders() {
        return map.values();
    }

    @Override
    public Collection<T> values() {
        return map.values().stream().map(DeferredHolder::get).toList();
    }

    @Override
    public Collection<ResourceLocation> ids() {
        return map.values().stream().map(DeferredHolder::getId).toList();
    }
}
