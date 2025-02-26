package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class ClipboardBlockEntity extends BlockEntity {
    private static final String CONTENT_KEY = "clipboard_content";
    private ClipboardContent content = ClipboardContent.DEFAULT;

    public ClipboardBlockEntity(BlockPos pos, BlockState blockState) {
        super(BCBlockEntities.CLIPBOARD.get(), pos, blockState);
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

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        setContent(componentInput.getOrDefault(BCDataComponents.CLIPBOARD_CONTENT, ClipboardContent.DEFAULT));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (!content.equals(ClipboardContent.DEFAULT)) {
            components.set(BCDataComponents.CLIPBOARD_CONTENT, content);
        }
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(CONTENT_KEY);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (!content.equals(ClipboardContent.DEFAULT)) {
            tag.put(CONTENT_KEY, BCUtil.encodeNbt(ClipboardContent.CODEC, getContent()));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        if (tag.contains(CONTENT_KEY)) {
            setContent(BCUtil.decodeNbt(ClipboardContent.CODEC, tag.get(CONTENT_KEY)));
        }
    }

    public ClipboardContent getContent() {
        return content;
    }

    public void setContent(ClipboardContent content) {
        this.content = content;
        setChanged();
    }
}
