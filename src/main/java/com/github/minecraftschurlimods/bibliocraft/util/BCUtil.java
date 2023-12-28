package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility class holding various helper methods.
 */
@SuppressWarnings("DataFlowIssue")
public final class BCUtil {
    private static final String TAG_COLOR = "color";
    private static final String TAG_DISPLAY = "display";

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

    //region Static variants of the methods in DyeableLeatherItem.
    public static boolean hasNBTColor(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return false;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        return tag != null && tag.contains(TAG_COLOR, Tag.TAG_ANY_NUMERIC);
    }

    public static int getNBTColor(ItemStack stack, int other) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return other;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        return tag != null && tag.contains(TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? tag.getInt(TAG_COLOR) : other;
    }

    public static void clearNBTColor(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        if (tag != null && tag.contains(TAG_COLOR)) {
            tag.remove(TAG_COLOR);
        }
    }

    public static void setNBTColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, color);
    }
    //endregion
}
