package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.client.screen.ClipboardScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.RedstoneBookScreen;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Utility class holding various helper methods. Kept separate from {@link BCUtil} for classloading reasons.
 */
public final class ClientUtil {
    /**
     * Opens a {@link ClipboardScreen} on the client.
     *
     * @param stack The owning {@link ItemStack} of the screen.
     */
    public static void openClipboardScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new ClipboardScreen(stack));
    }

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
        stack.mulPose(Axis.YP.rotationDegrees(switch (blockEntity.getBlockState().getValue(BlockStateProperties.FACING)) {
            case SOUTH -> 0;
            case EAST -> 90;
            default -> 180;
            case WEST -> 270;
        }));
    }

    /**
     * Renders an {@link ItemStack} in the {@link ItemDisplayContext#FIXED} pose.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     */
    public static void renderFixedItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer renderer = minecraft.getItemRenderer();
        renderer.render(item, ItemDisplayContext.FIXED, false, stack, buffer, light, overlay, renderer.getModel(item, minecraft.level, null, 0));
    }
}
