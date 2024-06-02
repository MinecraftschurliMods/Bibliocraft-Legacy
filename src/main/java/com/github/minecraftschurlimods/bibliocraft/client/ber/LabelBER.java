package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class LabelBER implements BlockEntityRenderer<LabelBlockEntity> {
    @Override
    public void render(LabelBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.mulPose(Axis.YP.rotationDegrees(180));
        stack.translate(0, 0, 0.4375);
        stack.pushPose();
        stack.translate(0, -0.3125, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        ClientUtil.renderFixedItem(blockEntity.getItem(0), stack, buffer, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.translate(0.1875, -0.1875, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        ClientUtil.renderFixedItem(blockEntity.getItem(1), stack, buffer, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.translate(-0.1875, -0.1875, 0);
        stack.scale(0.2f, 0.2f, 0.2f);
        ClientUtil.renderFixedItem(blockEntity.getItem(2), stack, buffer, light, overlay);
        stack.popPose();
        stack.popPose();
    }
}
