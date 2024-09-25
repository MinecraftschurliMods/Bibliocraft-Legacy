package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClipboardBESyncPacket(ClipboardContent content, BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClipboardBESyncPacket> TYPE = new CustomPacketPayload.Type<>(BCUtil.modLoc("clipboard_be_sync"));
    public static final StreamCodec<FriendlyByteBuf, ClipboardBESyncPacket> STREAM_CODEC = StreamCodec.composite(
            ClipboardContent.STREAM_CODEC, ClipboardBESyncPacket::content,
            BlockPos.STREAM_CODEC, ClipboardBESyncPacket::pos,
            ClipboardBESyncPacket::new);

    public static void handle(ClipboardBESyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (!level.isLoaded(packet.pos)) return;
            if (level.getBlockEntity(packet.pos) instanceof ClipboardBlockEntity clipboard) {
                clipboard.setContent(packet.content);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
