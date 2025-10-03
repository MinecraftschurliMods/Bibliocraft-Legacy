package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DinnerPlateBER implements BlockEntityRenderer<DinnerPlateBlockEntity, DinnerPlateBER.State> {
    private final ItemModelResolver itemModelResolver;

    public DinnerPlateBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(DinnerPlateBlockEntity blockEntity, State state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        ItemStack itemstack = blockEntity.getItem(0);
        if (itemstack.isEmpty()) {
            state.itemStackState = null;
            return;
        }
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(itemstackrenderstate, itemstack, ItemDisplayContext.FIXED, blockEntity.level(), blockEntity, i);
        state.itemStackState = itemstackrenderstate;
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState p_451022_) {
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

    public static class State extends BlockEntityRenderState {
        private @Nullable ItemStackRenderState itemStackState;
    }
}
