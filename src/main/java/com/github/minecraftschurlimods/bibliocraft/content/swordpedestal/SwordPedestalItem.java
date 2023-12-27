package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({"unused", "DataFlowIssue"})
public class SwordPedestalItem extends BlockItem implements DyeableLeatherItem {
    public SwordPedestalItem() {
        super(BCBlocks.SWORD_PEDESTAL.get(), new Properties());
    }

    //Static variants of the methods in DyeableLeatherItem.
    public static boolean hasNBTColor(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return false;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        return tag != null && tag.contains(TAG_COLOR, Tag.TAG_ANY_NUMERIC);
    }

    public static int getNBTColor(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return 0xa06540;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        return tag != null && tag.contains(TAG_COLOR, Tag.TAG_ANY_NUMERIC) ? tag.getInt(TAG_COLOR) : 0xa06540;
    }

    public static void clearNBTColor(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_DISPLAY)) return;
        CompoundTag tag = stack.getTagElement(TAG_DISPLAY);
        if (tag != null && tag.contains(TAG_COLOR)) {
            tag.remove(TAG_COLOR);
        }
    }

    public static void setNBTColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, color);
    }
}
