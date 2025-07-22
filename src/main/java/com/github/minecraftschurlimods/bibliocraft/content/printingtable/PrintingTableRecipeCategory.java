package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.client.widget.JeiSpriteWidget;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class PrintingTableRecipeCategory extends AbstractRecipeCategory<RecipeHolder<PrintingTableRecipe>> {
    public static final RecipeType<RecipeHolder<PrintingTableRecipe>> TYPE = RecipeType.createRecipeHolderType(BCRecipes.PRINTING_TABLE.getId());
    private static final ResourceLocation LEVEL = BCUtil.bcLoc("level");
    private static final int LEVEL_X = 98;
    private static final int LEVEL_Y = 24;
    private static final int LEVEL_SIZE = 9;

    public PrintingTableRecipeCategory(IGuiHelper guiHelper) {
        super(TYPE, Translations.PRINTING_TABLE_CATEGORY, guiHelper.createDrawableItemLike(BCItems.PRINTING_TABLE), 147, 54);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PrintingTableRecipe> holder, IFocusGroup focuses) {
        PrintingTableRecipe recipe = holder.value();
        Pair<List<Ingredient>, Ingredient> ingredients = recipe.getDisplayIngredients();
        List<Ingredient> left = ingredients.getFirst();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                IRecipeSlotBuilder slot = builder.addInputSlot(j * 18 + 1, i * 18 + 1).setStandardSlotBackground();
                if (i * 3 + j < left.size()) {
                    slot.addIngredients(left.get(i * 3 + j));
                }
            }
        }
        builder.addInputSlot(74, 19).setStandardSlotBackground().addIngredients(ingredients.getSecond());
        builder.addOutputSlot(126, 19).setOutputSlotBackground().addItemStack(recipe.getResultItem());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<PrintingTableRecipe> holder, IFocusGroup focuses) {
        super.createRecipeExtras(builder, holder, focuses);
        PrintingTableRecipe recipe = holder.value();
        builder.addRecipePlusSign().setPosition(57, 21);
        builder.addAnimatedRecipeArrow(recipe.getDuration()).setPosition(95, 20);
        builder.addText(Component.translatable(Translations.JEI_SECONDS_KEY, recipe.getDuration() / 20), getWidth() - 57, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setColor(0xFF808080);
        builder.addText(Component.translatable(Translations.PRINTING_TABLE_MODE_KEY, Component.translatable(recipe.getMode().getTranslationKey())), getWidth() - 57, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setColor(0xFF808080);
        if (recipe.canHaveExperienceCost()) {
            builder.addWidget(new JeiSpriteWidget(LEVEL, LEVEL_X, LEVEL_Y, 10, LEVEL_SIZE, LEVEL_SIZE));
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<PrintingTableRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        if (mouseX >= LEVEL_X && mouseX < LEVEL_X + LEVEL_SIZE && mouseY >= LEVEL_Y && mouseY < LEVEL_Y + LEVEL_SIZE) {
            tooltip.add(Translations.REQUIRES_EXPERIENCE);
        }
    }
}
