package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PrintingTableRecipe implements Recipe<PrintingTableRecipeInput> {
    protected final ItemStack result;
    protected final int duration;
    private final ItemStack resultCopy;

    public PrintingTableRecipe(ItemStack result, int duration) {
        this.result = result;
        this.duration = duration;
        resultCopy = result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeType<?> getType() {
        return BCRecipes.PRINTING_TABLE.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return resultCopy;
    }

    public int getDuration() {
        return duration;
    }

    public boolean requiresExperience() {
        return false;
    }

    public ItemStack postProcess(ItemStack result, PrintingTableBlockEntity blockEntity) {
        return result;
    }

    public abstract PrintingTableMode getMode();

    public static abstract class Builder implements RecipeBuilder {
        protected final ItemStack result;
        protected final int duration;
        protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        @Nullable
        protected String group;

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
            this.group = group;
            return this;
        }

        @Override
        public Item getResult() {
            return result.getItem();
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation id) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .requirements(AdvancementRequirements.Strategy.OR);
            criteria.forEach(advancement::addCriterion);
            output.accept(id, build(), advancement.build(id.withPrefix("recipes/")));
        }

        public abstract PrintingTableRecipe build();
    }
}
