package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ShelfBER implements BlockEntityRenderer<ShelfBlockEntity, ShelfBER.State> {
    private final ItemModelResolver itemModelResolver;

    public ShelfBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ShelfBlockEntity blockEntity, State state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, breakProgress);
        NonNullList<ItemStack> nonnulllist = blockEntity.getItems();
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());

        for (int j = 0; j < nonnulllist.size(); j++) {
            ItemStack itemstack = nonnulllist.get(j);
            if (!itemstack.isEmpty()) {
                ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
                this.itemModelResolver.updateForTopItem(itemstackrenderstate, itemstack, ItemDisplayContext.ON_SHELF, blockEntity.level(), blockEntity, i + j);
                state.items[j] = itemstackrenderstate;
            }
        }
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
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

    public static class State extends BlockEntityRenderState {
        public ItemStackRenderState[] items = new ItemStackRenderState[4];
    }
}
