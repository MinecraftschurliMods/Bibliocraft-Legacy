package com.github.minecraftschurlimods.bibliocraft.util.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract superclass for all block entities in this mod.
 */
public abstract class BCBlockEntity extends BlockEntity implements Container, ItemOwner {
    private static final String ITEMS_TAG = "items";
    protected final ResourceHandler<ItemResource> capability = VanillaContainerWrapper.of(this);
    protected final NonNullList<ItemStack> items;
    private LockCode lockKey = LockCode.NO_LOCK;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos pos, BlockState state) {
        super(type, pos, state);
        items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
    }

    public LockCode getLockKey() {
        return lockKey;
    }

    public void setLockKey(LockCode lockKey) {
        this.lockKey = lockKey;
        setChanged();
        level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public boolean isLocked() {
        return LockCode.NO_LOCK.equals(this.lockKey);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot < getContainerSize() ? items.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(items, slot, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, false);
    }

    @Override
    public void setItem(int slot, ItemStack stack, boolean insideTransaction) {
        items.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (!insideTransaction) {
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
       items.clear();
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.lockKey = componentGetter.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (this.isLocked()) {
            components.set(DataComponents.LOCK, this.lockKey);
        }

        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard(LockCode.TAG_LOCK);
        output.discard(ITEMS_TAG);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.lockKey = LockCode.fromTag(input);
        input.child(ITEMS_TAG).ifPresent(i -> ContainerHelper.loadAllItems(i, items));
        requestModelDataUpdate();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.lockKey.addToTag(output);
        ContainerHelper.saveAllItems(output.child(ITEMS_TAG), items);
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

    public ResourceHandler<ItemResource> getCapability(@Nullable Direction side) {
        return capability;
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
