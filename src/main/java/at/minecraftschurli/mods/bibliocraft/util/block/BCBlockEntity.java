package at.minecraftschurli.mods.bibliocraft.util.block;

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
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/// Abstract superclass for all block entities in this mod.
public abstract class BCBlockEntity extends BlockEntity implements ItemOwner {
    private final BCItemHandler itemHandler;
    private final int slotCapacity;
    private LockCode lockKey = LockCode.NO_LOCK;

    /// @param type          The [BlockEntityType] to use.
    /// @param containerSize The size of the container.
    /// @param pos           The position of this BE.
    /// @param state         The state of this BE.
    public BCBlockEntity(BlockEntityType<?> type, int containerSize, BlockPos pos, BlockState state) {
        this(type, containerSize, Item.ABSOLUTE_MAX_STACK_SIZE, pos, state);
    }

    /// @param type          The [BlockEntityType] to use.
    /// @param containerSize The size of the container.
    /// @param slotCapacity  The max capacity of each slot.
    /// @param pos           The position of this BE.
    /// @param state         The state of this BE.
    public BCBlockEntity(BlockEntityType<?> type, int containerSize, int slotCapacity, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemHandler = new BCItemHandler(containerSize, this::isValid, this::getCapacity, this::setChanged);
        this.slotCapacity = slotCapacity;
    }

    public BCItemHandler getItemHandler() {
        return this.itemHandler;
    }

    public int getContainerSize() {
        return this.itemHandler.size(); 
    }

    public LockCode getLockKey() {
        return this.lockKey;
    }

    public void setLockKey(LockCode lockKey) {
        this.lockKey = lockKey;
        setChanged();
        level().sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public boolean isLocked() {
        return LockCode.NO_LOCK.equals(this.lockKey);
    }

    public int getCapacity(ItemResource resource) {
        return Math.min(this.slotCapacity, resource.getMaxStackSize());
    }

    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public boolean isValid(int slot, ItemResource resource) {
        return true;
    }

    public boolean isEmpty(int index) {
        return this.itemHandler.isEmpty(index);
    }

    public ItemStack getItem(int index) {
        return this.itemHandler.getResource(index).toStack(this.itemHandler.getAmountAsInt(index));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter componentGetter) {
        super.applyImplicitComponents(componentGetter);
        this.lockKey = componentGetter.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
        this.itemHandler.fillFromComponent(componentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (isLocked()) {
            components.set(DataComponents.LOCK, this.lockKey);
        }
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.itemHandler.copyToList()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard(LockCode.TAG_LOCK);
        output.discard(BCItemHandler.ITEMS_TAG);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.lockKey = LockCode.fromTag(input);
        this.itemHandler.deserialize(input);
        requestModelDataUpdate();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        this.lockKey.addToTag(output);
        this.itemHandler.serialize(output);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Nullable
    public ResourceHandler<ItemResource> getItemCapability(@Nullable Direction side) {
        return this.itemHandler;
    }

    @Override
    public Level level() {
        return Objects.requireNonNull(this.level);
    }

    @Override
    public Vec3 position() {
        return Vec3.atCenterOf(this.worldPosition);
    }

    @Override
    public float getVisualRotationYInDegrees() {
        BlockState blockState = getBlockState();
        return blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() : 0;
    }
    
    public NonNullList<ItemStack> getContents() {
        return this.itemHandler.copyToList();
    }
}
