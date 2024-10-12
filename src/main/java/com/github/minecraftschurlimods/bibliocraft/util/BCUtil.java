package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
     *
     * @param list The {@link List} to extend.
     * @param size The size the {@link List} should be extended to.
     * @param fill The value to fill the new spots with.
     * @param <T>  The generic type of the list.
     * @return The given {@link List}, extended to the given size.
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
     * @param <E>            The enum type.
     * @return An enum codec.
     */
    public static <E extends Enum<E> & StringRepresentable> Codec<E> enumCodec(Supplier<E[]> valuesSupplier) {
        return StringRepresentable.fromEnum(valuesSupplier);
    }

    /**
     * @param valuesSupplier  The enum's {@code values()} method.
     * @param ordinalSupplier The enum's {@code ordinal()} method.
     * @param <E>             The enum type.
     * @return An enum stream codec.
     */
    public static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(Supplier<E[]> valuesSupplier, Function<E, Integer> ordinalSupplier) {
        return ByteBufCodecs.INT.map(e -> valuesSupplier.get()[e], ordinalSupplier);
    }

    /**
     * Encodes the given value to NBT using the given codec.
     *
     * @param codec The codec to use for encoding.
     * @param value The value to encode to NBT.
     * @param <T>   The type of the value and the codec.
     * @return The NBT representation of the given value.
     */
    public static <T> Tag encodeNbt(Codec<T> codec, T value) {
        return codec.encodeStart(NbtOps.INSTANCE, value).getOrThrow();
    }

    /**
     * Decodes the given value from NBT using the given codec.
     *
     * @param codec The codec to use for decoding.
     * @param tag   The NBT representation to decode.
     * @param <T>   The type of the value and the codec.
     * @return The decoded value.
     */
    public static <T> T decodeNbt(Codec<T> codec, Tag tag) {
        return codec.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
    }

    /**
     * Returns a {@link Comparator} that is reversed, i.e. will sort elements in the reverse order of the original {@link Comparator}.
     *
     * @param comparator The {@link Comparator} comparator to reverse.
     * @param <T>        The generic type of the {@link Comparator}.
     * @return The reversed {@link Comparator}.
     */
    public static <T> Comparator<T> reverseComparator(Comparator<T> comparator) {
        return (a, b) -> -comparator.compare(a, b);
    }

    /**
     * Returns a display name for the given position. If there is a nameable block entity at the position, the block entity's name is returned, otherwise the block's name is returned.
     *
     * @param level The {@link Level} to get the display name for.
     * @param pos   The {@link BlockPos} to get the display name for.
     * @return The display name to use for the given position.
     */
    public static Component getNameAtPos(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof Nameable nameable ? nameable.getDisplayName() : level.getBlockState(pos).getBlock().getName();
    }

    /**
     * @param vec A {@link Vec3i}.
     * @return The given {@link Vec3i}, represented as a {@link Vec3}.
     */
    public static Vec3 toVec3(Vec3i vec) {
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }
}
