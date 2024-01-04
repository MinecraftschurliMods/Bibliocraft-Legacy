package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
        stack.mulPose(Axis.YP.rotationDegrees(180));
        stack.translate(0, 0, 0.4375);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        stack.pushPose();
        stack.translate(0, -0.3125, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        ItemStack item = blockEntity.getItem(0);
        renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
        stack.popPose();
        stack.pushPose();
        stack.translate(0.1875, -0.1875, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        item = blockEntity.getItem(1);
        renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
        stack.popPose();
        stack.pushPose();
        stack.translate(-0.1875, -0.1875, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        item = blockEntity.getItem(2);
        renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
        stack.popPose();
        stack.popPose();
    }
}
