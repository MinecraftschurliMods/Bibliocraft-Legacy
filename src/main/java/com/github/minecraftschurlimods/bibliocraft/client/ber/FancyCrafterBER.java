package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class FancyCrafterBER implements BlockEntityRenderer<FancyCrafterBlockEntity> {
    @Override
    public void render(FancyCrafterBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        Level level = BCUtil.nonNull(blockEntity.getLevel());
        BlockPos pos = blockEntity.getBlockPos();
        Direction direction = blockEntity.getBlockState().getValue(FancyCrafterBlock.FACING);
        stack.pushPose();
        ClientUtil.setupCenteredBER(stack, blockEntity);
        if (!level.getBlockState(pos.above()).isSolidRender(level, pos.above())) {
            stack.pushPose();
            stack.translate(-0.1875f, 0.5, -0.1875f);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
            stack.scale(0.1875f, 0.1875f, 0.1875f);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    stack.pushPose();
                    stack.translate(-x, -y, 0);
                    ClientUtil.renderFixedItem(blockEntity.getItem(y * 3 + x), stack, buffer, light, overlay);
                    stack.popPose();
                }
            }
            stack.popPose();
        }
        if (!level.getBlockState(pos.offset(direction.getNormal())).isSolidRender(level, pos.offset(direction.getNormal()))) {
            stack.pushPose();
            stack.translate(-0.28125f, 0.03125, 0.25f);
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.scale(0.1875f, 0.1875f, 0.1875f);
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 4; x++) {
                    stack.pushPose();
                    stack.translate(-x, -y, 0);
                    ClientUtil.renderFixedItem(blockEntity.getItem(y * 2 + x + 10), stack, buffer, light, overlay);
                    stack.popPose();
                }
            }
            stack.popPose();
        }
        stack.popPose();
    }
}
