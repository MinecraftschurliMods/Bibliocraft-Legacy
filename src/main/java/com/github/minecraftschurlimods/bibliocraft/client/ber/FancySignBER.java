package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.client.screen.FancySignScreen;
import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.*;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FancySignBER implements BlockEntityRenderer<FancySignBlockEntity, FancySignBER.State> {

    private static void renderLines(List<FormattedLine> lines, PoseStack stack, SubmitNodeCollector collector) {
        stack.pushPose();
        float scale = 1 / 160f;
        stack.scale(scale, scale, 0);
        stack.translate(0, 1, 0);
        // FIXME: big rendering changes go brrr
        FormattedTextArea.renderLines(lines, stack, collector, 0, 0, FancySignScreen.WIDTH);
        stack.popPose();
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
            renderLines(state.frontContent.lines(), stack, collector);
            stack.popPose();

            stack.pushPose();
            if (!upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, -0.03125);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(state.backContent.lines(), stack, collector);
            stack.popPose();
        } else if (block instanceof WallFancySignBlock) {
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(180));
            if (upsideDown) {
                stack.mulPose(Axis.ZP.rotationDegrees(180));
            }
            stack.translate(-0.4375, -0.25, 0.40625);
            stack.translate(0, 0, -1 / 1024d);
            renderLines(state.frontContent.lines(), stack, collector);
            stack.popPose();
        }

        stack.popPose();
    }

    @SuppressWarnings("DataFlowIssue")
    public static class State extends BlockEntityRenderState {
        public FancySignContent frontContent = null;
        public FancySignContent backContent = null;
    }
}
