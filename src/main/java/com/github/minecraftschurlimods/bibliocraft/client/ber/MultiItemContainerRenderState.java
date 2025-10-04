package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MultiItemContainerRenderState extends BlockEntityRenderState {
    public @Nullable ItemStackRenderState[] items;
    
    public void fill(BCBlockEntity blockEntity, ItemDisplayContext displayContext, ItemModelResolver itemModelResolver) {
        items = new ItemStackRenderState[blockEntity.getContainerSize()];
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        for (int j = 0; j < items.length; j++) {
            ItemStack item = blockEntity.getItem(i);
            if (!item.isEmpty()) {
                continue;
            }
            ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemstackrenderstate, item, displayContext, blockEntity.level(), blockEntity, i + j);
            items[j] = itemstackrenderstate;
        }
    }
}
