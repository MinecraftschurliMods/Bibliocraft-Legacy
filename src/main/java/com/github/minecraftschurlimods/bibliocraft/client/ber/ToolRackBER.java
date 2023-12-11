package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ToolRackBER implements BlockEntityRenderer<ToolRackBlockEntity> {
    public ToolRackBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ToolRackBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(PotionShelfBlock.FACING)) {
            case SOUTH -> 0;
            case EAST -> 90;
            default -> 180;
            case WEST -> 270;
        }));
        stack.translate(-0.21875f, 0.21875f, -0.03125f); // -3.5 / 16, 3.5 / 16, -1 / 32
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                stack.pushPose();
                stack.translate(j * 0.4375f, -i * 0.4375f, 0); // j * 7 / 16, -i * 7 / 16
                stack.scale(0.4375f, 0.4375f, 0.4375f); // 7 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                ItemStack item = blockEntity.getItem(i * 2 + j);
                renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, blockEntity.getLevel(), null, 0));
                stack.popPose();
            }
        }
        stack.popPose();
    }
}
