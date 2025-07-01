package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PrintingTableSetRecipePacket(BlockPos pos, int duration, int maxDuration, int levelCost) implements CustomPacketPayload {
    public static final Type<PrintingTableSetRecipePacket> TYPE = new Type<>(BCUtil.bcLoc("printing_table_set_recipe"));
    public static final StreamCodec<ByteBuf, PrintingTableSetRecipePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PrintingTableSetRecipePacket::pos,
            ByteBufCodecs.INT, PrintingTableSetRecipePacket::duration,
            ByteBufCodecs.INT, PrintingTableSetRecipePacket::maxDuration,
            ByteBufCodecs.INT, PrintingTableSetRecipePacket::levelCost,
            PrintingTableSetRecipePacket::new);

    public void handle(IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!(level.getBlockEntity(pos) instanceof PrintingTableBlockEntity blockEntity)) return;
        if (level.isClientSide()) {
            blockEntity.setFromPacket(this);
        } else if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new PrintingTableSetRecipePacket(pos, blockEntity.getDuration(), blockEntity.getMaxDuration(), blockEntity.getLevelCost()));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
