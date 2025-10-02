package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public record PrintingTableTankSyncPacket(BlockPos pos, FluidResource resource, int amount) implements CustomPacketPayload {
    public static final Type<PrintingTableTankSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("printing_table_tank_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableTankSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PrintingTableTankSyncPacket::pos,
            FluidResource.STREAM_CODEC, PrintingTableTankSyncPacket::resource,
            ByteBufCodecs.INT, PrintingTableTankSyncPacket::amount,
            PrintingTableTankSyncPacket::new);

    public void handle(IPayloadContext context) {
        if (context.player().level().getBlockEntity(pos) instanceof PrintingTableBlockEntity printingTable) {
            printingTable.syncTank(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
