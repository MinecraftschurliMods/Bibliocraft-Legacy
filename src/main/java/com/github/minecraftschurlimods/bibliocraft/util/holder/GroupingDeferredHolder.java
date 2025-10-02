package com.github.minecraftschurlimods.bibliocraft.util.holder;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Represents a group of {@link DeferredHolder}s.
 */
public interface GroupingDeferredHolder<R, T extends R> {
    /**
     * @return The name of the things contained in this group.
     */
    String getName();

    /**
     * @return An immutable collection of all {@link DeferredHolder}s in this object.
     */
    @Unmodifiable Collection<DeferredHolder<R, T>> holders();

    /**
     * @return A {@link Stream} of all {@link DeferredHolder}s in this object.
     */
    Stream<DeferredHolder<R, T>> streamHolders();

    /**
     * @return An immutable collection of values of all {@link DeferredHolder}s in this object.
     */
    @Unmodifiable Collection<R> values();

    /**
     * @return An immutable collection of ids of all {@link DeferredHolder}s in this object.
     */
    @Unmodifiable Collection<ResourceLocation> ids();

    /**
     * @return A {@link Stream} of all values of all {@link DeferredHolder}s in this object.
     */
    Stream<R> stream();
}
