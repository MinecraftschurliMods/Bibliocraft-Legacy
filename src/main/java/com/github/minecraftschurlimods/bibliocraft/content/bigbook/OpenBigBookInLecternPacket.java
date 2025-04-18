package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenBigBookInLecternPacket(BlockPos pos, ItemStack stack) implements CustomPacketPayload {
    public static final Type<OpenBigBookInLecternPacket> TYPE = new Type<>(BCUtil.modLoc("open_big_book_in_lectern"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenBigBookInLecternPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenBigBookInLecternPacket::pos,
            ItemStack.STREAM_CODEC, OpenBigBookInLecternPacket::stack,
            OpenBigBookInLecternPacket::new);

    public static void handle(OpenBigBookInLecternPacket packet, IPayloadContext context) {
        Player player = context.player();
        Level level = player.level();
        if (!(level.getBlockEntity(packet.pos) instanceof LecternBlockEntity lectern)) return;
        if (lectern.getBook().isEmpty()) {
            lectern.setBook(packet.stack);
        }
        ClientUtil.openBigBookScreen(packet.stack, player, packet.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
