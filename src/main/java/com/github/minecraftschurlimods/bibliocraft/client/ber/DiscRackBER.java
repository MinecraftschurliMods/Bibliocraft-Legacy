package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.WallDiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DiscRackBER implements BlockEntityRenderer<DiscRackBlockEntity> {
    @Override
    public void render(DiscRackBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
            stack.pushPose();
            if (blockEntity.getBlockState().getBlock() instanceof WallDiscRackBlock) {
                stack.translate(0.5, 0.5, 0.5);
                Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
                stack.mulPose(Axis.YP.rotationDegrees(facing.getAxis() == Direction.Axis.X ? 90 : 0));
                if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                    stack.mulPose(Axis.YP.rotationDegrees(180));
                }
                stack.mulPose(Axis.XP.rotationDegrees(90));
            } else {
                ClientUtil.setupCenteredBER(stack, blockEntity);
            }
            stack.translate(0, -0.25f, (i - 4) / 16f);
            stack.scale(0.5f, 0.5f, 0.5f);
            ClientUtil.renderFixedItem(blockEntity.getItem(i), stack, buffer, light, overlay);
            stack.popPose();
        }
    }
}
