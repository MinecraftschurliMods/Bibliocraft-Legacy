package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FancyCrafterBER implements BlockEntityRenderer<FancyCrafterBlockEntity, FancyCrafterBER.State> {
    private final ItemModelResolver itemModelResolver;

    public FancyCrafterBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(FancyCrafterBlockEntity blockEntity, State state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver);
        Level level = BCUtil.nonNull(blockEntity.getLevel());
        BlockPos pos = blockEntity.getBlockPos();
        Direction direction = blockEntity.getBlockState().getValue(FancyCrafterBlock.FACING);
        state.isSolidTop = level.getBlockState(pos.above()).isSolidRender();
        state.isSolidFront = level.getBlockState(pos.offset(direction.getUnitVec3i())).isSolidRender();
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        if (!state.isSolidTop) {
            stack.pushPose();
            stack.translate(-0.1875f, 0.5, -0.1875f);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
            stack.scale(0.1875f, 0.1875f, 0.1875f);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    ItemStackRenderState item = state.items[y * 3 + x];
                    if (item == null) {
                        continue;
                    }
                    stack.pushPose();
                    stack.translate(-x, -y, 0);
                    item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    stack.popPose();
                }
            }
            stack.popPose();
        }
        if (!state.isSolidFront) {
            stack.pushPose();
            stack.translate(-0.28125f, 0.03125, 0.25f);
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.scale(0.1875f, 0.1875f, 0.1875f);
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 4; x++) {
                    ItemStackRenderState item = state.items[y * 2 + x + 10];
                    if (item == null) {
                        continue;
                    }
                    stack.pushPose();
                    stack.translate(-x, -y, 0);
                    item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    stack.popPose();
                }
            }
            stack.popPose();
        }
        stack.popPose();
    }

    public static class State extends MultiItemContainerRenderState {
        private boolean isSolidTop;
        private boolean isSolidFront;
    }
}
