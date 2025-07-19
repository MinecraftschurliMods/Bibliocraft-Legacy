package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record PrintingTableInputPacket(BlockPos pos, Optional<PrintingTableMode> mode, Optional<Integer> experience) implements CustomPacketPayload {
    public static final Type<PrintingTableInputPacket> TYPE = new Type<>(BCUtil.bcLoc("printing_table_set_mode"));
    public static final StreamCodec<ByteBuf, PrintingTableInputPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PrintingTableInputPacket::pos,
            PrintingTableMode.STREAM_CODEC.apply(ByteBufCodecs::optional), PrintingTableInputPacket::mode,
            ByteBufCodecs.INT.apply(ByteBufCodecs::optional), PrintingTableInputPacket::experience,
            PrintingTableInputPacket::new);

    public PrintingTableInputPacket(BlockPos pos, PrintingTableMode mode) {
        this(pos, Optional.of(mode), Optional.empty());
    }

    public PrintingTableInputPacket(BlockPos pos, int experience) {
        this(pos, Optional.empty(), Optional.of(experience));
    }

    public void handle(IPayloadContext context) {
        Player player = context.player();
        if (!(player.level().getBlockEntity(pos) instanceof PrintingTableBlockEntity blockEntity)) return;
        mode.ifPresent(blockEntity::setMode);
        if (experience.isPresent() && (player.isCreative() || player.totalExperience >= experience.get())) {
            blockEntity.addExperience(experience.get());
            player.giveExperiencePoints(-experience.get());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
