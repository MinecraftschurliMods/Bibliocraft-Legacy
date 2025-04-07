package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BigBookSyncPacket(BigBookContent content) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BigBookSyncPacket> TYPE = new CustomPacketPayload.Type<>(BCUtil.modLoc("big_book_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
            BigBookSyncPacket::new);

    public static void handle(BigBookSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getMainHandItem();
            if (!stack.is(BCItems.BIG_BOOK)) {
                stack = player.getOffhandItem();
                if (!stack.is(BCItems.BIG_BOOK)) return;
            }
            stack.set(BCDataComponents.BIG_BOOK_CONTENT, packet.content);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
