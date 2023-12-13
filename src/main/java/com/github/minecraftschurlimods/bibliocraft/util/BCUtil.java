package com.github.minecraftschurlimods.bibliocraft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class BCUtil {
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T> collection, T... others) {
        List<T> list = new ArrayList<>();
        list.addAll(collection);
        list.addAll(Arrays.stream(others).toList());
        return list;
    }
}
