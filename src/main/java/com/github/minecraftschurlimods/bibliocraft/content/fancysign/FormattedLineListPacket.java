package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FormattedLineListPacket(FormattedLineList list, BlockPos pos, boolean back) implements CustomPacketPayload {
    public static final Type<FormattedLineListPacket> TYPE = new Type<>(BCUtil.modLoc("formatted_line_list"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FormattedLineListPacket> STREAM_CODEC = StreamCodec.composite(
            FormattedLineList.STREAM_CODEC, FormattedLineListPacket::list,
            BlockPos.STREAM_CODEC, FormattedLineListPacket::pos,
            ByteBufCodecs.BOOL, FormattedLineListPacket::back,
            FormattedLineListPacket::new);

    public static void handle(FormattedLineListPacket packet, IPayloadContext context) {
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
