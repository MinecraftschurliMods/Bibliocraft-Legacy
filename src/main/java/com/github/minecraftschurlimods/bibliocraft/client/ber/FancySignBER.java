package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.client.screen.FancySignScreen;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.*;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FancySignBER implements BlockEntityRenderer<FancySignBlockEntity, FancySignBER.State> {

    public FancySignBER(BlockEntityRendererProvider.Context context) {}

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(FancySignBlockEntity blockEntity, State state, float partialTick, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, p_445788_, p_446944_);
        state.frontContent = blockEntity.getFrontContent();
        state.backContent = blockEntity.getBackContent();
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        boolean upsideDown = state.blockState.getValue(AbstractFancySignBlock.UPSIDE_DOWN);
        Block block = state.blockState.getBlock();
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);

        if (block instanceof FancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(state, false, stack, collector);
            stack.popPose();

            stack.pushPose();
            if (!upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(state, true, stack, collector);
            stack.popPose();
        } else if (block instanceof WallFancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, 0.40625);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(state, false, stack, collector);
            stack.popPose();
        }

        stack.popPose();
    }

    private static void renderLines(State state, boolean back, PoseStack stack, SubmitNodeCollector collector) {
        stack.pushPose();
        float scale = 1 / 160f;
        stack.scale(scale, scale, 0);
        stack.translate(0, 1, 0);
        var lines = back ? state.backContent.lines() : state.frontContent.lines();
        int y = 0;
        for (FormattedLine line : lines) {
            String text = line.text();
            Style style = line.style();
            int size = line.size();
            FormattedLine.Mode mode = line.mode();
            FormattedCharSequence formattedText = FormattedCharSequence.forward(text, style);
            // scale the text, 8 is the default font size, and we subtract a padding of 1 on each side
            float textWidth = (ClientUtil.getFont().width(formattedText) * ((size - 2) / 8f));
            float textX =  switch (line.alignment()) {
                case LEFT -> 1;
                case CENTER -> FancySignScreen.WIDTH / 2f - textWidth / 2;
                case RIGHT -> FancySignScreen.WIDTH - 1 - textWidth;
            };
            int color = style.getColor() == null ? 0 : style.getColor().getValue();
            int lightCoords = state.lightCoords;
            int outlineColor = mode == FormattedLine.Mode.GLOWING ? color == 0 ? 0xfff0ebcc : ARGB.scaleRGB(color, 0.4f) : color;
            collector.submitText(stack, textX, y, formattedText, mode == FormattedLine.Mode.SHADOW, Font.DisplayMode.POLYGON_OFFSET, lightCoords, color, 0, outlineColor);
            y += line.size();
        }
        stack.popPose();
    }

    @SuppressWarnings("DataFlowIssue")
    public static class State extends BlockEntityRenderState {
        public FancySignContent frontContent = null;
        public FancySignContent backContent = null;
    }
}
