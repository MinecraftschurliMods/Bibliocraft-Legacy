package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;

import java.util.Objects;

/**
 * Conceptual credit for the clock hands:
 * <a href="https://github.com/MehVahdJukaar/Supplementaries/blob/master/common/src/main/java/net/mehvahdjukaar/supplementaries/client/renderers/tiles/ClockBlockTileRenderer.java">ClockBlockTileRenderer from the Supplementaries mod</a>
 */
public class FancyClockBER implements BlockEntityRenderer<FancyClockBlockEntity> {
    public static final Material CLOCK_HAND_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BCUtil.modLoc("block/clock_hand"));
    public static final ModelLayerLocation CLOCK_HAND_LOCATION = new ModelLayerLocation(BCUtil.modLoc("block/clock_hand"), "main");
    private final ModelPart hourHand;
    private final ModelPart minuteHand;

    public FancyClockBER(BlockEntityRendererProvider.Context context) {
        ModelPart model = context.bakeLayer(CLOCK_HAND_LOCATION);
        hourHand = model.getChild("hour");
        minuteHand = model.getChild("minute");
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hour", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 1.5f, 0.5f), PartPose.ZERO);
        root.addOrReplaceChild("minute", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 2f, 0.5f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void render(FancyClockBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer clockHand = CLOCK_HAND_MATERIAL.buffer(buffer, RenderType::entityCutout);
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        stack.translate(0, 0.15625, 0.15625);
        renderHands(blockEntity, partialTick, hourHand, minuteHand, stack, clockHand, light, overlay);
        stack.popPose();
    }

    private void renderHands(FancyClockBlockEntity blockEntity, float partialTick, ModelPart hourHand, ModelPart minuteHand, PoseStack stack, VertexConsumer vc, int light, int overlay) {
        Level level = Objects.requireNonNull(blockEntity.getLevel());
        float rotation = level.getSunAngle(1) * 2;
        stack.pushPose();
        stack.mulPose(Axis.ZN.rotation(rotation));
        hourHand.render(stack, vc, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.mulPose(Axis.ZN.rotation(rotation * 12));
        minuteHand.render(stack, vc, light, overlay);
        stack.popPose();
    }
}
