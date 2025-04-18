package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record StockroomCatalogRequestListPacket(StockroomCatalogSorting.Container containerSorting, StockroomCatalogSorting.Item itemSorting) implements CustomPacketPayload {
    public static final Type<StockroomCatalogRequestListPacket> TYPE = new Type<>(BCUtil.modLoc("stockroom_catalog_request_list"));
    public static final StreamCodec<ByteBuf, StockroomCatalogRequestListPacket> STREAM_CODEC = StreamCodec.composite(
            StockroomCatalogSorting.Container.STREAM_CODEC, StockroomCatalogRequestListPacket::containerSorting,
            StockroomCatalogSorting.Item.STREAM_CODEC, StockroomCatalogRequestListPacket::itemSorting,
            StockroomCatalogRequestListPacket::new);

    public void handle(IPayloadContext context) {
        Player player = context.player();
        List<BlockPos> containers = StockroomCatalogItem.calculatePositions(player.getItemInHand(player.getUsedItemHand()), player.level(), player, containerSorting);
        List<StockroomCatalogItemEntry> items = StockroomCatalogItem.calculateItems(containers, player.level(), itemSorting);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new StockroomCatalogListPacket(containers, items));
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
