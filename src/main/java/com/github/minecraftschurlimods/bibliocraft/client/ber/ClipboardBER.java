package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.client.ClipboardReadOnlyRenderer;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class ClipboardBER implements BlockEntityRenderer<ClipboardBlockEntity> {
    @Override
    public void render(ClipboardBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        RenderSystem.enableDepthTest();
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        stack.translate(-0.25, -0.25, 0.4375);
        stack.translate(0, 0, -0.00001);
        float scale = 1 / 256f;
        stack.scale(scale, scale, 0);
        ClipboardReadOnlyRenderer.render(stack, buffer, blockEntity.getContent(), 128, 148);
        stack.popPose();
        RenderSystem.disableDepthTest();
    }
}
