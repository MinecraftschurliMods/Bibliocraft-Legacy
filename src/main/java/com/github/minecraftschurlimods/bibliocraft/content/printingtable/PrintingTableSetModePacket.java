package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PrintingTableSetModePacket(BlockPos pos, PrintingTableMode mode) implements CustomPacketPayload {
    public static final Type<PrintingTableSetModePacket> TYPE = new Type<>(BCUtil.bcLoc("printing_table_set_mode"));
    public static final StreamCodec<ByteBuf, PrintingTableSetModePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PrintingTableSetModePacket::pos,
            PrintingTableMode.STREAM_CODEC, PrintingTableSetModePacket::mode,
            PrintingTableSetModePacket::new);

    public void handle(IPayloadContext context) {
        if (context.player().level().getBlockEntity(pos) instanceof PrintingTableBlockEntity printingTable) {
            printingTable.setMode(mode);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
