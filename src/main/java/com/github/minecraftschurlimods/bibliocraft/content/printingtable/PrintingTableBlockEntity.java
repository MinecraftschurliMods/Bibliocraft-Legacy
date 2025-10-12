package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.slot.HasToggleableSlots;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.stream.IntStream;

public class PrintingTableBlockEntity extends BCMenuBlockEntity implements HasToggleableSlots {
    private static final String MODE_KEY = "mode";
    private static final String DURATION_KEY = "duration";
    private static final String PLAYER_NAME_KEY = "player_name";
    private static final String DISABLED_SLOTS_KEY = "disabled_slots";
    private static final int SLOT_DISABLED = 1;
    private static final int SLOT_ENABLED = 0;
    private final PrintingTableTank tank;
    private final Direction[] directions;
    private final boolean[] disabledSlots = new boolean[9];
    private PrintingTableRecipe recipe;
    private PrintingTableRecipeInput recipeInput;
    private PrintingTableMode mode = PrintingTableMode.BIND;
    private int levelCost = 0;
    private int duration = 0;
    private int maxDuration = 0;
    private Component playerName = null;

    public PrintingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.PRINTING_TABLE.get(), 11, defaultName("printing_table"), pos, state);
        tank = new PrintingTableTank(this, state.is(BCBlocks.IRON_PRINTING_TABLE));
        Direction facing = state.getValue(PrintingTableBlock.FACING);
        directions = new Direction[]{Direction.UP, facing, facing.getClockWise(), facing.getOpposite(), facing.getCounterClockWise(), Direction.DOWN};
    }

    @SuppressWarnings("unused")
    public static void tick(Level level, BlockPos pos, BlockState state, PrintingTableBlockEntity blockEntity) {
        if (blockEntity.duration < blockEntity.maxDuration && blockEntity.isExperienceFull()) {
            blockEntity.duration++;
        }
        if (blockEntity.duration >= blockEntity.maxDuration) {
            blockEntity.duration = 0;
            if (!level.isClientSide()) {
                blockEntity.finishRecipe();
            }
        }
        if (!blockEntity.isExperienceFull()) {
            blockEntity.pullExperience();
        }
        blockEntity.setChanged();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PrintingTableMenu(id, inventory, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read(MODE_KEY, PrintingTableMode.CODEC).ifPresent(this::setMode);
        duration = input.getIntOr(DURATION_KEY, 0);
        input.read(PLAYER_NAME_KEY, ComponentSerialization.CODEC).ifPresent(this::setPlayerName);
        tank.loadAdditional(input);
        int[] tagSlots = input.getIntArray(DISABLED_SLOTS_KEY).orElse(new int[9]);
        for (int i = 0; i < 9; i++) {
            disabledSlots[i] = canDisableSlot(i) && tagSlots[i] == SLOT_DISABLED;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(MODE_KEY, PrintingTableMode.CODEC, getMode());
        output.putInt(DURATION_KEY, duration);
        if (playerName != null) {
            output.store(PLAYER_NAME_KEY, ComponentSerialization.CODEC, playerName);
        }
        tank.saveAdditional(output);
        int[] tagSlots = new int[9];
        for (int i = 0; i < 9; i++) {
            tagSlots[i] = disabledSlots[i] ? SLOT_DISABLED : SLOT_ENABLED;
        }
        output.putIntArray(DISABLED_SLOTS_KEY, tagSlots);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (isSlotDisabled(slot) && !stack.isEmpty()) {
            setSlotDisabled(slot, false);
        }
        super.setItem(slot, stack);
        recipeInput = null;
        if (recipe == null || !recipe.matches(getRecipeInput(), BCUtil.nonNull(getLevel()))) {
            calculateRecipe(false);
            setChanged();
        }
    }

    @Override
    public void setSlotDisabled(int slot, boolean disabled) {
        if (!canDisableSlot(slot)) return;
        disabledSlots[slot] = disabled;
        setChanged();
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        return isCraftingSlot(slot) && disabledSlots[slot];
    }

    @Override
    public boolean canDisableSlot(int slot) {
        return isCraftingSlot(slot) && getItem(slot).isEmpty();
    }

    @Override
    public void onLoad() {
        if (!level().isClientSide()) {
            calculateRecipe(true);
        } else {
            ClientPacketDistributor.sendToServer(new PrintingTableSetRecipePacket(getBlockPos(), 0, 0, 0));
        }
    }

    public PrintingTableTank getFluidCapability() {
        return tank;
    }

    public PrintingTableMode getMode() {
        return mode;
    }

    public void setMode(PrintingTableMode mode) {
        this.mode = mode;
        calculateRecipe(false);
        setChanged();
    }

    public Component getPlayerName() {
        return playerName;
    }

    public void setPlayerName(Component playerName) {
        this.playerName = playerName;
    }

    public int getExperience() {
        return tank.getExperience();
    }

    public void addExperience(int experience) {
        tank.addExperience(experience);
        setChanged();
    }

    public float getProgress() {
        return !isExperienceFull() || maxDuration == 0 ? 0 : duration / (float) maxDuration;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public int getExperienceCost() {
        return BCUtil.getExperienceForLevel(levelCost);
    }

    public boolean isExperienceFull() {
        int experienceCost = getExperienceCost();
        return experienceCost <= 0 || getExperience() >= experienceCost;
    }

    public void setFromPacket(PrintingTableSetRecipePacket packet) {
        duration = packet.duration();
        maxDuration = packet.maxDuration();
        levelCost = packet.levelCost();
        tank.clear();
        setChanged();
    }

    private void finishRecipe() {
        if (recipe == null) return;
        List<ItemStack> remainingItems = recipe.getRemainingItems(getRecipeInput());
        ItemStack stack = getItem(10);
        ItemStack result = recipe.postProcess(recipe.assemble(getRecipeInput(), level().registryAccess()), this);
        if (!stack.isEmpty() && !ItemStack.isSameItemSameComponents(stack, result)) return;
        result.setCount(stack.getCount() + 1);
        setItem(10, result);
        IntStream.range(0, 10)
                .mapToObj(this::getItem)
                .forEach(e -> e.shrink(1));
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack remaining = remainingItems.get(i);
            if (remaining.isEmpty()) continue;
            setItem(i, remaining.copy());
        }
        calculateRecipe(false);
    }

    private void pullExperience() {
        List<Fluid> fluids = level()
                .registryAccess()
                .lookupOrThrow(Registries.FLUID)
                .getOrThrow(Tags.Fluids.EXPERIENCE)
                .stream()
                .map(Holder::value)
                .toList();
        for (Direction direction : directions) {
            IFluidHandler capability = level().getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos().offset(direction.getNormal()), direction);
            if (capability == null) continue;
            for (Fluid fluid : fluids) {
                tank.fillFromCapability(capability, fluid);
                if (isExperienceFull()) return;
            }
        }
    }

    private void calculateRecipe(boolean onLoad) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        recipe = serverLevel
                .getServer()
                .getRecipeManager()
                .recipeMap()
                .getRecipesFor(BCRecipes.PRINTING_TABLE.get(), getRecipeInput(), serverLevel)
                .map(RecipeHolder::value)
                .filter(e -> e.getMode() == mode)
                .findFirst()
                .orElse(null);
        if (recipe != null) {
            ItemStack output = getItem(10);
            if (!output.isEmpty() && (output.getCount() >= output.getMaxStackSize() || !ItemStack.isSameItemSameComponents(recipe.assemble(getRecipeInput(), level().registryAccess()), output))) {
                recipe = null;
            }
        }
        if (!onLoad) {
            duration = 0;
        }
        maxDuration = recipe == null ? 0 : recipe.getDuration();
        levelCost = recipe == null ? 0 : recipe.getExperienceLevelCost(recipeInput.right().copy(), serverLevel);
        tank.clear();
        PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new PrintingTableSetRecipePacket(getBlockPos(), duration, maxDuration, levelCost));
    }

    private PrintingTableRecipeInput getRecipeInput() {
        if (recipeInput == null) {
            recipeInput = new PrintingTableRecipeInput(IntStream.range(0, 9).mapToObj(this::getItem).toList(), getItem(9));
        }
        return recipeInput;
    }

    private boolean isCraftingSlot(int slot) {
        return slot >= 0 && slot < 9;
    }
}
