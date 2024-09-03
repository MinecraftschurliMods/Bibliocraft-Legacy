package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class holding various helper methods.
 */
public final class BCUtil {
    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "minecraft" namespace and the given path.
     */
    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "c" namespace and the given path.
     */
    public static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "bibliocraft" namespace and the given path.
     */
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, path);
    }

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
     * Extends the given {@link List} to the given size, filling the new spaces with the provided values.
     * @param list The {@link List} to extend.
     * @param size The size the {@link List} should be extended to.
     * @param fill The value to fill the new spots with.
     * @return The given {@link List}, extended to the given size.
     * @param <T> The generic type of the list.
     */
    public static <T> List<T> extend(List<T> list, int size, T fill) {
        for (int i = list.size(); i < size; i++) {
            list.add(fill);
        }
        return list;
    }

    /**
     * Shorthand to open a menu for the block entity at the given position.
     *
     * @param player The player to open the menu for.
     * @param level  The level of the block entity.
     * @param pos    The position of the block entity.
     */
    public static void openBEMenu(Player player, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MenuProvider mp && player instanceof ServerPlayer sp) {
            sp.openMenu(mp, buf -> buf.writeBlockPos(pos));
        }
    }

    /**
     * @param valuesSupplier The enum's {@code values()} method.
     * @return An enum codec.
     * @param <E> The enum type.
     */
    public static <E extends Enum<E> & StringRepresentable> Codec<E> enumCodec(Supplier<E[]> valuesSupplier) {
        return StringRepresentable.fromEnum(valuesSupplier);
    }

    /**
     * @param valuesSupplier  The enum's {@code values()} method.
     * @param ordinalSupplier The enum's {@code ordinal()} method.
     * @return An enum stream codec.
     * @param <E> The enum type.
     */
    public static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(Supplier<E[]> valuesSupplier, Function<E, Integer> ordinalSupplier) {
        return ByteBufCodecs.INT.map(e -> valuesSupplier.get()[e], ordinalSupplier);
    }
}
