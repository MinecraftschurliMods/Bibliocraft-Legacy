package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BigBookSignPacket(WrittenBigBookContent content, InteractionHand hand) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BigBookSignPacket> TYPE = new CustomPacketPayload.Type<>(BCUtil.modLoc("big_book_sign"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSignPacket> STREAM_CODEC = StreamCodec.composite(
            WrittenBigBookContent.STREAM_CODEC, BigBookSignPacket::content,
            BCUtil.enumStreamCodec(InteractionHand::values, InteractionHand::ordinal), BigBookSignPacket::hand,
            BigBookSignPacket::new);

    public static void handle(BigBookSignPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ItemStack stack = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
            stack.set(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, packet.content);
            context.player().setItemInHand(packet.hand, stack);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
