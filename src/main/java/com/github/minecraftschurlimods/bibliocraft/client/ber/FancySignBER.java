package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.AbstractFancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.WallFancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FancySignBER implements BlockEntityRenderer<FancySignBlockEntity> {
    @Override
    public void render(FancySignBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        boolean upsideDown = blockEntity.getBlockState().getValue(AbstractFancySignBlock.UPSIDE_DOWN);
        Block block = blockEntity.getBlockState().getBlock();
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);

        if (block instanceof FancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(blockEntity.getFrontContent().lines(), stack, buffer);
            stack.popPose();

            stack.pushPose();
            if (!upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(blockEntity.getBackContent().lines(), stack, buffer);
            stack.popPose();
        } else if (block instanceof WallFancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, 0.40625);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(blockEntity.getFrontContent().lines(), stack, buffer);
            stack.popPose();
        }

        stack.popPose();
    }

    private static void renderLines(List<FormattedLine> lines, PoseStack stack, MultiBufferSource bufferSource) {
        stack.pushPose();
        float scale = 1 / 160f;
        stack.scale(scale, scale, 0);
        stack.translate(0, 1, 0);
        int y = 0;
        for (FormattedLine line : lines) {
            FormattedTextArea.renderLine(line, stack, bufferSource, 0, y, FormattedTextArea.WIDTH, FormattedTextArea.HEIGHT);
            y += line.size();
        }
        stack.popPose();
    }
}
