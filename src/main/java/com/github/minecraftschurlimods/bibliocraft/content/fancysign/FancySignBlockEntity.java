package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FancySignBlockEntity extends BlockEntity {
    private static final String FRONT_CONTENT_KEY = "front_content";
    private static final String BACK_CONTENT_KEY = "back_content";
    private FormattedLineList frontContent = FormattedLineList.withSize(16);
    private FormattedLineList backContent = FormattedLineList.withSize(16);

    public FancySignBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_SIGN.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(FRONT_CONTENT_KEY)) {
            setFrontContent(BCUtil.decodeNbt(FormattedLineList.CODEC, tag.get(FRONT_CONTENT_KEY)));
        }
        if (tag.contains(BACK_CONTENT_KEY)) {
            setBackContent(BCUtil.decodeNbt(FormattedLineList.CODEC, tag.get(BACK_CONTENT_KEY)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(FRONT_CONTENT_KEY, BCUtil.encodeNbt(FormattedLineList.CODEC, getFrontContent()));
        tag.put(BACK_CONTENT_KEY, BCUtil.encodeNbt(FormattedLineList.CODEC, getBackContent()));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (!getFrontContent().lines().isEmpty()) {
            tag.put(FRONT_CONTENT_KEY, BCUtil.encodeNbt(FormattedLineList.CODEC, getFrontContent()));
        }
        if (!getBackContent().lines().isEmpty()) {
            tag.put(BACK_CONTENT_KEY, BCUtil.encodeNbt(FormattedLineList.CODEC, getBackContent()));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        if (tag.contains(FRONT_CONTENT_KEY)) {
            setFrontContent(BCUtil.decodeNbt(FormattedLineList.CODEC, tag.get(FRONT_CONTENT_KEY)));
        }
        if (tag.contains(BACK_CONTENT_KEY)) {
            setBackContent(BCUtil.decodeNbt(FormattedLineList.CODEC, tag.get(BACK_CONTENT_KEY)));
        }
    }

    public FormattedLineList getFrontContent() {
        return frontContent;
    }

    public void setFrontContent(FormattedLineList frontContent) {
        this.frontContent = frontContent;
        setChanged();
    }

    public FormattedLineList getBackContent() {
        return backContent;
    }

    public void setBackContent(FormattedLineList backContent) {
        this.backContent = backContent;
        setChanged();
    }
}
