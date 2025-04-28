package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClipboardSyncPacket(ClipboardContent content, InteractionHand hand) implements CustomPacketPayload {
    public static final Type<ClipboardSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("clipboard_sync"));
    public static final StreamCodec<FriendlyByteBuf, ClipboardSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ClipboardContent.STREAM_CODEC, ClipboardSyncPacket::content,
            BCUtil.INTERACTION_HAND_STREAM_CODEC, ClipboardSyncPacket::hand,
            ClipboardSyncPacket::new);

    public void handle(IPayloadContext context) {
        context.player().getItemInHand(hand).set(BCDataComponents.CLIPBOARD_CONTENT, content);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
