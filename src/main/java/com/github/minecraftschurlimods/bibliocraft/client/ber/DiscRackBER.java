package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.WallDiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DiscRackBER implements BlockEntityRenderer<DiscRackBlockEntity, MultiItemContainerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public DiscRackBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MultiItemContainerRenderState createRenderState() {
        return new MultiItemContainerRenderState();
    }

    @Override
    public void extractRenderState(DiscRackBlockEntity blockEntity, MultiItemContainerRenderState state, float partialTicks, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, p_445788_, p_446944_);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver);
    }

    @Override
    public void submit(MultiItemContainerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        if (state.blockState.getBlock() instanceof WallDiscRackBlock) {
            stack.translate(0.5, 0.5, 0.5);
            Direction facing = state.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            stack.mulPose(Axis.YP.rotationDegrees(facing.getAxis() == Direction.Axis.X ? 90 : 0));
            if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                stack.mulPose(Axis.YP.rotationDegrees(180));
            }
            stack.mulPose(Axis.XP.rotationDegrees(90));
        } else {
            ClientUtil.setupCenteredBER(stack, state);
        }
        for (int i = 0; i < state.items.length; i++) {
            ItemStackRenderState item = state.items[i];
            if (item == null) {
                continue;
            }
            stack.pushPose();
            stack.translate(0, -0.25f, (i - 4) / 16f);
            stack.scale(0.5f, 0.5f, 0.5f);
            item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
        stack.popPose();
    }
}
