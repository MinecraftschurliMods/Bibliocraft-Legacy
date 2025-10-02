package com.github.minecraftschurlimods.bibliocraft.client.ber;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FancySignBER implements BlockEntityRenderer<FancySignBlockEntity, FancySignBER.State> {
    private final Font font;

    public FancySignBER(BlockEntityRendererProvider.Context context) {
        font = context.font();
    }

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
            submitLines(state, false, stack, collector);
            stack.popPose();

            stack.pushPose();
            if (!upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            submitLines(state, true, stack, collector);
            stack.popPose();
        } else if (block instanceof WallFancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, 0.40625);
            stack.translate(0, 0, -1 / 1024d);
            submitLines(state, false, stack, collector);
            stack.popPose();
        }

        stack.popPose();
    }

    private void submitLines(State state, boolean back, PoseStack stack, SubmitNodeCollector collector) {
        stack.pushPose();
        float scale = 1 / 160f;
        stack.scale(scale, scale, 0);
        stack.translate(0, 1, 0);
        var lines = back ? state.backContent.lines() : state.frontContent.lines();
        float y = 0;
        int width = 140;
        int lightCoords = state.lightCoords;
        for (FormattedLine line : lines) {
            String text = line.text();
            Style style = line.style();
            // scale the text, 8 is the default font size, and we subtract a padding of 1 on each side
            float size = (line.size() - 2) / 8f;
            FormattedLine.Mode mode = line.mode();
            FormattedCharSequence formattedText = FormattedCharSequence.forward(text, style);
            int lineWidth = font.width(formattedText);
            float textX =  switch (line.alignment()) {
                case LEFT -> 1;
                case CENTER -> width / 2f - (lineWidth * size) / 2;
                case RIGHT -> width - 1 - (lineWidth * size);
            };
            int color = (style.getColor() == null ? 0 : style.getColor().getValue());
            int outlineColor = mode == FormattedLine.Mode.GLOWING ? 0xFF000000 | (color == 0 ? 0xf0ebcc : ARGB.scaleRGB(color, 0.4f)) : 0;
            boolean renderWithShadow = mode == FormattedLine.Mode.SHADOW;
            submitText(stack, collector, textX, y, size, formattedText, renderWithShadow, lightCoords, color, outlineColor);
            y += line.size();
        }
        stack.popPose();
    }

    private void submitText(PoseStack stack, SubmitNodeCollector collector, float x, float y, float size, FormattedCharSequence text, boolean renderWithShadow, int lightCoords, int color, int outlineColor) {
        stack.pushPose();
        stack.translate(x, y, 0);
        stack.scale(size, size, 1);
        collector.submitText(stack, 0, 0, text, renderWithShadow, Font.DisplayMode.POLYGON_OFFSET, lightCoords, 0xFF000000 | color, 0, outlineColor);
        stack.popPose();
    }

    @SuppressWarnings("DataFlowIssue")
    public static class State extends BlockEntityRenderState {
        public FancySignContent frontContent = null;
        public FancySignContent backContent = null;
    }
}
