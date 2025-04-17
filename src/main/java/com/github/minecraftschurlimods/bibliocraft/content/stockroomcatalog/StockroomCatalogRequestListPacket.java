package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record StockroomCatalogRequestListPacket(StockroomCatalogSorting.Container containerSorting, StockroomCatalogSorting.Item itemSorting) implements CustomPacketPayload {
    public static final Type<StockroomCatalogRequestListPacket> TYPE = new Type<>(BCUtil.modLoc("stockroom_catalog_request_list"));
    public static final StreamCodec<ByteBuf, StockroomCatalogRequestListPacket> STREAM_CODEC = StreamCodec.composite(
            StockroomCatalogSorting.Container.STREAM_CODEC, StockroomCatalogRequestListPacket::containerSorting,
            StockroomCatalogSorting.Item.STREAM_CODEC, StockroomCatalogRequestListPacket::itemSorting,
            StockroomCatalogRequestListPacket::new);

    public static void handle(StockroomCatalogRequestListPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            List<BlockPos> containers = StockroomCatalogItem.calculatePositions(context.player().getItemInHand(context.player().getUsedItemHand()), context.player().level(), context.player(), packet.containerSorting());
            List<StockroomCatalogItemEntry> items = StockroomCatalogItem.calculateItems(containers, context.player().level(), packet.itemSorting());
            PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new StockroomCatalogListPacket(containers, items));
        });
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
