package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LabelBER implements BlockEntityRenderer<LabelBlockEntity> {
    @SuppressWarnings("unused")
    public LabelBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LabelBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ItemStack item = blockEntity.getItem(0);
        renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
        stack.popPose();
    }
}
