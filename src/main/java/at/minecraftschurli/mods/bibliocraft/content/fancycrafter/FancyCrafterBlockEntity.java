package at.minecraftschurli.mods.bibliocraft.content.fancycrafter;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.block.BCInputItemHandler;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCOutputItemHandler;
import at.minecraftschurli.mods.bibliocraft.util.slot.HasToggleableSlots;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
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
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FancyCrafterBlockEntity extends BCMenuBlockEntity implements HasToggleableSlots {
    private static final String CRAFTING_TICKS_REMAINING_KEY = "crafting_ticks_remaining";
    private static final String DISABLED_SLOTS_KEY = "disabled_slots";
    private static final IntList INPUTS = IntList.of(IntStream.range(0, 18).filter(i -> i != 9).toArray());
    private static final IntList OUTPUTS = IntList.of(9);
    private static final int WIDTH = 3;
    private static final int HEIGHT = 3;
    static final int SLOT_DISABLED = 1;
    static final int SLOT_ENABLED = 0;
    static final int CRAFTING_SLOTS = WIDTH * HEIGHT;
    static final int CRAFTING_RESULT_SLOTS = 1;
    static final int STORAGE_SLOTS = 8;
    static final int CRAFTING_RESULT_SLOT_INDEX = CRAFTING_SLOTS;
    private static final int MAX_CRAFTING_TICKS = 6;
    private final BCInputItemHandler inputItemHandler = new BCInputItemHandler(getContainerSize(), this, INPUTS);
    private final BCOutputItemHandler outputItemHandler = new BCOutputItemHandler(getContainerSize(), this, OUTPUTS);
    private final ContainerData containerData = new ContainerData() {
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
    @Nullable
    private RecipeHolder<CraftingRecipe> recipe;

    public FancyCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_CRAFTER.get(), CRAFTING_SLOTS + CRAFTING_RESULT_SLOTS + STORAGE_SLOTS, defaultName("fancy_crafter"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FancyCrafterMenu(id, inventory, this, containerData);
    }

    @Override
    public boolean canDisableSlot(int slot) {
        return isCraftingSlot(slot) && getContents().get(slot).isEmpty();
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        return isCraftingSlot(slot) && containerData.get(slot) == SLOT_DISABLED;
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        if (!canDisableSlot(slot)) return;
        containerData.set(slot, disabled ? SLOT_DISABLED : SLOT_ENABLED);
        setChanged();
    }

    private void calculateRecipe() {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        RecipeManager recipes = serverLevel.recipeAccess();
        CraftingInput input = CraftingInput.ofPositioned(WIDTH, HEIGHT, getContents()).input();
        recipe = recipes.getRecipeFor(RecipeType.CRAFTING, input, serverLevel).orElse(null);
        if (recipe == null) {
            getItemHandler().set(CRAFTING_RESULT_SLOT_INDEX, ItemResource.EMPTY, 0);
        } else {
            ItemStack assembled = recipe.value().assemble(input);
            getItemHandler().set(CRAFTING_RESULT_SLOT_INDEX, ItemResource.of(assembled), assembled.count());
        }
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        if (resource.getItem().getCraftingRemainder(resource.toStack()) != null || isSlotDisabled(slot) || !isCraftingSlot(slot)) return false;
        ItemStack slotStack = getItem(slot);
        return slotStack.isEmpty() || slotStack.getCount() < slotStack.getMaxStackSize() && !smallerStackExists(slotStack.getCount(), resource.toStack(), slot);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        craftingTicksRemaining = input.getIntOr(CRAFTING_TICKS_REMAINING_KEY, 0);
        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            containerData.set(i, SLOT_ENABLED);
        }
        input.getIntArray(DISABLED_SLOTS_KEY).ifPresent(i -> {
            for (int j : i) {
                if (canDisableSlot(j)) {
                    containerData.set(j, SLOT_DISABLED);
                }
            }
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(CRAFTING_TICKS_REMAINING_KEY, this.craftingTicksRemaining);
        IntList intlist = new IntArrayList();
        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            if (this.isSlotDisabled(i)) {
                intlist.add(i);
            }
        }
        output.putIntArray(DISABLED_SLOTS_KEY, intlist.toIntArray());
    }

    @Override
    @Nullable
    public ResourceHandler<ItemResource> getItemCapability(@Nullable Direction side) {
        return side == null ? null : side == Direction.DOWN ? outputItemHandler : inputItemHandler;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FancyCrafterBlockEntity blockEntity) {
        if (blockEntity.recipe == null) return;
        CraftingRecipe recipe = blockEntity.recipe.value();
        CraftingInput input = CraftingInput.ofPositioned(WIDTH, HEIGHT, blockEntity.getContents()).input();
        ItemStack result = recipe.assemble(input);
        ItemStack resultStack = blockEntity.getItem(CRAFTING_RESULT_SLOT_INDEX);
        if (!resultStack.isEmpty() && !ItemStack.isSameItemSameComponents(result, resultStack))
            return;
        blockEntity.craftingTicksRemaining--;
        if (blockEntity.craftingTicksRemaining > 0) return;
        result.onCraftedBySystem(level);
        ItemStack dispensed = blockEntity.tryDispense(level, pos, result, state);
        if (blockEntity.isSlotDisabled(CRAFTING_RESULT_SLOT_INDEX)) {
            blockEntity.setSlotDisabled(CRAFTING_RESULT_SLOT_INDEX, false);
        }
        blockEntity.getItemHandler().set(CRAFTING_RESULT_SLOT_INDEX, ItemResource.of(dispensed), dispensed.count());
        blockEntity.calculateRecipe();
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
                if (!ItemStack.isSameItemSameComponents(e, stack) || e.getCount() >= e.getMaxStackSize()) continue;
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
            if (isSlotDisabled(i)) continue;
            ItemStack slotStack = getItem(i);
            if (slotStack.isEmpty() || slotStack.getCount() < currentSize && ItemStack.isSameItemSameComponents(slotStack, stack)) return true;
        }
        return false;
    }

    private ItemStack tryDispense(Level level, BlockPos pos, ItemStack stack, BlockState state) {
        Direction direction = state.getValue(FancyCrafterBlock.FACING);
        BCUtil.tryInsert(level, pos.relative(direction), direction.getOpposite(), stack, this);
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
