package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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
public class PrintingTableCloningWithEnchantmentsRecipe extends PrintingTableCloningRecipe {
    public static final MapCodec<PrintingTableCloningWithEnchantmentsRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(e -> e.ingredients),
            ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result),
            Codec.INT.fieldOf("duration").forGetter(e -> e.duration),
            NumberProviders.CODEC.optionalFieldOf("experience_cost").forGetter(e -> e.experienceCost)
    ).apply(inst, PrintingTableCloningWithEnchantmentsRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableCloningWithEnchantmentsRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), e -> e.ingredients,
            ItemStack.STREAM_CODEC, e -> e.result,
            ByteBufCodecs.INT, e -> e.duration,
            CodecUtil.toStreamCodec(NumberProviders.CODEC).apply(ByteBufCodecs::optional), e -> e.experienceCost,
            PrintingTableCloningWithEnchantmentsRecipe::new);
    private final Optional<NumberProvider> experienceCost;

    public PrintingTableCloningWithEnchantmentsRecipe(List<Ingredient> ingredients, ItemStack result, int duration, Optional<NumberProvider> experienceCost) {
        super(List.of(DataComponents.STORED_ENCHANTMENTS), ingredients, result, duration);
        this.experienceCost = experienceCost;
    }

    @Override
    public boolean matches(PrintingTableRecipeInput input, Level level) {
        if (!super.matches(input, level)) return false;
        ItemStack stack = input.right();
        if (!stack.has(DataComponents.STORED_ENCHANTMENTS)) return false;
        return BCUtil.nonNull(stack.get(DataComponents.STORED_ENCHANTMENTS))
                .keySet()
                .stream()
                .noneMatch(e -> e.is(BCTags.Enchantments.PRINTING_TABLE_CLONING_BLACKLIST));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.PRINTING_TABLE_CLONING_WITH_ENCHANTMENTS.get();
    }

    @Override
    public int getExperienceLevelCost(ItemStack stack, ServerLevel level) {
        return experienceCost.map(e -> e.getInt(new LootContext.Builder(new LootParams(level, Map.of(LootContextParams.TOOL, stack), Map.of(), 0)).create(Optional.empty()))).orElse(0);
    }

    @Override
    public boolean canHaveExperienceCost() {
        return experienceCost.isPresent();
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final List<Ingredient> ingredients = new ArrayList<>();
        private NumberProvider experienceCost = null;

        public Builder(ItemStack result, int duration) {
            super(result, duration);
        }

        public Builder addIngredient(Ingredient ingredient) {
            ingredients.add(ingredient);
            return this;
        }

        public Builder experienceCost(NumberProvider experienceCost) {
            this.experienceCost = experienceCost;
            return this;
        }

        public PrintingTableCloningWithEnchantmentsRecipe build() {
            return new PrintingTableCloningWithEnchantmentsRecipe(ingredients, result, duration, Optional.ofNullable(experienceCost));
        }
    }
}
