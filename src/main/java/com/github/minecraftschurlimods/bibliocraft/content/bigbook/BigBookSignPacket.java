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
    public static final Type<BigBookSignPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sign"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSignPacket> STREAM_CODEC = StreamCodec.composite(
            WrittenBigBookContent.STREAM_CODEC, BigBookSignPacket::content,
            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSignPacket::hand,
            BigBookSignPacket::new);

    public void handle(IPayloadContext context) {
        ItemStack stack = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
        stack.set(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, content);
        context.player().setItemInHand(hand, stack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
