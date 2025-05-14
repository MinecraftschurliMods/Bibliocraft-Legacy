package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StockroomCatalogSyncPacket(StockroomCatalogContent content, Either<InteractionHand, BlockPos> target) implements CustomPacketPayload {
    public static final Type<StockroomCatalogSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("stockroom_catalog_sync"));
    public static final StreamCodec<FriendlyByteBuf, StockroomCatalogSyncPacket> STREAM_CODEC = StreamCodec.composite(
            StockroomCatalogContent.STREAM_CODEC, StockroomCatalogSyncPacket::content,
            ByteBufCodecs.either(CodecUtil.INTERACTION_HAND_STREAM_CODEC, BlockPos.STREAM_CODEC), StockroomCatalogSyncPacket::target,
            StockroomCatalogSyncPacket::new);

    public void handle(IPayloadContext context) {
        Player player = context.player();
        target.ifLeft(left -> player.getItemInHand(left).set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, content));
        target.ifRight(right -> LecternUtil.tryGetLecternAndRun(player.level(), right,
                lectern -> lectern.getBook().update(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT, $ -> content)));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
