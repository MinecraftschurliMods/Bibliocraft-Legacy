package com.github.minecraftschurlimods.bibliocraft.util.holder;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Represents a group of {@link DeferredHolder}s.
 */
public interface GroupingDeferredHolder<R, T extends R> {
    String getName();

    /**
     * @return An immutable collection of all {@link DeferredHolder}s in this object.
     */
    Collection<DeferredHolder<R, T>> holders();

    Stream<DeferredHolder<R, T>> streamHolders();

    /**
     * @return An immutable collection of values of all {@link DeferredHolder}s in this object.
     */
    Collection<R> values();

    /**
     * @return An immutable collection of ids of all {@link DeferredHolder}s in this object.
     */
    Collection<Identifier> ids();

    Stream<R> stream();
}
