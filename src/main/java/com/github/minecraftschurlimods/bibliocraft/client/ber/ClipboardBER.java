package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.client.ClipboardReadOnlyRenderer;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;

public class ClipboardBER implements BlockEntityRenderer<ClipboardBlockEntity, ClipboardBER.State> {
    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        stack.translate(-0.25, -0.25, 0.4375);
        stack.translate(0, 0, -1 / 1024d);
        float scale = 1 / 256f;
        stack.scale(scale, scale, 0);
        ClipboardReadOnlyRenderer.render(stack, collector, state.content, 128, 148);
        stack.popPose();
    }

    public static class State extends BlockEntityRenderState {
        public ClipboardContent content;
    }
}
