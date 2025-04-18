package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StockroomCatalogSyncPacket(StockroomCatalogContent content) implements CustomPacketPayload {
    public static final Type<StockroomCatalogSyncPacket> TYPE = new Type<>(BCUtil.modLoc("stockroom_catalog_sync"));
    public static final StreamCodec<FriendlyByteBuf, StockroomCatalogSyncPacket> STREAM_CODEC = StreamCodec.composite(
            StockroomCatalogContent.STREAM_CODEC, StockroomCatalogSyncPacket::content,
            StockroomCatalogSyncPacket::new);

    public void handle(IPayloadContext context) {
        Player player = context.player();
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(BCItems.STOCKROOM_CATALOG)) {
            stack = player.getOffhandItem();
            if (!stack.is(BCItems.STOCKROOM_CATALOG)) return;
        }
        stack.set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, content);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
