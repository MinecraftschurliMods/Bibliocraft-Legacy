package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.state.BlockState;

public class SwordPedestalBlockEntity extends BCBlockEntity {
    private static final String COLOR_KEY = "color";
    private DyedItemColor color = SwordPedestalBlock.DEFAULT_COLOR;
    
    public SwordPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SWORD_PEDESTAL.get(), 1, pos, state);
    }

    public DyedItemColor getColor() {
        return color;
    }

    public void setColor(DyedItemColor color) {
        this.color = color;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.SWORD_PEDESTAL_SWORDS);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(COLOR_KEY)) {
            setColor(BCUtil.decodeNbt(DyedItemColor.CODEC, tag.get(COLOR_KEY)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(COLOR_KEY, BCUtil.encodeNbt(DyedItemColor.CODEC, getColor()));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        setColor(componentInput.getOrDefault(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR));
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (!color.equals(SwordPedestalBlock.DEFAULT_COLOR)) {
            components.set(DataComponents.DYED_COLOR, color);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(COLOR_KEY);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        if (!color.equals(SwordPedestalBlock.DEFAULT_COLOR)) {
            tag.put(COLOR_KEY, BCUtil.encodeNbt(DyedItemColor.CODEC, getColor()));
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        if (tag.contains(COLOR_KEY)) {
            setColor(BCUtil.decodeNbt(DyedItemColor.CODEC, tag.get(COLOR_KEY)));
        }
    }
}
