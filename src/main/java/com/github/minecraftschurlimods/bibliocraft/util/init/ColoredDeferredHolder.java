package com.github.minecraftschurlimods.bibliocraft.util.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Registration utility that holds variants of a {@link DeferredHolder} for each {@link DyeColor}.
 *
 * @param <R> The first type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code Block}.
 * @param <T> The second type of the {@link DeferredHolder} to use. For example, for wrapping a {@code DeferredHolder<Block, DoorBlock>}, this would be {@code DoorBlock}.
 */
@SuppressWarnings("unused")
public class ColoredDeferredHolder<R, T extends R> {
    private final Map<DyeColor, DeferredHolder<R, T>> map = new LinkedHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param register The registry to use.
     * @param suffix   The suffix to use for the registry names to use. Will be combined with the wood type for the final registry name.
     * @param creator  A function of {@link DyeColor} to {@code T}, responsible for actually creating the {@link DeferredHolder}.
     */
    public ColoredDeferredHolder(DeferredRegister<R> register, String suffix, Function<DyeColor, ? extends T> creator) {
        for (DyeColor type : DyeColor.values()) {
            map.put(type, register.register(type.getName() + "_" + suffix, () -> creator.apply(type)));
        }
    }

    /**
     * @param color The {@link DyeColor} to get the {@link DeferredHolder} for.
     * @return The {@link DeferredHolder} for the given {@link DyeColor}.
     */
    public DeferredHolder<R, T> holder(DyeColor color) {
        return map.get(color);
    }

    /**
     * @param color The {@link DyeColor} to get the value of the {@link DeferredHolder} for.
     * @return The value of the {@link DeferredHolder} for the given {@link DyeColor}. This is equivalent to calling {@code holder(color).get()}.
     */
    public T get(DyeColor color) {
        return map.get(color).get();
    }

    /**
     * @param color The {@link DyeColor} to get the id of the {@link DeferredHolder} for.
     * @return The id of the {@link DeferredHolder} for the given {@link DyeColor}. This is equivalent to calling {@code holder(color).getId()}.
     */
    public ResourceLocation id(DyeColor color) {
        return map.get(color).getId();
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
     * @return An immutable map of all {@link DyeColor} to {@link DeferredHolder} associations in this object.
     */
    public Map<DyeColor, DeferredHolder<R, T>> map() {
        return Collections.unmodifiableMap(map);
    }
}
