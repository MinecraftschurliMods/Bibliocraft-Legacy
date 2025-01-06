package com.github.minecraftschurlimods.bibliocraft.util.init;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collection;

/**
 * Represents a group of {@link DeferredHolder}s.
 */
public interface GroupingDeferredHolder<R, T extends R> {
    /**
     * @return An immutable collection of all {@link DeferredHolder}s in this object.
     */
    Collection<DeferredHolder<R, T>> holders();

    /**
     * @return An immutable collection of values of all {@link DeferredHolder}s in this object.
     */
    Collection<T> values();

    /**
     * @return An immutable collection of ids of all {@link DeferredHolder}s in this object.
     */
    Collection<ResourceLocation> ids();
}
