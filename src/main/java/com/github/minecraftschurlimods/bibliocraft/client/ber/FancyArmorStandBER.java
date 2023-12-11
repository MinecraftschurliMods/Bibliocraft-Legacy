package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

public class FancyArmorStandBER implements BlockEntityRenderer<FancyArmorStandBlockEntity> {
    public FancyArmorStandBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FancyArmorStandBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        FancyArmorStandEntity entity = blockEntity.getDisplayEntity();
        if (entity != null) {
            stack.pushPose();
            stack.translate(0.5, 0, 0.5);
            stack.mulPose(Axis.YP.rotationDegrees(entity.getYHeadRot()));
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, partialTick, stack, buffer, light);
            stack.popPose();
        }
    }
}
