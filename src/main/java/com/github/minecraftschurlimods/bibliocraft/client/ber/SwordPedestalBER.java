package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SwordPedestalBER implements BlockEntityRenderer<SwordPedestalBlockEntity> {
    @SuppressWarnings("unused")
    public SwordPedestalBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SwordPedestalBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.mulPose(Axis.YP.rotationDegrees(180));
        stack.translate(0, 1 / 16d, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(135));
        stack.scale(0.6f, 0.6f, 0.6f);
        ClientUtil.renderFixedItem(blockEntity.getItem(0), stack, buffer, light, overlay);
        stack.popPose();
    }
}
