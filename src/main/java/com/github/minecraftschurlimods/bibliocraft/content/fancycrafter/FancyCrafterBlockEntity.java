package com.github.minecraftschurlimods.bibliocraft.content.fancycrafter;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.slot.HasToggleableSlots;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FancyCrafterBlockEntity extends BCMenuBlockEntity implements HasToggleableSlots, CraftingContainer {
    private static final String CRAFTING_TICKS_REMAINING_KEY = "crafting_ticks_remaining";
    private static final String DISABLED_SLOTS_KEY = "disabled_slots";
    static final int WIDTH = 3;
    static final int HEIGHT = 3;
    static final int SLOT_DISABLED = 1;
    static final int SLOT_ENABLED = 0;
    static final int CRAFTING_SLOTS = WIDTH * HEIGHT;
    static final int CRAFTING_RESULT_SLOTS = 1;
    static final int STORAGE_SLOTS = 8;
    static final int CRAFTING_RESULT_SLOT_INDEX = CRAFTING_SLOTS;
    private static final int MAX_CRAFTING_TICKS = 6;
    final ContainerData containerData = new ContainerData() {
        private final int[] slotStates = new int[CRAFTING_SLOTS];

        @Override
        public int get(int index) {
            return slotStates[index];
        }

        @Override
        public void set(int index, int value) {
            slotStates[index] = value;
        }

        @Override
        public int getCount() {
            return CRAFTING_SLOTS;
        }
    };
    private int craftingTicksRemaining = MAX_CRAFTING_TICKS;
    private @Nullable RecipeHolder<CraftingRecipe> recipe;

    public FancyCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_CRAFTER.get(), CRAFTING_SLOTS + CRAFTING_RESULT_SLOTS + STORAGE_SLOTS, defaultName("fancy_crafter"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FancyCrafterMenu(id, inventory, this, this.containerData);
    }

    @Override
    public boolean canDisableSlot(int slot) {
        return isCraftingSlot(slot) && this.items.get(slot).isEmpty();
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        return isCraftingSlot(slot) && this.containerData.get(slot) == SLOT_DISABLED;
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        if (!this.canDisableSlot(slot)) return;
        this.containerData.set(slot, disabled ? SLOT_DISABLED : SLOT_ENABLED);
        this.setChanged();
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (isSlotDisabled(slot)) {
            setSlotDisabled(slot, false);
        }
        super.setItem(slot, stack);
        if (slot != CRAFTING_RESULT_SLOT_INDEX) {
            calculateRecipe();
        }
    }

    private void calculateRecipe() {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        RecipeManager recipes = serverLevel.recipeAccess();
        RegistryAccess registries = serverLevel.registryAccess();
        CraftingInput input = asCraftInput();
        recipe = recipes.getRecipeFor(RecipeType.CRAFTING, input, serverLevel).orElse(null);
        super.setItem(CRAFTING_RESULT_SLOT_INDEX, recipe == null ? ItemStack.EMPTY : recipe.value().assemble(input, registries).copy());
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (!stack.getCraftingRemainder().isEmpty()) {
            return false;
        } else if (isSlotDisabled(slot)) {
            return false;
        } else if (!isCraftingSlot(slot)) {
            return false;
        }
        ItemStack slotStack = getItem(slot);
        return slotStack.isEmpty() || slotStack.getCount() < slotStack.getMaxStackSize() && !smallerStackExists(slotStack.getCount(), stack, slot);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        craftingTicksRemaining = input.getIntOr(CRAFTING_TICKS_REMAINING_KEY, 0);
        ContainerHelper.loadAllItems(input, this.items);

        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            this.containerData.set(i, SLOT_ENABLED);
        }

        input.getIntArray(DISABLED_SLOTS_KEY).ifPresent(p_409682_ -> {
            for (int j : p_409682_) {
                if (this.canDisableSlot(j)) {
                    this.containerData.set(j, SLOT_DISABLED);
                }
            }
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(CRAFTING_TICKS_REMAINING_KEY, this.craftingTicksRemaining);
        ContainerHelper.saveAllItems(output, this.items);

        IntList intlist = new IntArrayList();

        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            if (this.isSlotDisabled(i)) {
                intlist.add(i);
            }
        }

        output.putIntArray(DISABLED_SLOTS_KEY, intlist.toIntArray());
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public List<ItemStack> getItems() {
        return this.items.subList(0, CRAFTING_SLOTS);
    }

    @Override
    public void fillStackedContents(StackedItemContents stackedContents) {
        for (ItemStack itemstack : getItems()) {
            stackedContents.accountSimpleStack(itemstack);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FancyCrafterBlockEntity blockEntity) {
        if (blockEntity.recipe == null) return;
        CraftingRecipe recipe = blockEntity.recipe.value();
        CraftingInput input = blockEntity.asCraftInput();
        ItemStack result = recipe.assemble(input, level.registryAccess());
        ItemStack resultStack = blockEntity.getItem(CRAFTING_RESULT_SLOT_INDEX);
        if (!resultStack.isEmpty() && !ItemStack.isSameItemSameComponents(result, resultStack))
            return;
        blockEntity.craftingTicksRemaining--;
        if (blockEntity.craftingTicksRemaining > 0) return;
        result.onCraftedBySystem(level);
        blockEntity.setItem(CRAFTING_RESULT_SLOT_INDEX, blockEntity.tryDispense(level, pos, result, state));
        blockEntity.craftingTicksRemaining = MAX_CRAFTING_TICKS;
        recipe.getRemainingItems(input)
                .stream()
                .filter(e -> !e.isEmpty())
                .forEach(e -> blockEntity.tryDispense(level, pos, e, state));
        List<ItemStack> inputs = new ArrayList<>(input.items()
                .stream()
                .filter(e -> !e.isEmpty())
                .toList());
        // for loop instead of stream chain to prevent CME
        for (int i = CRAFTING_SLOTS + CRAFTING_RESULT_SLOTS; i < blockEntity.getContainerSize(); i++) {
            ItemStack stack = blockEntity.getItem(i);
            if (stack.isEmpty()) continue;
            List<ItemStack> toRemove = new ArrayList<>();
            for (ItemStack e : inputs) {
                if (!ItemStack.isSameItemSameComponents(e, stack)) continue;
                if (e.getCount() >= e.getMaxStackSize()) continue;
                if (stack.isEmpty()) continue;
                e.grow(1);
                toRemove.add(e);
                stack.shrink(1);
            }
            toRemove.forEach(inputs::remove);
        }
        input.items()
                .stream()
                .filter(e -> !e.isEmpty())
                .forEach(e -> e.shrink(1));
        blockEntity.setChanged();
    }

    private boolean smallerStackExists(int currentSize, ItemStack stack, int slot) {
        for (int i = slot + 1; i < CRAFTING_SLOTS; i++) {
            if (!isSlotDisabled(i)) {
                ItemStack slotStack = getItem(i);
                if (slotStack.isEmpty() || slotStack.getCount() < currentSize && ItemStack.isSameItemSameComponents(slotStack, stack))
                    return true;
            }
        }
        return false;
    }

    private ItemStack tryDispense(Level level, BlockPos pos, ItemStack stack, BlockState state) {
        Direction direction = state.getValue(FancyCrafterBlock.FACING);
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

    public int getRedstoneSignal() {
        int count = 0;
        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            if (!getItem(i).isEmpty() || isSlotDisabled(i)) {
                count++;
            }
        }
        return count;
    }

    private boolean isCraftingSlot(int slot) {
        return slot >= 0 && slot < CRAFTING_SLOTS;
    }
}
