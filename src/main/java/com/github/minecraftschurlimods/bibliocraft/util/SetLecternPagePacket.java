package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookContent;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.WrittenBigBookContent;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetLecternPagePacket(int page, Either<InteractionHand, BlockPos> target) implements CustomPacketPayload {
    public static final Type<SetLecternPagePacket> TYPE = new Type<>(BCUtil.modLoc("set_lectern_page"));
    public static final StreamCodec<ByteBuf, SetLecternPagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetLecternPagePacket::page,
            ByteBufCodecs.either(BCUtil.enumStreamCodec(InteractionHand::values, InteractionHand::ordinal), BlockPos.STREAM_CODEC), SetLecternPagePacket::target,
            SetLecternPagePacket::new);

    public static void handle(SetLecternPagePacket packet, IPayloadContext context) {
        Player player = context.player();
        packet.target.ifLeft(left -> updateStack(player.getItemInHand(left), packet.page));
        packet.target.ifRight(right -> {
            Level level = player.level();
            if (!level.getBlockState(right).hasProperty(LecternBlock.HAS_BOOK) || !level.getBlockState(right).getValue(LecternBlock.HAS_BOOK))
                return;
            if (!(level.getBlockEntity(right) instanceof LecternBlockEntity lectern)) return;
            updateStack(lectern.getBook(), packet.page);
            lectern.setPage(packet.page);
        });
    }

    private static void updateStack(ItemStack stack, int page) {
        if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
            stack.update(
                    BCDataComponents.BIG_BOOK_CONTENT,
                    BigBookContent.DEFAULT,
                    data -> new BigBookContent(data.pages(), Math.clamp(page, 0, Math.max(0, data.pages().size() - 1)))
            );
        }
        if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
            stack.update(
                    BCDataComponents.WRITTEN_BIG_BOOK_CONTENT,
                    WrittenBigBookContent.DEFAULT,
                    data -> new WrittenBigBookContent(data.pages(), data.title(), data.author(), data.generation(), Math.clamp(page, 0, Math.max(0, data.pages().size() - 1)))
            );
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
