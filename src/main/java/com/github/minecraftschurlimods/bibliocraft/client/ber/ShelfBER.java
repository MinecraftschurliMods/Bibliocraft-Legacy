package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
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
import org.jspecify.annotations.Nullable;

public class ShelfBER implements BlockEntityRenderer<ShelfBlockEntity, MultiItemContainerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ShelfBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MultiItemContainerRenderState createRenderState() {
        return new MultiItemContainerRenderState();
    }

    @Override
    public void extractRenderState(ShelfBlockEntity blockEntity, MultiItemContainerRenderState state, float partialTicks, Vec3 p_445788_, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, breakProgress);
        state.fill(blockEntity, ItemDisplayContext.ON_SHELF, itemModelResolver);
    }

    @Override
    public void submit(MultiItemContainerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.translate(-0.28125f, 0.28125f, -0.25f); // 0.5 - 3.5 / 16, 0.5 - 3.5 / 16, -4 / 16
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (state.items[i * 2 + j] == null) {
                    continue;
                }
                stack.pushPose();
                stack.translate(j * 0.5625f, -i * 0.5f, 0); // j * 9 / 16, -i * 8 / 16
                stack.scale(0.4375f, 0.4375f, 0.4375f); // 7 / 16
                stack.mulPose(Axis.YP.rotationDegrees(180));
                state.items[i * 2 + j].submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                stack.popPose();
            }
        }
        stack.popPose();
    }
}
