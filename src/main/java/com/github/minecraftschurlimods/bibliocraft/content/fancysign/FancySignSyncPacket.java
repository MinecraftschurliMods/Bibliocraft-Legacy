package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FancySignSyncPacket(FancySignContent list, BlockPos pos, boolean back) implements CustomPacketPayload {
    public static final Type<FancySignSyncPacket> TYPE = new Type<>(BCUtil.modLoc("fancy_sign_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FancySignSyncPacket> STREAM_CODEC = StreamCodec.composite(
            FancySignContent.STREAM_CODEC, FancySignSyncPacket::list,
            BlockPos.STREAM_CODEC, FancySignSyncPacket::pos,
            ByteBufCodecs.BOOL, FancySignSyncPacket::back,
            FancySignSyncPacket::new);

    public static void handle(FancySignSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockPos pos = packet.pos();
            if (!(context.player().level().getBlockEntity(pos) instanceof FancySignBlockEntity sign)) return;
            if (packet.back()) {
                sign.setBackContent(packet.list());
            } else {
                sign.setFrontContent(packet.list());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
