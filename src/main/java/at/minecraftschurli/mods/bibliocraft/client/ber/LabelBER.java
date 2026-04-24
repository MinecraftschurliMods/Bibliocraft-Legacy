package at.minecraftschurli.mods.bibliocraft.client.ber;

import at.minecraftschurli.mods.bibliocraft.content.label.LabelBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class LabelBER implements BlockEntityRenderer<LabelBlockEntity, MultiItemContainerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public LabelBER(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MultiItemContainerRenderState createRenderState() {
        return new MultiItemContainerRenderState();
    }

    @Override
    public void extractRenderState(LabelBlockEntity blockEntity, MultiItemContainerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver);
    }

    @Override
    public void submit(MultiItemContainerRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.mulPose(Axis.YP.rotationDegrees(180));
        stack.translate(0, 0, 0.4375);
        if (state.items[0] != null) {
            stack.pushPose();
            stack.translate(0, -0.3125, 0);
            stack.scale(0.2f, 0.2f, 0.2f);
            state.items[0].submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
        if (state.items[1] != null) {
            stack.pushPose();
            stack.translate(0.1875, -0.1875, 0);
            stack.scale(0.2f, 0.2f, 0.2f);
            state.items[1].submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
        if (state.items[2] != null) {
            stack.pushPose();
            stack.translate(-0.1875, -0.1875, 0);
            stack.scale(0.2f, 0.2f, 0.2f);
            state.items[2].submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
        stack.popPose();
    }
}
