package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Registration utility that holds variants of a {@link DeferredHolder} for a given list of {@link WoodType}s.
 *
 * @param <R> The first type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code Block}.
 * @param <T> The second type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code DoorBlock}.
 */
@SuppressWarnings("unused")
public class WoodTypeDeferredHolder<R, T extends R> {
    private final Map<WoodType, DeferredHolder<R, T>> map = new LinkedHashMap<>();
    private final List<WoodType> types;

    /**
     * Creates a new instance of this class.
     *
     * @param register The registry to use.
     * @param suffix   The suffix to use for the registry names to use. Will be combined with the wood type for the final registry name.
     * @param types    The wood types to use.
     * @param creator  A function of {@link WoodType} to {@code T}, responsible for actually creating the {@link DeferredHolder}.
     */
    public WoodTypeDeferredHolder(DeferredRegister<R> register, String suffix, List<WoodType> types, Function<WoodType, ? extends T> creator) {
        this.types = types;
        for (WoodType type : types) {
            map.put(type, register.register(type.name() + "_" + suffix, () -> creator.apply(type)));
        }
    }

    /**
     * @param type The {@link WoodType} to get the {@link DeferredHolder} for.
     * @return The {@link DeferredHolder} for the given {@link WoodType}.
     */
    public DeferredHolder<R, T> holder(WoodType type) {
        return map.get(type);
    }

    /**
     * @param type The {@link WoodType} to get the value of the {@link DeferredHolder} for.
     * @return The value of the {@link DeferredHolder} for the given {@link WoodType}. This is equivalent to calling {@code holder(type).get()}.
     */
    public T get(WoodType type) {
        return map.get(type).get();
    }

    /**
     * @param type The {@link WoodType} to get the id of the {@link DeferredHolder} for.
     * @return The id of the {@link DeferredHolder} for the given {@link WoodType}. This is equivalent to calling {@code holder(type).getId()}.
     */
    public ResourceLocation id(WoodType type) {
        return map.get(type).getId();
    }

    /**
     * @return An immutable collection of all {@link DeferredHolder}s in this object.
     */
    public Collection<DeferredHolder<R, T>> holders() {
        return map.values();
    }

    /**
     * @return An immutable collection of values of all {@link DeferredHolder}s in this object.
     */
    public Collection<T> values() {
        return map.values().stream().map(DeferredHolder::get).toList();
    }

    /**
     * @return An immutable collection of ids of all {@link DeferredHolder}s in this object.
     */
    public Collection<ResourceLocation> ids() {
        return map.values().stream().map(DeferredHolder::getId).toList();
    }

    /**
     * @return An immutable list of all {@link WoodType}s this object has associations for.
     */
    public List<WoodType> types() {
        return Collections.unmodifiableList(types);
    }

    /**
     * @return An immutable map of all {@link WoodType} to {@link DeferredHolder} associations in this object.
     */
    public Map<WoodType, DeferredHolder<R, T>> map() {
        return Collections.unmodifiableMap(map);
    }
}
