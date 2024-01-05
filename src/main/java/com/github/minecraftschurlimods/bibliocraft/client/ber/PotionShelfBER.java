package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class PotionShelfBER implements BlockEntityRenderer<PotionShelfBlockEntity> {
    @SuppressWarnings("unused")
    public PotionShelfBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PotionShelfBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.translate(-0.328125f, 0.34375f, -0.375); // -(7 - 3.5 / 2) / 16, 5.5 / 16, -3 / 8
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stack.pushPose();
                stack.translate(j * 0.21875, -i * 0.3125, 0); // j * 3.5 / 16, -i * 5 / 16
                stack.scale(0.21875f, 0.21875f, 0.21875f); // 3.5 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                ClientUtil.renderFixedItem(blockEntity.getItem(i * 4 + j), stack, buffer, light, overlay);
                stack.popPose();
            }
        }
        stack.popPose();
    }
}
