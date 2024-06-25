package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class ShelfBER implements BlockEntityRenderer<ShelfBlockEntity> {
    @Override
    public void render(ShelfBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.translate(-0.28125f, 0.28125f, -0.25f); // 0.5 - 3.5 / 16, 0.5 - 3.5 / 16, -4 / 16
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                stack.pushPose();
                stack.translate(j * 0.5625f, -i * 0.5f, 0); // j * 9 / 16, -i * 8 / 16
                stack.scale(0.4375f, 0.4375f, 0.4375f); // 7 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                ClientUtil.renderFixedItem(blockEntity.getItem(i * 2 + j), stack, buffer, light, overlay);
                stack.popPose();
            }
        }
        stack.popPose();
    }
}
