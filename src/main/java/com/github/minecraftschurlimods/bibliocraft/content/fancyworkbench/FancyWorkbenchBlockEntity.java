package com.github.minecraftschurlimods.bibliocraft.content.fancyworkbench;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class FancyWorkbenchBlockEntity extends BCMenuBlockEntity {
    private static final String CRAFTING_TICKS_REMAINING_KEY = "crafting_ticks_remaining";
    private static final String DISABLED_SLOTS_KEY = "disabled_slots";
    private static final String TRIGGERED_KEY = "triggered";
    private static final int SLOT_DISABLED = 1;
    private static final int SLOT_ENABLED = 0;
    private static final int MAX_CRAFTING_TICKS = 6;
    private final boolean[] disabledSlots = new boolean[9];
    private int craftingTicksRemaining = MAX_CRAFTING_TICKS;
    private RecipeHolder<CraftingRecipe> recipe;

    public FancyWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_WORKBENCH.get(), 18, defaultName("fancy_workbench"), pos, state);
    }

    public int getRedstoneSignal() {
        return (int) IntStream.range(0, 9).filter(i -> !getItem(i).isEmpty() || isSlotDisabled(i)).count();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FancyWorkbenchBlockEntity blockEntity) {
        if (blockEntity.recipe == null) return;
        CraftingRecipe recipe = blockEntity.recipe.value();
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack resultStack = blockEntity.getItem(9);
        if (!resultStack.isEmpty() && (!ItemStack.isSameItemSameComponents(result, resultStack) || result.getCount() + resultStack.getCount() > result.getMaxStackSize()))
            return;
        blockEntity.craftingTicksRemaining--;
        if (blockEntity.craftingTicksRemaining <= 0) {
            CraftingInput input = CraftingInput.of(3, 3, blockEntity.getInputs());
            ItemStack assembled = recipe.assemble(input, level.registryAccess());
            assembled.onCraftedBySystem(level);
            blockEntity.setItem(9, blockEntity.tryDispense(level, pos, assembled, state));
            blockEntity.craftingTicksRemaining = MAX_CRAFTING_TICKS;
            recipe.getRemainingItems(CraftingInput.of(3, 3, blockEntity.getInputs()))
                    .stream()
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> blockEntity.tryDispense(level, pos, e, state));
            List<ItemStack> inputs = new ArrayList<>(blockEntity.getInputs()
                    .stream()
                    .filter(e -> !e.isEmpty())
                    .toList());
            // for loop instead of stream chain to prevent CME
            for (int i = 10; i < 18; i++) {
                ItemStack stack = blockEntity.getItem(i);
                if (stack.isEmpty()) continue;
                List<ItemStack> toRemove = new ArrayList<>();
                for (ItemStack e : inputs) {
                    if (!ItemStack.isSameItemSameComponents(e, stack)) continue;
                    if (e.getCount() >= e.getMaxStackSize()) continue;
                    if (!stack.isEmpty()) {
                        e.grow(1);
                        toRemove.add(e);
                        stack.shrink(1);
                    }
                }
                toRemove.forEach(inputs::remove);
            }
            blockEntity.getInputs()
                    .stream()
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> e.shrink(1));
            blockEntity.setChanged();
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (isSlotDisabled(slot)) {
            disabledSlots[slot] = false;
        }
        super.setItem(slot, stack);
        calculateRecipe();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.hasCraftingRemainingItem() || isSlotDisabled(slot)) return false;
        ItemStack slotStack = getItem(slot);
        return slotStack.isEmpty() || slotStack.getCount() < slotStack.getMaxStackSize() && !smallerStackExists(slotStack.getCount(), stack, slot);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FancyWorkbenchMenu(id, inventory, this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        craftingTicksRemaining = tag.getInt(CRAFTING_TICKS_REMAINING_KEY);
        int[] tagSlots = tag.getIntArray(DISABLED_SLOTS_KEY);
        for (int i = 0; i < 9; i++) {
            disabledSlots[i] = canSlotBeDisabled(i) && tagSlots[i] == SLOT_DISABLED;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(CRAFTING_TICKS_REMAINING_KEY, craftingTicksRemaining);
        int[] tagSlots = new int[9];
        for (int i = 0; i < 9; i++) {
            tagSlots[i] = disabledSlots[i] ? SLOT_DISABLED : SLOT_ENABLED;
        }
        tag.putIntArray(DISABLED_SLOTS_KEY, tagSlots);
    }

    public void setSlotDisabled(int slot, boolean disabled) {
        if (!canSlotBeDisabled(slot)) return;
        disabledSlots[slot] = disabled;
    }

    public boolean isSlotDisabled(int slot) {
        return isCraftingSlot(slot) && disabledSlots[slot];
    }

    public boolean canSlotBeDisabled(int slot) {
        return isCraftingSlot(slot) && items.getStackInSlot(slot).isEmpty();
    }

    private boolean isCraftingSlot(int slot) {
        return slot >= 0 && slot < 9;
    }

    private boolean smallerStackExists(int currentSize, ItemStack stack, int slot) {
        for (int i = slot + 1; i < 9; i++) {
            if (!isSlotDisabled(i)) {
                ItemStack slotStack = getItem(i);
                if (slotStack.isEmpty() || slotStack.getCount() < currentSize && ItemStack.isSameItemSameComponents(slotStack, stack))
                    return true;
            }
        }
        return false;
    }

    private void calculateRecipe() {
        RecipeManager recipes = Objects.requireNonNull(level).getRecipeManager();
        CraftingInput input = CraftingInput.of(3, 3, getInputs());
        recipe = recipes.getRecipeFor(RecipeType.CRAFTING, input, level).orElse(null);
        if (recipe != null) {
            items.setStackInSlot(9, recipe.value().getResultItem(level.registryAccess()).copy());
        }
    }

    private ItemStack tryDispense(Level level, BlockPos pos, ItemStack stack, BlockState state) {
        Direction direction = state.getValue(FancyWorkbenchBlock.FACING);
        stack = BCUtil.tryInsert(level, pos, direction, stack, this);
        if (!stack.isEmpty() && !level.isClientSide() && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) {
            Vec3 vec3 = Vec3.atCenterOf(pos.above());
            ItemEntity entity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), stack);
            level.addFreshEntity(entity);
            level.playSound(null, pos, SoundEvents.CRAFTER_CRAFT, SoundSource.BLOCKS, 1, 1);
            return ItemStack.EMPTY;
        }
        return stack;
    }

    private List<ItemStack> getInputs() {
        return IntStream.range(0, 9).mapToObj(this::getItem).toList();
    }
}
