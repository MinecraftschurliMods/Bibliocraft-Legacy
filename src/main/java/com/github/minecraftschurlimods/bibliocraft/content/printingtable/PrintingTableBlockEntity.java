package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.IntStream;

public class PrintingTableBlockEntity extends BCMenuBlockEntity {
    private static final String MODE_KEY = "mode";
    private static final String DURATION_KEY = "duration";
    private static final String MAX_DURATION_KEY = "duration";
    private PrintingTableRecipe recipe;
    private PrintingTableRecipeInput recipeInput;
    private PrintingTableMode mode = PrintingTableMode.BIND;
    private int duration = 0;
    private int maxDuration = 0;

    public PrintingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.PRINTING_TABLE.get(), 11, defaultName("printing_table"), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PrintingTableBlockEntity blockEntity) {
        if (blockEntity.duration < blockEntity.maxDuration) {
            blockEntity.duration++;
        }
        if (blockEntity.duration >= blockEntity.maxDuration) {
            blockEntity.duration = 0;
            if (level.isClientSide()) return;
            PrintingTableRecipe recipe = blockEntity.recipe;
            if (recipe == null) return;
            List<ItemStack> remainingItems = recipe.getRemainingItems(blockEntity.getRecipeInput());
            ItemStack result = blockEntity.getItem(10);
            if (result.isEmpty()) {
                blockEntity.setItem(10, recipe.assemble(blockEntity.getRecipeInput(), level.registryAccess()));
            } else {
                result = result.copy();
                result.grow(1);
                blockEntity.setItem(10, result);
            }
            IntStream.range(0, 10)
                    .mapToObj(blockEntity::getItem)
                    .forEach(e -> e.shrink(1));
            for (int i = 0; i < remainingItems.size(); i++) {
                ItemStack remaining = remainingItems.get(i);
                if (remaining.isEmpty()) continue;
                blockEntity.setItem(i, remaining.copy());
            }
            blockEntity.calculateRecipe();
        }
        blockEntity.setChanged();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PrintingTableMenu(id, inventory, this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        setMode(CodecUtil.decodeNbt(PrintingTableMode.CODEC, tag.get(MODE_KEY)));
        duration = tag.getInt(DURATION_KEY);
        maxDuration = tag.getInt(MAX_DURATION_KEY);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(MODE_KEY, CodecUtil.encodeNbt(PrintingTableMode.CODEC, getMode()));
        tag.putInt(DURATION_KEY, duration);
        tag.putInt(MAX_DURATION_KEY, maxDuration);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        recipeInput = null;
        if (recipe == null || !recipe.matches(getRecipeInput(), BCUtil.nonNull(getLevel()))) {
            calculateRecipe();
            setChanged();
        }
    }

    public PrintingTableMode getMode() {
        return mode;
    }

    public void setMode(PrintingTableMode mode) {
        this.mode = mode;
        calculateRecipe();
        setChanged();
    }

    public float getProgress() {
        if (maxDuration == 0) return 0;
        System.out.println("Duration: " + duration + ", Max Duration: " + maxDuration);
        return duration / (float) maxDuration;
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
        if (recipe != null) {
            ItemStack output = getItem(10);
            if (!output.isEmpty() && (output.getCount() >= output.getMaxStackSize() || !ItemStack.isSameItemSameComponents(recipe.assemble(getRecipeInput(), getLevel().registryAccess()), output))) {
                recipe = null;
            }
        }
        duration = 0;
        maxDuration = recipe == null ? 0 : recipe.getDuration();
    }

    private PrintingTableRecipeInput getRecipeInput() {
        if (recipeInput == null) {
            recipeInput = new PrintingTableRecipeInput(IntStream.range(0, 9).mapToObj(this::getItem).toList(), getItem(9));
        }
        return recipeInput;
    }
}
