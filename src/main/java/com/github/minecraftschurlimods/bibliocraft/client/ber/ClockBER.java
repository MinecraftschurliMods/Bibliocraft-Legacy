package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.GrandfatherClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.WallFancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Conceptual credit for the clock hands:
 * <a href="https://github.com/MehVahdJukaar/Supplementaries/blob/master/common/src/main/java/net/mehvahdjukaar/supplementaries/client/renderers/tiles/ClockBlockTileRenderer.java">ClockBlockTileRenderer from the Supplementaries mod</a>
 */
public class ClockBER implements BlockEntityRenderer<ClockBlockEntity, ClockBER.State> {
    public static final Material HAND_MATERIAL = new Material(AtlasIds.BLOCKS, BCUtil.bcLoc("block/clock_hand"));
    public static final Material PENDULUM_MATERIAL = new Material(AtlasIds.BLOCKS, BCUtil.mcLoc("block/copper_block"));
    public static final ModelLayerLocation LOCATION = new ModelLayerLocation(BCUtil.bcLoc("clock"), "main");
    private final ModelPart hourHand;
    private final ModelPart minuteHand;
    private final ModelPart pendulum;
    private final ModelPart grandfatherHourHand;
    private final ModelPart grandfatherMinuteHand;
    private final ModelPart grandfatherPendulum;
    private float rotation;
    private float rotationOld;
    private long lastUpdateTick;

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

    private float getRotation(long gameTime) {
        float rotationNew = (float) Math.random();
        if (gameTime != lastUpdateTick) {
            lastUpdateTick = gameTime;
            float f = rotationNew - rotation;
            f = Mth.positiveModulo(f + 0.5f, 1) - 0.5f;
            rotationOld += f * 0.1f;
            rotationOld *= 0.9f;
            rotation = Mth.positiveModulo(rotation + rotationOld, 1);
        }
        return rotation * (float) Math.PI * 4;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void submit(State state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        RenderType handMaterial = HAND_MATERIAL.renderType(RenderType::entityCutout);
        RenderType pendulumRenderType = PENDULUM_MATERIAL.renderType(RenderType::entityCutout);
        ModelPart hourHand = state.isGrandfather ? this.grandfatherHourHand : this.hourHand;
        ModelPart minuteHand = state.isGrandfather ? this.grandfatherMinuteHand : this.minuteHand;
        ModelPart pendulum = state.isGrandfather ? this.grandfatherPendulum : this.pendulum;
        float handsRotation = state.isNaturalDimension ? -((state.dayTime + 6000) % 12000) * 0.03f : getRotation(state.gameTime);
        float pendulumRotation = (float) Math.sin((state.dayTime % 40 - 20) * Math.PI / 20);

        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, state);
        stack.pushPose();
        stack.translate(0, state.handY, state.handZ);
        stack.pushPose();
        stack.mulPose(Axis.ZP.rotationDegrees(handsRotation));
        collector.submitModelPart(hourHand, stack, handMaterial, state.lightCoords, OverlayTexture.NO_OVERLAY, null);
        stack.popPose();
        stack.pushPose();
        stack.mulPose(Axis.ZP.rotationDegrees((handsRotation * 12) % 360));
        collector.submitModelPart(minuteHand, stack, handMaterial, state.lightCoords, OverlayTexture.NO_OVERLAY, null);
        stack.popPose();
        stack.popPose();
        stack.pushPose();
        stack.translate(0, state.pendulumY, state.pendulumZ);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.ZP.rotation(pendulumRotation / state.pendulumSize));
        collector.submitModelPart(pendulum, stack, pendulumRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, null);
        stack.popPose();
        stack.popPose();
    }

    @Override
    public void extractRenderState(ClockBlockEntity blockEntity, State state, float p_446851_, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, p_446851_, p_445788_, breakProgress);
        Level level = BCUtil.nonNull(blockEntity.getLevel());
        state.dayTime = level.getDayTime();
        state.gameTime = level.getGameTime();
        state.isNaturalDimension = level.dimensionType().natural();

        if (blockEntity.getBlockState().getBlock() instanceof FancyClockBlock) {
            state.isGrandfather = false;
            state.handY = 0.15625;
            state.handZ = 0.15625;
            state.pendulumSize = (float) 4;
            state.pendulumY = -0.0625;
            state.pendulumZ = 0.09375;
        } else if (blockEntity.getBlockState().getBlock() instanceof WallFancyClockBlock) {
            state.isGrandfather = false;
            state.handY = 0.15625;
            state.handZ = -0.15625;
            state.pendulumSize = (float) 4;
            state.pendulumY = -0.0625;
            state.pendulumZ = -0.21875;
        } else if (blockEntity.getBlockState().getBlock() instanceof GrandfatherClockBlock) {
            state.isGrandfather = true;
            state.handY = 0.125;
            state.handZ = 0.15625;
            state.pendulumSize = (float) 17;
            state.pendulumY = -0.125;
            state.pendulumZ = 0.09375;
        }
    }

    public static class State extends BlockEntityRenderState {
        private boolean isNaturalDimension;
        private long gameTime;
        private long dayTime;
        private boolean isGrandfather;
        private float pendulumSize;
        private double pendulumY;
        private double pendulumZ;
        private double handY;
        private double handZ;
    }
}
