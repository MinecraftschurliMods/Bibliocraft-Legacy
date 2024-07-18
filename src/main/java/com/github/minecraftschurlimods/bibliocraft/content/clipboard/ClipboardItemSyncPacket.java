package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClipboardItemSyncPacket(ClipboardContent content) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClipboardItemSyncPacket> TYPE = new CustomPacketPayload.Type<>(BCUtil.modLoc("clipboard_item_sync"));
    public static final StreamCodec<FriendlyByteBuf, ClipboardItemSyncPacket> STREAM_CODEC = StreamCodec.composite(ClipboardContent.STREAM_CODEC, ClipboardItemSyncPacket::content, ClipboardItemSyncPacket::new);

    public static void handle(ClipboardItemSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getMainHandItem();
            if (!stack.is(BCItems.CLIPBOARD)) {
                stack = player.getOffhandItem();
                if (!stack.is(BCItems.CLIPBOARD)) return;
            }
            stack.set(BCDataComponents.CLIPBOARD_CONTENT, packet.content);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
