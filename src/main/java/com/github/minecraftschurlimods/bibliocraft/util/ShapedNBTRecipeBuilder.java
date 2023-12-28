package com.github.minecraftschurlimods.bibliocraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class ShapedNBTRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final ItemStack stack;
    private String group;
    private boolean showNotification = true;

    public ShapedNBTRecipeBuilder(RecipeCategory category, ItemStack stack) {
        this.category = category;
        this.stack = stack;
    }

    public static ShapedNBTRecipeBuilder shaped(RecipeCategory category, ItemStack stack) {
        return new ShapedNBTRecipeBuilder(category, stack);
    }

    public static ShapedNBTRecipeBuilder shaped(RecipeCategory category, ItemLike item) {
        return shaped(category, item, 1);
    }

    public static ShapedNBTRecipeBuilder shaped(RecipeCategory category, ItemLike item, int count) {
        return shaped(category, item, count, new CompoundTag());
    }

    public static ShapedNBTRecipeBuilder shaped(RecipeCategory category, ItemLike item, int count, CompoundTag tag) {
        return new ShapedNBTRecipeBuilder(category, new ItemStack(item, count, tag));
    }

    public ShapedNBTRecipeBuilder define(Character c, TagKey<Item> tag) {
        return define(c, Ingredient.of(tag));
    }

    public ShapedNBTRecipeBuilder define(Character c, ItemLike item) {
        return define(c, Ingredient.of(item));
    }

    public ShapedNBTRecipeBuilder define(Character c, Ingredient ingredient) {
        if (key.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            key.put(c, ingredient);
            return this;
        }
    }

    public ShapedNBTRecipeBuilder pattern(String line) {
        if (!rows.isEmpty() && line.length() != rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            rows.add(line);
            return this;
        }
    }

    public ShapedNBTRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public ShapedNBTRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public ShapedNBTRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getResult() {
        return stack.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        ShapedRecipePattern pattern = ensureValid(id);
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(advancement::addCriterion);
        ShapedRecipe recipe = new ShapedRecipe(
                Objects.requireNonNullElse(group, ""),
                RecipeBuilder.determineBookCategory(category),
                pattern,
                stack, // fixme not adding nbt to file
                showNotification
        );
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/")));
    }

    private ShapedRecipePattern ensureValid(ResourceLocation id) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        } else {
            return ShapedRecipePattern.of(key, rows);
        }
    }
}
