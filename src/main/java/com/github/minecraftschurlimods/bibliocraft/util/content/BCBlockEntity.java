package com.github.minecraftschurlimods.bibliocraft.util.content;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Abstract superclass for most block entities this mod has.
 */
public abstract class BCBlockEntity extends BlockEntity implements Container {
    private static final String ITEMS_TAG = "items";
    protected final ItemStackHandler items;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos pos, BlockState state) {
        super(type, pos, state);
        items = new ItemStackHandler(containerSize) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
                Objects.requireNonNull(level).sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
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
        return Objects.requireNonNull(level).getBlockEntity(pos) == this && player.distanceToSqr(Vec3.atCenterOf(pos)) <= 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTag(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveTag(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveTag(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        loadTag(tag);
    }

    /**
     * Helper method for loading block entity data from a {@link CompoundTag}.
     *
     * @param tag The {@link CompoundTag} to load from.
     */
    protected void loadTag(CompoundTag tag) {
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
        requestModelDataUpdate();
    }

    /**
     * Helper method for saving block entity data into a {@link CompoundTag}.
     *
     * @param tag The {@link CompoundTag} to save into.
     */
    protected void saveTag(CompoundTag tag) {
        tag.put(ITEMS_TAG, items.serializeNBT());
    }
}
