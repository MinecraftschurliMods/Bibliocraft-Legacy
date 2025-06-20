package com.github.minecraftschurlimods.bibliocraft.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class holding various codec-related helper methods.
 */
public final class CodecUtil {
    /**
     * A {@link StreamCodec} for {@link InteractionHand}s.
     */
    public static final StreamCodec<ByteBuf, InteractionHand> INTERACTION_HAND_STREAM_CODEC = ByteBufCodecs.BOOL.map(
            bool -> bool ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
            hand -> hand == InteractionHand.MAIN_HAND
    );

    /**
     * @param valuesSupplier The enum's {@code values()} method.
     * @param <E>            The enum type.
     * @return An enum {@link Codec}.
     */
    public static <E extends Enum<E> & StringRepresentable> Codec<E> enumCodec(Supplier<E[]> valuesSupplier) {
        return StringRepresentable.fromEnum(valuesSupplier);
    }

    /**
     * @param valuesSupplier  The enum's {@code values()} method.
     * @param ordinalSupplier The enum's {@code ordinal()} method.
     * @param <E>             The enum type.
     * @return An enum {@link StreamCodec}.
     */
    public static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(Supplier<E[]> valuesSupplier, Function<E, Integer> ordinalSupplier) {
        return ByteBufCodecs.VAR_INT.map(e -> valuesSupplier.get()[e], ordinalSupplier);
    }

    /**
     * @param keyCodec   The key {@link StreamCodec} to use.
     * @param valueCodec The value {@link StreamCodec} to use.
     * @param <B>        The buffer type.
     * @param <K>        The generic type of the {@link Map} key.
     * @param <V>        The generic type of the {@link Map} value.
     * @return A {@link StreamCodec} representing a {@link Map}.
     */
    public static <B extends ByteBuf, K, V> StreamCodec<B, Map<K, V>> mapStreamCodec(StreamCodec<? super B, K> keyCodec, StreamCodec<? super B, V> valueCodec) {
        return ByteBufCodecs.map(HashMap::new, keyCodec, valueCodec);
    }

    /**
     * Encodes the given value to NBT using the given {@link Codec}.
     *
     * @param codec The {@link Codec} to use for encoding.
     * @param value The value to encode to NBT.
     * @param <T>   The type of the value and the {@link Codec}.
     * @return The NBT representation of the given value.
     */
    public static <T> Tag encodeNbt(Codec<T> codec, T value) {
        return codec.encodeStart(NbtOps.INSTANCE, value).getOrThrow();
    }

    /**
     * Decodes the given value from NBT using the given {@link Codec}.
     *
     * @param codec The {@link Codec} to use for decoding.
     * @param tag   The NBT representation to decode.
     * @param <T>   The type of the value and the {@link Codec}.
     * @return The decoded value.
     */
    public static <T> T decodeNbt(Codec<T> codec, Tag tag) {
        return codec.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
    }

    /**
     * Encodes the given value to JSON using the given {@link Codec}.
     *
     * @param codec The {@link Codec} to use for encoding.
     * @param value The value to encode to NBT.
     * @param <T>   The type of the value and the {@link Codec}.
     * @return The JSON representation of the given value.
     */
    public static <T> JsonElement encodeJson(Codec<T> codec, T value) {
        return codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
    }

    /**
     * Decodes the given value from JSON using the given {@link Codec}.
     *
     * @param codec The {@link Codec} to use for decoding.
     * @param json  The JSON representation to decode.
     * @param <T>   The type of the value and the {@link Codec}.
     * @return The decoded value.
     */
    public static <T> T decodeJson(Codec<T> codec, JsonElement json) {
        return codec.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
    }
}
