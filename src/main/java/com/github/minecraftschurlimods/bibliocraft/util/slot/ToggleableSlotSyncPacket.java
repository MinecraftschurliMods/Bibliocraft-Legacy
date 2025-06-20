package com.github.minecraftschurlimods.bibliocraft.util.slot;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleableSlotSyncPacket(BlockPos pos, int slot, boolean disabled) implements CustomPacketPayload {
    public static final Type<ToggleableSlotSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("toggleable_slot_sync"));
    public static final StreamCodec<ByteBuf, ToggleableSlotSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ToggleableSlotSyncPacket::pos,
            ByteBufCodecs.INT, ToggleableSlotSyncPacket::slot,
            ByteBufCodecs.BOOL, ToggleableSlotSyncPacket::disabled,
            ToggleableSlotSyncPacket::new);

    public void handle(IPayloadContext context) {
        Level level = context.player().level();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof HasToggleableSlots slots) {
            slots.setSlotDisabled(slot, disabled);
            blockEntity.setChanged();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
