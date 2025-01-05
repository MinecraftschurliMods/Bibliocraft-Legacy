package com.github.minecraftschurlimods.bibliocraft.content.clock;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ClockSyncPacket(BlockPos pos, List<ClockTrigger> triggers) implements CustomPacketPayload {
    public static final Type<ClockSyncPacket> TYPE = new Type<>(BCUtil.modLoc("clock_sync"));
    public static final StreamCodec<FriendlyByteBuf, ClockSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClockSyncPacket::pos,
            ClockTrigger.STREAM_CODEC.apply(ByteBufCodecs.list()), ClockSyncPacket::triggers,
            ClockSyncPacket::new);

    @SuppressWarnings("deprecation")
    public static void handle(ClockSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockPos pos = packet.pos();
            Level level = context.player().level();
            if (!level.hasChunkAt(pos)) return;
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof ClockBlockEntity clock)) return;
            clock.setFromPacket(packet);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
