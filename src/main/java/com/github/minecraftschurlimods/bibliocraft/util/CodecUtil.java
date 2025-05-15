package com.github.minecraftschurlimods.bibliocraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;

import java.util.List;
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
        return ByteBufCodecs.VAR_INT.map(e -> valuesSupplier.get()[e], ordinalSupplier);
    }

    /**
     * @param baseCodec The {@link Codec} to base the list codec on.
     * @param fieldName The name of the codec field.
     * @param converter A {@link Function} to convert the {@link List} to a {@link DataResult}.
     * @return A {@link MapCodec} that is serialized to a {@link List} but is deserialized to a {@link NonNullList}.
     * @param <T> The generic type of the lists.
     */
    public static <T> MapCodec<NonNullList<T>> nonNullListMapCodec(Codec<T> baseCodec, String fieldName, Function<List<T>, DataResult<NonNullList<T>>> converter) {
        return baseCodec.listOf().fieldOf(fieldName).flatXmap(converter, DataResult::success);
    }

    /**
     * @param baseStreamCodec The {@link StreamCodec} to base the list stream codec on.
     * @return A {@link StreamCodec} for synchronizing a {@link NonNullList}.
     * @param <B> The buffer type.
     * @param <T> The generic type of the {@link NonNullList}.
     */
    public static <B extends FriendlyByteBuf, T> StreamCodec<B, NonNullList<T>> nonNullListStreamCodec(StreamCodec<B, T> baseStreamCodec) {
        return new StreamCodec<>() {
            @Override
            public NonNullList<T> decode(B buffer) {
                NonNullList<T> result = NonNullList.createWithCapacity(buffer.readVarInt());
                result.replaceAll($ -> baseStreamCodec.decode(buffer));
                return result;
            }

            @Override
            public void encode(B buffer, NonNullList<T> value) {
                buffer.writeVarInt(value.size());
                value.forEach(t -> baseStreamCodec.encode(buffer, t));
            }
        };
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
}
