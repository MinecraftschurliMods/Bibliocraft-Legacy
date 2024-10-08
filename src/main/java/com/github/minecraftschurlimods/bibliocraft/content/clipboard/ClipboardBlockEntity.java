package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClipboardBlockEntity extends BlockEntity {
    private static final String CONTENT_KEY = "clipboard_content";
    private ClipboardContent content = ClipboardContent.DEFAULT;

    public ClipboardBlockEntity(BlockPos pos, BlockState blockState) {
        super(BCBlockEntities.CLIPBOARD.get(), pos, blockState);
    }

    @Override
    public void onLoad() { //fixme
        if (!level.isClientSide()) {
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new ClipboardBESyncPacket(getContent(), getBlockPos()));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(CONTENT_KEY)) {
            setContent(BCUtil.decodeNbt(ClipboardContent.CODEC, tag.get(CONTENT_KEY)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(CONTENT_KEY, BCUtil.encodeNbt(ClipboardContent.CODEC, getContent()));
    }

    public ClipboardContent getContent() {
        return content;
    }

    public void setContent(ClipboardContent content) {
        this.content = content;
        if (level != null && !level.isClientSide()) {
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new ClipboardBESyncPacket(content, getBlockPos()));
        }
        setChanged();
    }
}
