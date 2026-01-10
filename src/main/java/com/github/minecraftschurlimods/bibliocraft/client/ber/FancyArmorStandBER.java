package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FancyArmorStandBER implements BlockEntityRenderer<FancyArmorStandBlockEntity, FancyArmorStandBER.State> {
    private final EntityRenderDispatcher entityRenderDispatcher;

    public FancyArmorStandBER(BlockEntityRendererProvider.Context context) {
        this.entityRenderDispatcher = context.entityRenderer();
    }

    @Override
    public AABB getRenderBoundingBox(FancyArmorStandBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity).expandTowards(0, 1, 0);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(FancyArmorStandBlockEntity blockEntity, State state, float partialTicks, Vec3 p_445788_, ModelFeatureRenderer.@Nullable CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        FancyArmorStandEntity entity = blockEntity.getDisplayEntity();
        if (entity == null) {
            return;
        }
        state.entity = entityRenderDispatcher.extractEntity(entity, 0);
        state.entity.lightCoords = LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.entity == null) {
            return;
        }
        stack.pushPose();
        stack.translate(0.5, 0.0625, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        entityRenderDispatcher.submit(state.entity, camera, 0, 0, 0, stack, collector);
        stack.popPose();
    }

    public static class State extends BlockEntityRenderState {
        public @Nullable EntityRenderState entity;
        public float rotation;
    }
}
