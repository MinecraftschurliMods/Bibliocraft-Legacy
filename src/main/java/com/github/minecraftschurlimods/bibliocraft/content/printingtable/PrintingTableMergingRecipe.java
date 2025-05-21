package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrintingTableMergingRecipe extends PrintingTableRecipe {
    public static final MapCodec<PrintingTableMergingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.unboundedMap(BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec(), Codec.unboundedMap(Codec.STRING, MergeMethod.CODEC)).fieldOf("component_mergers").forGetter(e -> e.mergers),
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(e -> e.ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result)
    ).apply(inst, PrintingTableMergingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableMergingRecipe> STREAM_CODEC = StreamCodec.composite(
            CodecUtil.mapStreamCodec(ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE), CodecUtil.mapStreamCodec(ByteBufCodecs.STRING_UTF8, MergeMethod.STREAM_CODEC)), e -> e.mergers,
            Ingredient.CONTENTS_STREAM_CODEC, e -> e.ingredient,
            ItemStack.STREAM_CODEC, e -> e.result,
            PrintingTableMergingRecipe::new);
    private final Map<DataComponentType<?>, Map<String, MergeMethod>> mergers;
    private final Ingredient ingredient;

    public PrintingTableMergingRecipe(Map<DataComponentType<?>, Map<String, MergeMethod>> mergers, Ingredient ingredient, ItemStack result) {
        super(result);
        this.mergers = mergers;
        this.ingredient = ingredient;
    }

    @Override
    public PrintingTableMode getMode() {
        return PrintingTableMode.MERGE;
    }

    @Override
    public boolean matches(PrintingTableRecipeInput input, Level level) {
        ItemStack right = input.right();
        if (!ingredient.test(right)) return false;
        List<ItemStack> left = new ArrayList<>(input.left());
        left.removeIf(ItemStack::isEmpty);
        if (left.size() < 2) return false;
        Item item = left.getFirst().getItem();
        return left.stream().allMatch(stack -> stack.is(item) && mergers.keySet().stream().allMatch(stack::has));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemStack assemble(PrintingTableRecipeInput input, HolderLookup.Provider registries) {
        ItemStack result = input.right().copy();
        List<Map<DataComponentType<?>, JsonObject>> jsons = input.left()
                .stream()
                .filter(e -> !e.isEmpty())
                .<Map<DataComponentType<?>, JsonObject>>map(stack -> Map.ofEntries(stack.getComponents()
                        .keySet()
                        .stream()
                        .filter(type -> type.codec() != null)
                        .map(type -> mapToJson(type, stack))
                        .filter(Objects::nonNull)
                        .toArray(Map.Entry[]::new)))
                .toList();
        for (DataComponentType<?> type : mergers.keySet()) {
            JsonObject json = new JsonObject();
            for (String key : mergers.get(type).keySet()) {
                MergeMethod merger = getMerger(type, key, jsons);
                json.add(key, switch (merger) {
                    case FIRST, RANDOM -> jsons.getFirst().get(type).get(key); //TODO random
                    case LAST -> jsons.getLast().get(type).get(key);
                    case MIN -> new JsonPrimitive(jsons.stream()
                            .mapToInt(e -> e.get(type).get(key).getAsInt())
                            .min()
                            .orElseThrow());
                    case MAX -> new JsonPrimitive(jsons.stream()
                            .mapToInt(e -> e.get(type).get(key).getAsInt())
                            .max()
                            .orElseThrow());
                    case APPEND -> {
                        JsonArray array = new JsonArray();
                        jsons.stream()
                                .map(e -> e.get(type).get(key).getAsJsonArray().asList())
                                .flatMap(List::stream)
                                .forEach(array::add);
                        yield array;
                    }
                });
            }
            setFromJson(type, result, json);
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.PRINTING_TABLE_MERGING.get();
    }

    private MergeMethod getMerger(DataComponentType<?> type, String key, List<Map<DataComponentType<?>, JsonObject>> jsons) {
        MergeMethod merger = mergers.get(type).get(key);
        if (merger == null)
            return jsons.stream().allMatch(e -> e.get(type).get(key).isJsonArray()) ? MergeMethod.APPEND : MergeMethod.FIRST;
        if (merger == MergeMethod.MIN || merger == MergeMethod.MAX)
            return jsons.stream().allMatch(e -> isJsonNumber(e.get(type).get(key))) ? merger : MergeMethod.FIRST;
        return merger;
    }

    private static boolean isJsonNumber(JsonElement json) {
        return json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber();
    }

    @Nullable
    private static <T> Map.Entry<DataComponentType<T>, JsonObject> mapToJson(DataComponentType<T> type, ItemStack stack) {
        JsonElement json = CodecUtil.encodeJson(BCUtil.nonNull(type.codec()), stack.get(type));
        return json.isJsonObject() ? Map.entry(type, json.getAsJsonObject()) : null;
    }

    private static <T> void setFromJson(DataComponentType<T> type, ItemStack stack, JsonObject json) {
        stack.set(type, CodecUtil.decodeJson(BCUtil.nonNull(type.codec()), json));
    }

    public enum MergeMethod implements StringRepresentableEnum {
        FIRST, LAST, RANDOM, MIN, MAX, APPEND;
        public static final Codec<MergeMethod> CODEC = CodecUtil.enumCodec(MergeMethod::values);
        public static final StreamCodec<ByteBuf, MergeMethod> STREAM_CODEC = CodecUtil.enumStreamCodec(MergeMethod::values, MergeMethod::ordinal);
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final Map<DataComponentType<?>, Map<String, MergeMethod>> mergers = new HashMap<>();
        private final Ingredient ingredient;

        public Builder(Ingredient ingredient, ItemStack result) {
            super(result);
            this.ingredient = ingredient;
        }

        public Builder addMerger(DataComponentType<?> type, String key, MergeMethod method) {
            mergers.putIfAbsent(type, new HashMap<>());
            mergers.get(type).put(key, method);
            return this;
        }

        @Override
        public PrintingTableRecipe build() {
            return new PrintingTableMergingRecipe(mergers, ingredient, result);
        }
    }
}
