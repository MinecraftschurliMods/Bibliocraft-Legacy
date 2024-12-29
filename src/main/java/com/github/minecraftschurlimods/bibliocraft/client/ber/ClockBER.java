package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.GrandfatherClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.WallFancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

import java.util.Objects;

/**
 * Conceptual credit for the clock hands:
 * <a href="https://github.com/MehVahdJukaar/Supplementaries/blob/master/common/src/main/java/net/mehvahdjukaar/supplementaries/client/renderers/tiles/ClockBlockTileRenderer.java">ClockBlockTileRenderer from the Supplementaries mod</a>
 */
public class ClockBER implements BlockEntityRenderer<ClockBlockEntity> {
    public static final Material HAND_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BCUtil.modLoc("block/clock_hand"));
    public static final Material PENDULUM_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, BCUtil.mcLoc("block/copper_block"));
    public static final ModelLayerLocation LOCATION = new ModelLayerLocation(BCUtil.modLoc("clock"), "main");
    private final ModelPart hourHand;
    private final ModelPart minuteHand;
    private final ModelPart pendulum;
    private final ModelPart grandfatherHourHand;
    private final ModelPart grandfatherMinuteHand;
    private final ModelPart grandfatherPendulum;

    public ClockBER(BlockEntityRendererProvider.Context context) {
        ModelPart model = context.bakeLayer(LOCATION);
        hourHand = model.getChild("hour");
        minuteHand = model.getChild("minute");
        pendulum = model.getChild("pendulum");
        grandfatherHourHand = model.getChild("grandfather_hour");
        grandfatherMinuteHand = model.getChild("grandfather_minute");
        grandfatherPendulum = model.getChild("grandfather_pendulum");
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("hour", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 1.5f, 0.5f), PartPose.ZERO);
        root.addOrReplaceChild("minute", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 2, 0.5f), PartPose.ZERO);
        PartDefinition pendulum = root.addOrReplaceChild("pendulum", CubeListBuilder.create().addBox(-0.25f, 0, -0.25f, 0.5f, 2, 0.5f), PartPose.ZERO);
        pendulum.addOrReplaceChild("head", CubeListBuilder.create().texOffs(7, 7).addBox(-1, 2, -0.5f, 2, 2, 1), PartPose.ZERO);
        root.addOrReplaceChild("grandfather_hour", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 2, 0.5f), PartPose.ZERO);
        root.addOrReplaceChild("grandfather_minute", CubeListBuilder.create().addBox(-0.25f, -0.25f, -0.25f, 0.5f, 2.5f, 0.5f), PartPose.ZERO);
        PartDefinition grandfatherPendulum = root.addOrReplaceChild("grandfather_pendulum", CubeListBuilder.create().addBox(-0.25f, 0, -0.25f, 0.5f, 15, 0.5f), PartPose.ZERO);
        grandfatherPendulum.addOrReplaceChild("grandfather_head", CubeListBuilder.create().texOffs(7, 7).addBox(-1, 15, -0.5f, 2, 2, 1), PartPose.ZERO);
        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void render(ClockBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer handMaterial = HAND_MATERIAL.buffer(buffer, RenderType::entityCutout);
        VertexConsumer pendulumMaterial = PENDULUM_MATERIAL.buffer(buffer, RenderType::entityCutout);
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        if (blockEntity.getBlockState().getBlock() instanceof FancyClockBlock) {
            renderHands(blockEntity, hourHand, minuteHand, 0.15625, 0.15625, stack, handMaterial, light, overlay);
            renderPendulum(blockEntity, pendulum, 4, -0.0625, 0.09375, stack, pendulumMaterial, light, overlay);
        } else if (blockEntity.getBlockState().getBlock() instanceof WallFancyClockBlock) {
            renderHands(blockEntity, hourHand, minuteHand, 0.15625, -0.15625, stack, handMaterial, light, overlay);
            renderPendulum(blockEntity, pendulum, 4, -0.0625, -0.21875, stack, pendulumMaterial, light, overlay);
        } else if (blockEntity.getBlockState().getBlock() instanceof GrandfatherClockBlock) {
            renderHands(blockEntity, grandfatherHourHand, grandfatherMinuteHand, 0.125, 0.15625, stack, handMaterial, light, overlay);
            renderPendulum(blockEntity, grandfatherPendulum, 17, -0.125, 0.09375, stack, pendulumMaterial, light, overlay);
        }
        stack.popPose();
    }

    private void renderHands(ClockBlockEntity blockEntity, ModelPart hourHand, ModelPart minuteHand, double y, double z, PoseStack stack, VertexConsumer vc, int light, int overlay) {
        float rotation = -Objects.requireNonNull(blockEntity.getLevel()).getSunAngle(1) * 2;
        stack.pushPose();
        stack.translate(0, y, z);
        stack.pushPose();
        stack.mulPose(Axis.ZP.rotation(rotation));
        hourHand.render(stack, vc, light, overlay);
        stack.popPose();
        stack.pushPose();
        stack.mulPose(Axis.ZP.rotation(rotation * 12));
        minuteHand.render(stack, vc, light, overlay);
        stack.popPose();
        stack.popPose();
    }

    private void renderPendulum(ClockBlockEntity blockEntity, ModelPart pendulum, float pendulumSize, double y, double z, PoseStack stack, VertexConsumer vc, int light, int overlay) {
        float rotation = (float) Math.sin((Objects.requireNonNull(blockEntity.getLevel()).getDayTime() % 40 - 20) * Math.PI / 20);
        stack.pushPose();
        stack.translate(0, y, z);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.ZP.rotation(rotation / pendulumSize));
        pendulum.render(stack, vc, light, overlay);
        stack.popPose();
    }
}
