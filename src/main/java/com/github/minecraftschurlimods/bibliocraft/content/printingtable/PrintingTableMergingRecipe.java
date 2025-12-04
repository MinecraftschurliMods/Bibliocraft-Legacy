package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrintingTableMergingRecipe extends PrintingTableRecipe {
    public static final MapCodec<PrintingTableMergingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Merger.MAP_CODEC.fieldOf("component_mergers").forGetter(e -> e.mergers),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(e -> e.ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result),
            Codec.INT.fieldOf("duration").forGetter(e -> e.duration)
    ).apply(inst, PrintingTableMergingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableMergingRecipe> STREAM_CODEC = StreamCodec.composite(
            Merger.MAP_STREAM_CODEC, e -> e.mergers,
            Ingredient.CONTENTS_STREAM_CODEC, e -> e.ingredient,
            ItemStack.STREAM_CODEC, e -> e.result,
            ByteBufCodecs.INT, e -> e.duration,
            PrintingTableMergingRecipe::new);

    private final Map<DataComponentType<?>, Merger<?>> mergers;
    private final Ingredient ingredient;

    public PrintingTableMergingRecipe(Map<DataComponentType<?>, Merger<?>> mergers, Ingredient ingredient, ItemStack result, int duration) {
        super(result, duration);
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
        return left.stream().allMatch(stack -> ItemStack.isSameItem(stack, result) && mergers.keySet().stream().allMatch(stack::has));
    }

    @Override
    public ItemStack assemble(PrintingTableRecipeInput input, HolderLookup.Provider registries) {
        ItemStack result = input.right().copy();
        DataComponentMap dataComponents = input.left()
                .stream()
                .filter(s -> !s.isEmpty())
                .map(ItemStack::getComponents)
                .reduce((left, right) -> mergeComponents(left, right, registries))
                .orElse(DataComponentMap.EMPTY);
        result.applyComponents(dataComponents);
        return result;
    }

    private DataComponentMap mergeComponents(DataComponentMap leftComponents, DataComponentMap rightComponents, HolderLookup.Provider registries) {
        if (leftComponents.isEmpty() && rightComponents.isEmpty()) return DataComponentMap.EMPTY;
        if (leftComponents.isEmpty()) return rightComponents;
        if (rightComponents.isEmpty()) return leftComponents;
        DataComponentMap.Builder result = DataComponentMap.builder();
        for (DataComponentType<?> type : Sets.union(leftComponents.keySet(), rightComponents.keySet())) {
            assert type != null;
            mergeComponent(leftComponents, rightComponents, type, result, registries);
        }
        return result.build();
    }

    private <T> void mergeComponent(DataComponentMap leftComponents, DataComponentMap rightComponents, DataComponentType<T> type, DataComponentMap.Builder resultBuilder, HolderLookup.Provider registries) {
        @Nullable T left = leftComponents.get(type);
        @Nullable T right = rightComponents.get(type);
        @Nullable T result;
        @SuppressWarnings("unchecked")
        Merger<T> merger = (Merger<T>) mergers.get(type);
        if (left == null) {
            result = right;
        } else if (right == null) {
            result = left;
        } else if (merger == null) {
            result = left;
        } else {
            result = merger.merge(left, right, registries).getOrThrow();
        }
        resultBuilder.set(type, result);
    }

    @Override
    public RecipeSerializer<? extends Recipe<PrintingTableRecipeInput>> getSerializer() {
        return BCRecipes.PRINTING_TABLE_MERGING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(PrintingTableRecipeInput input) {
        NonNullList<ItemStack> remainingItems = super.getRemainingItems(input);
        for (int i = 0; i < 9; i++) {
            ItemStack stack = input.left().get(i);
            if (stack.isEmpty()) continue;
            remainingItems.set(i, stack.copy());
        }
        return remainingItems;
    }

    @Override
    public Pair<List<Ingredient>, Ingredient> getDisplayIngredients() {
        Ingredient input = DataComponentIngredient.of(true, result.copy());
        return Pair.of(List.of(input, input), ingredient);
    }

    public enum MergeMethod implements StringRepresentableEnum {
        FIRST, LAST, MIN, MAX, APPEND;
        public static final Codec<MergeMethod> CODEC = CodecUtil.enumCodec(MergeMethod::values);
        public static final StreamCodec<ByteBuf, MergeMethod> STREAM_CODEC = CodecUtil.enumStreamCodec(MergeMethod::values, MergeMethod::ordinal);
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final Map<DataComponentType<?>, Map<String, MergeMethod>> mergers = new HashMap<>();
        private final Ingredient ingredient;

        public Builder(Ingredient ingredient, ItemStack result, int duration) {
            super(result, duration);
            this.ingredient = ingredient;
        }

        public Builder addMerger(DataComponentType<?> type, String key, MergeMethod method) {
            mergers.putIfAbsent(type, new HashMap<>());
            mergers.get(type).put(key, method);
            return this;
        }

        @Override
        public PrintingTableRecipe build() {
            Map<DataComponentType<?>, Merger<?>> mergers = new HashMap<>();
            this.mergers.forEach((type, m) -> mergers.put(type, new Merger<>(type, m)));
            return new PrintingTableMergingRecipe(mergers, ingredient, result, duration);
        }
    }

    public record Merger<T>(DataComponentType<T> type, Map<String, MergeMethod> mergers) {
        public static final Codec<Merger<?>> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                DataComponentType.CODEC.fieldOf("type").forGetter(Merger::type),
                Codec.unboundedMap(Codec.STRING, MergeMethod.CODEC).fieldOf("mergers").forGetter(Merger::mergers)
        ).apply(inst, Merger::new));
        public static final Codec<Map<DataComponentType<?>, Merger<?>>> MAP_CODEC = Codec.unboundedMap(DataComponentType.CODEC, Codec.unboundedMap(Codec.STRING, MergeMethod.CODEC)).xmap(
                m -> m.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new Merger<>(e.getKey(), e.getValue())
                )),
                m -> m.values().stream().collect(Collectors.toMap(
                        Merger::type,
                        Merger::mergers
                ))
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Merger<?>> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE), Merger::type,
                CodecUtil.mapStreamCodec(ByteBufCodecs.STRING_UTF8, MergeMethod.STREAM_CODEC), Merger::mergers,
                Merger::new
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Map<DataComponentType<?>, Merger<?>>> MAP_STREAM_CODEC = STREAM_CODEC
                .apply(ByteBufCodecs.list())
                .map(
                        l -> l.stream().collect(Collectors.toMap(Merger::type, Function.identity())),
                        m -> List.copyOf(m.values())
                );

        public DataResult<T> merge(T left, T right, HolderLookup.Provider registries) {
            Codec<T> codec = type.codec();
            if (codec == null) {
                return DataResult.error(() -> "Cannot merge " + type + " because it has no codec");
            }
            RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
            DataResult<JsonElement> leftJsonResult = codec.encodeStart(ops, left);
            DataResult<JsonElement> rightJsonResult = codec.encodeStart(ops, right);
            return leftJsonResult.apply2stable(this::mergeJson, rightJsonResult)
                    .flatMap(jsonElement -> codec.decode(ops, jsonElement))
                    .map(Pair::getFirst);
        }

        private JsonElement mergeJson(JsonElement left, JsonElement right) {
            return mergeJson(left, right, "");
        }

        @Contract("!null, !null, _ -> !null")
        private @Nullable JsonElement mergeJson(@Nullable JsonElement left, @Nullable JsonElement right, String path) {
            if (left == null) return right;
            if (right == null) return left;

            MergeMethod mergeMethod = mergers.get(path);
            if (mergeMethod == null) {
                if (left.isJsonObject() && right.isJsonObject()) {
                    JsonObject leftJson = left.getAsJsonObject();
                    JsonObject rightJson = right.getAsJsonObject();
                    JsonObject mergedJson = new JsonObject();
                    Set<String> keys = new HashSet<>(leftJson.keySet());
                    keys.addAll(rightJson.keySet());
                    for (String key : keys) {
                        JsonElement leftValue = leftJson.get(key);
                        JsonElement rightValue = rightJson.get(key);
                        if (leftValue == null && rightValue == null) {
                            continue;
                        }
                        mergedJson.add(key, mergeJson(leftValue, rightValue, (path.isEmpty() ? "" : path + ".") + key));
                    }
                    return mergedJson;
                }
                if (left.isJsonArray() || right.isJsonArray()) {
                    mergeMethod = MergeMethod.APPEND;
                } else {
                    mergeMethod = MergeMethod.FIRST;
                }
            }
            return switch (mergeMethod) {
                case FIRST -> left;
                case LAST -> right;
                case APPEND -> {
                    var arrayValue = new JsonArray();
                    if (left.isJsonArray()) {
                        arrayValue.addAll(left.getAsJsonArray());
                    } else {
                        arrayValue.add(left);
                    }
                    if (right.isJsonArray()) {
                        arrayValue.addAll(right.getAsJsonArray());
                    } else {
                        arrayValue.add(right);
                    }
                    yield arrayValue;
                }
                case MIN -> {
                    if (left.isJsonPrimitive() && right.isJsonPrimitive() && left.getAsJsonPrimitive().isNumber() && right.getAsJsonPrimitive().isNumber()) {
                        BigDecimal leftValue = left.getAsBigDecimal();
                        BigDecimal rightValue = right.getAsBigDecimal();
                        yield new JsonPrimitive(leftValue.min(rightValue));
                    }
                    yield left;
                }
                case MAX -> {
                    if (left.isJsonPrimitive() && right.isJsonPrimitive() && left.getAsJsonPrimitive().isNumber() && right.getAsJsonPrimitive().isNumber()) {
                        BigDecimal leftValue = left.getAsBigDecimal();
                        BigDecimal rightValue = right.getAsBigDecimal();
                        yield new JsonPrimitive(leftValue.max(rightValue));
                    }
                    yield left;
                }
            };
        }
    }
}
