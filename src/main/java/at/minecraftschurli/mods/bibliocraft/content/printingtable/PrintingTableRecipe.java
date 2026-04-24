package at.minecraftschurli.mods.bibliocraft.content.printingtable;

import at.minecraftschurli.mods.bibliocraft.init.BCRecipes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class PrintingTableRecipe implements Recipe<PrintingTableRecipeInput> {
    protected final ItemStackTemplate result;
    protected final int duration;
    protected final String group;
    protected final boolean showNotification;

    public PrintingTableRecipe(ItemStackTemplate result, int duration, String group, boolean showNotification) {
        this.result = result;
        this.duration = duration;
        this.group = group;
        this.showNotification = showNotification;
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

    @Override
    public String group() {
        return group;
    }

    @Override
    public boolean showNotification() {
        return showNotification;
    }

    public ItemStack getResultItem() {
        return result.create();
    }

    public NonNullList<ItemStack> getRemainingItems(PrintingTableRecipeInput input) {
        return defaultCraftingReminder(input);
    }

    static NonNullList<ItemStack> defaultCraftingReminder(PrintingTableRecipeInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = input.getItem(i);
            nonnulllist.set(i, item.getCraftingRemainder() != null ? item.getCraftingRemainder().create() : ItemStack.EMPTY);
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
        protected final ItemStackTemplate result;
        protected final int duration;
        protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
        protected String group = "";
        protected boolean showNotification = true;

        public Builder(ItemStackTemplate result, int duration) {
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
            this.group = group == null ? "" : group;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder showNotification(boolean showNotification) {
            this.showNotification = showNotification;
            return this;
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

        @Override
        public ResourceKey<Recipe<?>> defaultId() {
            return RecipeBuilder.getDefaultRecipeId(Objects.requireNonNull(result, "A result stack is required to use a default id"));
        }

        public abstract PrintingTableRecipe build();
    }
}
