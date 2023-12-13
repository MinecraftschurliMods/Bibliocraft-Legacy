package com.github.minecraftschurlimods.bibliocraft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility class holding various helper methods.
 */
public final class BCUtil {
    /**
     * Merges a given collection with the given elements. Does not mutate the original collection.
     *
     * @param collection The collection to merge.
     * @param others     The other elements to merge.
     * @param <T>        The type of the collection.
     * @return A new collection with the contents of the given collection and the given vararg parameter.
     */
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T> collection, T... others) {
        List<T> list = new ArrayList<>();
        list.addAll(collection);
        list.addAll(Arrays.stream(others).toList());
        return list;
    }
}
