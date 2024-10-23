package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.client.screen.StockroomCatalogScreen;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.Minecraft;
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

    public static void handle(StockroomCatalogListPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof StockroomCatalogScreen screen) {
                screen.setFromPacket(packet);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
