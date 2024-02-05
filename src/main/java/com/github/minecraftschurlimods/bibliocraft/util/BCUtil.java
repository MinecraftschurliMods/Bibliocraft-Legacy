package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
        return merge(collection, Arrays.stream(others).toList());
    }

    /**
     * Merges two collections. Does not mutate the original collections.
     *
     * @param collections The collections to merge.
     * @param <T>         The type of the collection.
     * @return A new collection with the contents of the given collections.
     */
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T>... collections) {
        List<T> list = new ArrayList<>();
        for (Collection<T> collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /**
     * Merges two collections, disregarding their types. Does not mutate the original collections.
     *
     * @param collections The collections to merge.
     * @return A new collection with the contents of the given collections.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Collection<T> mergeRaw(Collection... collections) {
        List<T> list = new ArrayList<>();
        for (Collection collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /**
     * Shorthand to open a menu for the block entity at the given position and return {@link InteractionResult#SUCCESS}.
     *
     * @param player The player to open the menu for.
     * @param level  The level of the block entity.
     * @param pos    The position of the block entity.
     * @return {@link InteractionResult#SUCCESS}
     */
    public static InteractionResult openBEMenu(Player player, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MenuProvider menu && player instanceof ServerPlayer sp) {
            sp.openMenu(menu, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
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
