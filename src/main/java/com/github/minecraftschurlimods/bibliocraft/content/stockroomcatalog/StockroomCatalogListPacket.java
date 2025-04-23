package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record StockroomCatalogListPacket(List<BlockPos> containers, List<StockroomCatalogItemEntry> items) implements CustomPacketPayload {
    public static final Type<StockroomCatalogListPacket> TYPE = new Type<>(BCUtil.modLoc("stockroom_catalog_list"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StockroomCatalogListPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), StockroomCatalogListPacket::containers,
            StockroomCatalogItemEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), StockroomCatalogListPacket::items,
            StockroomCatalogListPacket::new);

    public void handle(IPayloadContext context) {
        ClientUtil.setStockroomCatalogList(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
