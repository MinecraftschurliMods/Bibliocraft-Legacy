package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PrintingTableCloningRecipe extends PrintingTableRecipe {
    public static final MapCodec<PrintingTableCloningRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            DataComponentType.CODEC.listOf(1, 256).fieldOf("data_components").forGetter(e -> e.dataComponentTypes),
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(e -> e.left),
            ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result),
            Codec.INT.fieldOf("duration").forGetter(e -> e.duration),
            NumberProviders.CODEC.optionalFieldOf("experience_cost").forGetter(e -> e.experienceCost)
    ).apply(inst, PrintingTableCloningRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableCloningRecipe> STREAM_CODEC = StreamCodec.composite(
            DataComponentType.STREAM_CODEC.apply(ByteBufCodecs.list()), e -> e.dataComponentTypes,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), e -> e.left,
            ItemStack.STREAM_CODEC, e -> e.result,
            ByteBufCodecs.INT, e -> e.duration,
            CodecUtil.toStreamCodec(NumberProviders.CODEC).apply(ByteBufCodecs::optional), e -> e.experienceCost,
            PrintingTableCloningRecipe::new);
    private final List<DataComponentType<?>> dataComponentTypes;
    private final List<Ingredient> left;
    private final Optional<NumberProvider> experienceCost;

    public PrintingTableCloningRecipe(List<DataComponentType<?>> dataComponentTypes, List<Ingredient> left, ItemStack result, int duration, Optional<NumberProvider> experienceCost) {
        super(result, duration);
        this.dataComponentTypes = dataComponentTypes;
        this.left = left;
        this.experienceCost = experienceCost;
    }

    @Override
    public boolean matches(PrintingTableRecipeInput input, Level level) {
        if (input.left().isEmpty()) return false;
        if (input.right().isEmpty()) return false;
        if (!input.right().is(result.getItem())) return false;
        if (!dataComponentTypes.stream().allMatch(e -> input.right().has(e))) return false;
        if (input.left().stream().filter(e -> e != ItemStack.EMPTY).count() != left.size()) return false;
        List<Ingredient> copy = new ArrayList<>(left);
        outer:
        for (int i = 0; i < input.left().size(); i++) {
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
        remainingItems.set(9, input.right().copy());
        return remainingItems;
    }

    @Override
    public int getExperienceCost(ItemStack stack, ServerLevel level) {
        return experienceCost.map(e -> e.getInt(new LootContext.Builder(new LootParams(level, Map.of(LootContextParams.TOOL, stack), Map.of(), 0)).create(Optional.empty()))).orElse(0);
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final List<DataComponentType<?>> dataComponentTypes = new ArrayList<>();
        private final List<Ingredient> left = new ArrayList<>();
        private NumberProvider experienceCost = null;

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

        public Builder experienceCost(NumberProvider experienceCost) {
            this.experienceCost = experienceCost;
            return this;
        }

        @Override
        public PrintingTableRecipe build() {
            return new PrintingTableCloningRecipe(dataComponentTypes, List.copyOf(left), result, duration, Optional.ofNullable(experienceCost));
        }
    }
}
