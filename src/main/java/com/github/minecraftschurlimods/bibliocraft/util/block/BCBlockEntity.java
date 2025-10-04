package com.github.minecraftschurlimods.bibliocraft.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract superclass for all block entities in this mod.
 */
public abstract class BCBlockEntity extends BlockEntity implements Container, ItemOwner {
    private static final String ITEMS_TAG = "items";
    protected final BCItemHandler items;
    private LockCode lockKey = LockCode.NO_LOCK;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos pos, BlockState state) {
        super(type, pos, state);
        items = new BCItemHandler(containerSize, this);
    }

    public LockCode getLockKey() {
        return lockKey;
    }

    public void setLockKey(LockCode lockKey) {
        this.lockKey = lockKey;
        setChanged();
        level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public int getContainerSize() {
        return items.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!items.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot < getContainerSize() ? items.getStackInSlot(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return slot < getContainerSize() ? items.getStackInSlot(slot).split(count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < getContainerSize()) {
            ItemStack stack = items.getStackInSlot(slot);
            items.setStackInSlot(slot, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot < getContainerSize()) {
            items.setStackInSlot(slot, stack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = getBlockPos();
        return level().getBlockEntity(pos) == this && player.distanceToSqr(Vec3.atCenterOf(pos)) <= 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        lockKey = LockCode.fromTag(input);
        input.child(ITEMS_TAG).ifPresent(items::deserialize);
        requestModelDataUpdate();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        lockKey.addToTag(output);
        items.serialize(output.child(ITEMS_TAG));
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    public IItemHandler getCapability(@Nullable Direction side) {
        return items;
    }

    public Level level() {
        return level;
    }

    @Override
    public Vec3 position() {
        return Vec3.atCenterOf(worldPosition);
    }

    @Override
    public float getVisualRotationYInDegrees() {
        BlockState blockState = getBlockState();
        return blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() : 0;
    }
}
