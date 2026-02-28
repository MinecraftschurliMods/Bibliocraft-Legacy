package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PotionShelfBER implements BlockEntityRenderer<PotionShelfBlockEntity, MultiItemContainerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public PotionShelfBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MultiItemContainerRenderState createRenderState() {
        return new MultiItemContainerRenderState();
    }

    @Override
    public void extractRenderState(PotionShelfBlockEntity blockEntity, MultiItemContainerRenderState state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver);
    }

    @Override
    public void submit(MultiItemContainerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.translate(-0.328125f, 0.34375f, -0.375); // -(7 - 3.5 / 2) / 16, 5.5 / 16, -3 / 8
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                ItemStackRenderState item = state.items[i * 4 + j];
                if (item == null) {
                    continue;
                }
                stack.pushPose();
                stack.translate(j * 0.21875, -i * 0.3125, 0); // j * 3.5 / 16, -i * 5 / 16
                stack.scale(0.21875f, 0.21875f, 0.21875f); // 3.5 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                stack.popPose();
            }
        }
        stack.popPose();
    }
}
