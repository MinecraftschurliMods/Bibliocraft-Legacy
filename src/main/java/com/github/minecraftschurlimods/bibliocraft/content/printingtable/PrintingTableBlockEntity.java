package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class PrintingTableBlockEntity extends BCMenuBlockEntity {
    private PrintingTableRecipe recipe;
    private PrintingTableRecipeInput recipeInput;
    private PrintingTableMode mode;

    public PrintingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.PRINTING_TABLE.get(), 11, defaultName("printing_table"), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PrintingTableBlockEntity blockEntity) {

    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PrintingTableMenu(id, inventory, this);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        recipeInput = null;
        if (!recipe.matches(getRecipeInput(), BCUtil.nonNull(getLevel()))) {
            calculateRecipe();
        }
    }

    private void calculateRecipe() {
        if (!(getLevel() instanceof ServerLevel serverLevel)) return;
        recipe = serverLevel
                .getRecipeManager()
                .getRecipesFor(BCRecipes.PRINTING_TABLE.get(), getRecipeInput(), serverLevel)
                .stream()
                .map(RecipeHolder::value)
                .filter(e -> e.getMode() == mode)
                .findFirst()
                .orElse(null);
    }

    private PrintingTableRecipeInput getRecipeInput() {
        if (recipeInput == null) {
            recipeInput = new PrintingTableRecipeInput(IntStream.range(0, 9).mapToObj(this::getItem).toList(), getItem(9));
        }
        return recipeInput;
    }
}
