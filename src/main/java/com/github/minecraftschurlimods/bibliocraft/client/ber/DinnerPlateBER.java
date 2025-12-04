package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DinnerPlateBER implements BlockEntityRenderer<DinnerPlateBlockEntity, SingleItemContainerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public DinnerPlateBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public SingleItemContainerRenderState createRenderState() {
        return new SingleItemContainerRenderState();
    }

    @Override
    public void extractRenderState(DinnerPlateBlockEntity blockEntity, SingleItemContainerRenderState state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver);
    }

    @Override
    public void submit(SingleItemContainerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState p_451022_) {
        if (state.itemStackState == null) {
            return;
        }
        stack.pushPose();
        stack.translate(0.5, 0.0625, 0.5);
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.scale(0.4375f, 0.4375f, 0.4375f);
        state.itemStackState.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        stack.popPose();
    }
}
