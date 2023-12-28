package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.client.screen.RedstoneBookScreen;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/**
 * Utility class holding various helper methods. Kept separate from {@link BCUtil} for classloading reasons.
 */
public class ClientUtil {
    /**
     * Opens a {@link RedstoneBookScreen} on the client.
     */
    public static void openRedstoneBookScreen() {
        Minecraft.getInstance().setScreen(new RedstoneBookScreen());
    }

    /**
     * Translates the {@link PoseStack} into the block center and rotates it according to the block entity's rotation.
     *
     * @param stack       The pose stack to transform.
     * @param blockEntity The block entity to get the rotation from.
     */
    public static void setupCenteredBER(PoseStack stack, BCBlockEntity blockEntity) {
        stack.translate(0.5, 0.5, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(PotionShelfBlock.FACING)) {
            case SOUTH -> 0;
            case EAST -> 90;
            default -> 180;
            case WEST -> 270;
        }));
    }
}
