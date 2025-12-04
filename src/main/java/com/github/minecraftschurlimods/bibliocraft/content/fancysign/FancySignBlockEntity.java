package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FancySignBlockEntity extends BlockEntity {
    private static final String FRONT_CONTENT_KEY = "front_content";
    private static final String BACK_CONTENT_KEY = "back_content";
    private FancySignContent frontContent = FancySignContent.withSize(16);
    private FancySignContent backContent = FancySignContent.withSize(16);

    public FancySignBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_SIGN.get(), pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read(FRONT_CONTENT_KEY, FancySignContent.CODEC).ifPresent(this::setFrontContent);
        input.read(BACK_CONTENT_KEY, FancySignContent.CODEC).ifPresent(this::setBackContent);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(FRONT_CONTENT_KEY, FancySignContent.CODEC, getFrontContent());
        output.store(BACK_CONTENT_KEY, FancySignContent.CODEC, getBackContent());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    public FancySignContent getFrontContent() {
        return frontContent;
    }

    public void setFrontContent(FancySignContent frontContent) {
        this.frontContent = frontContent;
        setChanged();
    }

    public FancySignContent getBackContent() {
        return backContent;
    }

    public void setBackContent(FancySignContent backContent) {
        this.backContent = backContent;
        setChanged();
    }
}
