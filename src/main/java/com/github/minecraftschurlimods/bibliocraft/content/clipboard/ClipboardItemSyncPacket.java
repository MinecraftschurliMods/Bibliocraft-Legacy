package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCAttachments;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Objects;

public record ClipboardItemSyncPacket(CompoundTag tag) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(BibliocraftApi.MOD_ID, "clipboard_item_sync");

    public ClipboardItemSyncPacket(FriendlyByteBuf buf) {
        this(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            Player player = context.player().orElseThrow();
            ItemStack stack = player.getMainHandItem();
            if (!stack.is(BCItems.CLIPBOARD)) {
                stack = player.getOffhandItem();
                if (!stack.is(BCItems.CLIPBOARD)) return;
            }
            ClipboardAttachment attachment = new ClipboardAttachment();
            attachment.deserializeNBT(tag);
            stack.setData(BCAttachments.CLIPBOARD, attachment);
        });
    }
}
