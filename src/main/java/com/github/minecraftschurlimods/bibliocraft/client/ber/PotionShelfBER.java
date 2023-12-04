package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PotionShelfBER implements BlockEntityRenderer<PotionShelfBlockEntity> {
    public PotionShelfBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PotionShelfBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(PotionShelfBlock.FACING)) {
            case SOUTH -> 0;
            case EAST -> 90;
            default -> 180;
            case WEST -> 270;
        }));
        stack.translate(-0.328125f, 0.34375f, -0.375); // -(7 - 3.5 / 2) / 16, 5.5 / 16, -3 / 8
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stack.pushPose();
                stack.translate(j * 0.21875, -i * 0.3125, 0); // j * 3.5 / 16, -i * 5 / 16
                stack.scale(0.21875f, 0.21875f, 0.21875f); // 3.5 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                ItemStack item = blockEntity.getItem(i * 4 + j);
                renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
                stack.popPose();
            }
        }
        stack.popPose();
    }
}