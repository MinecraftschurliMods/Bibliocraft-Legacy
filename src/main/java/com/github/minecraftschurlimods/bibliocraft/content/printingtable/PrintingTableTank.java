package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class PrintingTableTank extends FluidStacksResourceHandler {
    static final int EXPERIENCE_MULTIPLIER = 20;
    private final PrintingTableBlockEntity blockEntity;
    private final boolean acceptAutomation;

    public PrintingTableTank(PrintingTableBlockEntity blockEntity, boolean acceptAutomation) {
        super(1, blockEntity.getExperienceCost() * EXPERIENCE_MULTIPLIER);
        this.blockEntity = blockEntity;
        this.acceptAutomation = acceptAutomation;
    }

    public void fillFromCapability(ResourceHandler<FluidResource> capability, TransactionContext transaction) {
        ResourceHandlerUtil.move(capability, this, f -> f.is(Tags.Fluids.EXPERIENCE), Integer.MAX_VALUE, transaction);
    }

    public void clear() {
        set(0, FluidResource.EMPTY, 0);
    }

    public void update(PrintingTableTankSyncPacket packet) {
        set(0, packet.resource(), packet.amount());
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        super.onContentsChanged(index, previousContents);
        blockEntity.setChanged();
        if (blockEntity.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = blockEntity.getBlockPos();
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(pos), new PrintingTableTankSyncPacket(pos, getResource(0), getAmountAsInt(0)));
        }
    }

    @Override
    public int getCapacity(int index, FluidResource resource) {
        return index == 0 ? getCapacity() : 0;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return index == 0 && resource.is(Tags.Fluids.EXPERIENCE);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (index != 0 || !acceptAutomation) return 0;
        return super.insert(index, resource, amount, transaction);
    }

    public boolean isFull() {
        return getCapacity() <= getAmountAsInt(0);
    }

    private int getCapacity() {
        return blockEntity.getExperienceCost() * EXPERIENCE_MULTIPLIER;
    }
}
