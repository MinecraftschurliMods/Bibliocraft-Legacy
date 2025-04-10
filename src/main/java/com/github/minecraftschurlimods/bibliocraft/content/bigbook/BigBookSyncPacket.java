package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BigBookSyncPacket(BigBookContent content, InteractionHand hand) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BigBookSyncPacket> TYPE = new CustomPacketPayload.Type<>(BCUtil.modLoc("big_book_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
            BCUtil.enumStreamCodec(InteractionHand::values, InteractionHand::ordinal), BigBookSyncPacket::hand,
            BigBookSyncPacket::new);

    public static void handle(BigBookSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> context.player().getItemInHand(packet.hand).set(BCDataComponents.BIG_BOOK_CONTENT, packet.content));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
