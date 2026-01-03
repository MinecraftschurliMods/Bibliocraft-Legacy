package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import org.jspecify.annotations.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class PrintingTableRecipe implements Recipe<PrintingTableRecipeInput> {
    protected final ItemStack result;
    protected final int duration;

    public PrintingTableRecipe(ItemStack result, int duration) {
        this.result = result;
        this.duration = duration;
    }

    @Override
    public RecipeType<PrintingTableRecipe> getType() {
        return BCRecipes.PRINTING_TABLE.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return BCRecipes.PRINTING_TABLE_RECIPE_CATEGORY.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public ItemStack getResultItem() {
        return result.copy();
    }

    public NonNullList<ItemStack> getRemainingItems(PrintingTableRecipeInput input) {
        return defaultCraftingReminder(input);
    }

    static NonNullList<ItemStack> defaultCraftingReminder(PrintingTableRecipeInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = input.getItem(i);
            nonnulllist.set(i, item.getCraftingRemainder());
        }

        return nonnulllist;
    }

    public int getDuration() {
        return duration;
    }

    public int getExperienceLevelCost(ItemStack result, ServerLevel level) {
        return 0;
    }

    public boolean canHaveExperienceCost() {
        return false;
    }

    public ItemStack postProcess(ItemStack result, PrintingTableBlockEntity blockEntity) {
        return result;
    }

    public Pair<List<Ingredient>, Ingredient> getDisplayIngredients() {
        return Pair.of(List.of(), Ingredient.of());
    }

    public abstract PrintingTableMode getMode();

    public static abstract class Builder implements RecipeBuilder {
        protected final ItemStack result;
        protected final int duration;
        protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        public Builder(ItemStack result, int duration) {
            this.result = result;
            this.duration = duration;
        }

        @Override
        public Builder unlockedBy(String name, Criterion<?> criterion) {
            criteria.put(name, criterion);
            return this;
        }

        @Override
        public Builder group(@Nullable String group) {
            return this;
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                    .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                    .requirements(AdvancementRequirements.Strategy.OR);
            criteria.forEach(advancement::addCriterion);
            output.accept(resourceKey, build(), advancement.build(resourceKey.identifier().withPrefix("recipes/")));
        }

        public abstract PrintingTableRecipe build();
    }
}
