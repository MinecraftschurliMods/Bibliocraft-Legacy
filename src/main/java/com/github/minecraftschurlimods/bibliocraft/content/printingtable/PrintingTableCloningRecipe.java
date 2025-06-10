package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PrintingTableCloningRecipe extends PrintingTableRecipe {
    public static final MapCodec<PrintingTableCloningRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            DataComponentType.CODEC.listOf(1, 256).fieldOf("data_components").forGetter(e -> e.dataComponentTypes),
            CodecUtil.nonNullListMapCodec(Ingredient.CODEC_NONEMPTY, "ingredients", list -> {
                if (list.isEmpty()) return DataResult.error(() -> "No ingredients for printing table clone recipe");
                int size = ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth();
                return list.size() > size
                        ? DataResult.error(() -> "Too many inputs for printing table clone recipe. The maximum is: %s".formatted(size))
                        : DataResult.success(NonNullList.of(Ingredient.EMPTY, list.toArray(Ingredient[]::new)));
            }).forGetter(e -> e.left),
            ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result),
            Codec.INT.fieldOf("duration").forGetter(e -> e.duration)
    ).apply(inst, PrintingTableCloningRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableCloningRecipe> STREAM_CODEC = StreamCodec.composite(
            DataComponentType.STREAM_CODEC.apply(ByteBufCodecs.list()), e -> e.dataComponentTypes,
            CodecUtil.nonNullListStreamCodec(Ingredient.CONTENTS_STREAM_CODEC), e -> e.left,
            ItemStack.STREAM_CODEC, e -> e.result,
            ByteBufCodecs.INT, e -> e.duration,
            PrintingTableCloningRecipe::new);
    private final List<DataComponentType<?>> dataComponentTypes;
    private final NonNullList<Ingredient> left;

    public PrintingTableCloningRecipe(List<DataComponentType<?>> dataComponentTypes, NonNullList<Ingredient> left, ItemStack result, int duration) {
        super(result, duration);
        this.dataComponentTypes = dataComponentTypes;
        this.left = left;
    }

    @Override
    public boolean matches(PrintingTableRecipeInput input, Level level) {
        if (input.left().isEmpty()) return false;
        if (input.right().isEmpty()) return false;
        if (!input.right().is(result.getItem())) return false;
        if (!dataComponentTypes.stream().allMatch(e -> input.right().has(e))) return false;
        if (input.left().stream().filter(e -> e != ItemStack.EMPTY).count() != left.size()) return false;
        List<Ingredient> copy = new ArrayList<>(left);
        outer: for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            for (Ingredient ingredient : copy) {
                if (ingredient.test(stack)) {
                    copy.remove(ingredient);
                    continue outer;
                }
            }
            return false;
        }
        return copy.isEmpty();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ItemStack assemble(PrintingTableRecipeInput input, HolderLookup.Provider registries) {
        ItemStack stack = result.copy();
        for (DataComponentType type : dataComponentTypes) {
            stack.set(type, input.right().get(type));
        }
        return stack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.PRINTING_TABLE_CLONING.get();
    }

    @Override
    public PrintingTableMode getMode() {
        return PrintingTableMode.CLONE;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(PrintingTableRecipeInput input) {
        NonNullList<ItemStack> remainingItems = super.getRemainingItems(input);
        remainingItems.set(9, input.right());
        return remainingItems;
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final List<DataComponentType<?>> dataComponentTypes = new ArrayList<>();
        private final List<Ingredient> left = new ArrayList<>();

        public Builder(ItemStack result, int duration) {
            super(result, duration);
        }

        public Builder addDataComponentType(DataComponentType<?> type) {
            dataComponentTypes.add(type);
            return this;
        }

        public Builder addIngredient(Ingredient ingredient) {
            left.add(ingredient);
            return this;
        }

        @Override
        public PrintingTableRecipe build() {
            return new PrintingTableCloningRecipe(dataComponentTypes, NonNullList.copyOf(left), result, duration);
        }
    }
}
