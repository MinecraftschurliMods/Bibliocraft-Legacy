package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.WallDisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;

public class DisplayCaseBER implements BlockEntityRenderer<DisplayCaseBlockEntity> {
    @Override
    public void render(DisplayCaseBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        Block block = blockEntity.getBlockState().getBlock();
        if (block instanceof WallDisplayCaseBlock) {
            stack.mulPose(Axis.YP.rotationDegrees(180));
        } else if (block instanceof DisplayCaseBlock) {
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
        }
        stack.translate(0, 0, 0.25f);
        stack.scale(0.5f, 0.5f, 0.5f);
        ClientUtil.renderFixedItem(blockEntity.getItem(0), stack, buffer, light, overlay);
        stack.popPose();
    }
}
