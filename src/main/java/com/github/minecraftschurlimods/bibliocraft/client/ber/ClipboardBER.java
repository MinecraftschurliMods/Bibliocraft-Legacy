package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.CheckboxState;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SameParameterValue"})
public class ClipboardBER implements BlockEntityRenderer<ClipboardBlockEntity, ClipboardBER.State> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/clipboard_block.png");
    private static final Material CHECK_TEXTURE = new Material(Sheets.GUI_SHEET, BCUtil.bcLoc("check"));
    private static final Material X_TEXTURE = new Material(Sheets.GUI_SHEET, BCUtil.bcLoc("x"));
    private static final int CHECK_ICON_SIZE = 14;

    private final Font font;
    private final MaterialSet materials;

    public ClipboardBER(BlockEntityRendererProvider.Context context) {
        font = context.font();
        materials = context.materials();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        Vector3fc normal = state.blockState.getValue(BCFacingBlock.FACING).getUnitVec3f();
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        stack.translate(-0.25, -0.25, 0.4376);
        stack.translate(0, 0, -1 / 1024d);
        float scale = 1 / 256f;
        stack.scale(scale, scale, 1);
        stack.pushPose();
        blit(stack, collector, RenderType.entityCutout(ClipboardBER.BACKGROUND), 0, 128, 0, 148, 0, 0.0f, 0.5f, 0.0f, 0.578125f, normal, 0xffffffff, state.lightCoords, OverlayTexture.NO_OVERLAY);
        drawText(stack, collector, state.title, 29, 2, 72, state.lightCoords);
        stack.translate(2, -1, 0);
        for (State.Line line : state.lines) {
            stack.translate(0, 15, 0);
            switch (line.state()) {
                case CHECK:
                    blitSprite(stack, collector, normal, CHECK_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY);
                    break;
                case X:
                    blitSprite(stack, collector, normal, X_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY);
                    break;
            }
            drawText(stack, collector, line.text(), 15, 2, 109, state.lightCoords);
        }
        stack.popPose();
        stack.popPose();
    }

    @Override
    public void extractRenderState(ClipboardBlockEntity blockEntity, State state, float partialTick, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, p_445788_, p_446944_);
        ClipboardContent content = blockEntity.getContent();
        state.title = content.title();
        state.lines = new ArrayList<>();
        ClipboardContent.Page page = content.pages().get(content.active());
        for (int i = 0; i < ClipboardContent.MAX_LINES; i++) {
            state.lines.add(new State.Line(page.checkboxes().get(i), page.lines().get(i)));
        }
        state.lightCoords = LightTexture.FULL_BRIGHT;
    }

    private void drawText(PoseStack pose, OrderedSubmitNodeCollector collector, String text, float x, float y, int width, int light) {
        String visibleText = font.plainSubstrByWidth(text, width);
        if (visibleText.isEmpty()) return;
        collector.submitText(pose, x, y, FormattedCharSequence.forward(text, Style.EMPTY), false, Font.DisplayMode.POLYGON_OFFSET, light, 0xFF000000, 0, 0);
    }

    private void blitSprite(PoseStack pose, OrderedSubmitNodeCollector collector, Vector3fc normal, Material material, int light, int overlay) {
        TextureAtlasSprite sprite = materials.get(material);
        blit(pose, collector, material.renderType(RenderType::entityCutout), 0, ClipboardBER.CHECK_ICON_SIZE, 0, ClipboardBER.CHECK_ICON_SIZE, -0.001f, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), normal, 0, light, overlay);
    }

    private static void blit(PoseStack poseStack, OrderedSubmitNodeCollector collector, RenderType renderType, float x1, float x2, float y1, float y2, float z, float minU, float maxU, float minV, float maxV, Vector3fc normal, int color, int light, int overlay) {
        Vector3f normalVector = new Vector3f(normal);
        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> {
            consumer.addVertex(pose, x1, y1, z).setColor(color).setUv(minU, minV).setLight(light).setOverlay(overlay).setNormal(pose, normalVector);
            consumer.addVertex(pose, x1, y2, z).setColor(color).setUv(minU, maxV).setLight(light).setOverlay(overlay).setNormal(pose, normalVector);
            consumer.addVertex(pose, x2, y2, z).setColor(color).setUv(maxU, maxV).setLight(light).setOverlay(overlay).setNormal(pose, normalVector);
            consumer.addVertex(pose, x2, y1, z).setColor(color).setUv(maxU, minV).setLight(light).setOverlay(overlay).setNormal(pose, normalVector);
        });
    }

    public static class State extends BlockEntityRenderState {
        public @UnknownNullability List<Line> lines;
        public @UnknownNullability String title;
        
        public record Line(CheckboxState state, String text) {}
    }
}
