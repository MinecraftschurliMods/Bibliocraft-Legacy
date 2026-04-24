package at.minecraftschurli.mods.bibliocraft.content.clock;

import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ClockSyncPacket(BlockPos pos, boolean tickSound, List<ClockTrigger> triggers) implements CustomPacketPayload {
    public static final Type<ClockSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("clock_sync"));
    public static final StreamCodec<FriendlyByteBuf, ClockSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClockSyncPacket::pos,
            ByteBufCodecs.BOOL, ClockSyncPacket::tickSound,
            ClockTrigger.STREAM_CODEC.apply(ByteBufCodecs.list()), ClockSyncPacket::triggers,
            ClockSyncPacket::new);

    @SuppressWarnings("deprecation")
    public void handle(IPayloadContext context) {
        BlockPos pos = pos();
        Level level = context.player().level();
        if (!level.hasChunkAt(pos)) return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ClockBlockEntity clock)) return;
        clock.setFromPacket(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
