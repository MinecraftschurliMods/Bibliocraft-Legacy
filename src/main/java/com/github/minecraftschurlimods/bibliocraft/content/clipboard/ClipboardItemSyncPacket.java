package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClipboardItemSyncPacket(DataComponentPatch patch) implements CustomPacketPayload {
    public static final Type<ClipboardItemSyncPacket> TYPE = new Type<>(BCUtil.modLoc("clipboard_item_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClipboardItemSyncPacket> STREAM_CODEC = DataComponentPatch.STREAM_CODEC.map(ClipboardItemSyncPacket::new, ClipboardItemSyncPacket::patch);

    public static void sync(DataComponentPatch patch) {
        PacketDistributor.sendToServer(new ClipboardItemSyncPacket(patch));
    }

    public void handle(IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(BCItems.CLIPBOARD)) {
            stack = player.getOffhandItem();
            if (!stack.is(BCItems.CLIPBOARD)) return;
        }
        stack.applyComponents(patch());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
