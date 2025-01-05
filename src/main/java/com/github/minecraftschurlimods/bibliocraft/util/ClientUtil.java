package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.client.screen.ClipboardScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.clock.ClockScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.RedstoneBookScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.StockroomCatalogScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

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
     * Opens a {@link ClockScreen} on the client.
     *
     * @param pos The {@link BlockPos} of the clock owning the screen.
     */
    public static void openClockScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new ClockScreen(pos));
    }

    /**
     * Opens a {@link RedstoneBookScreen} on the client.
     */
    public static void openRedstoneBookScreen() {
        Minecraft.getInstance().setScreen(new RedstoneBookScreen());
    }

    /**
     * Opens a {@link StockroomCatalogScreen} on the client.
     *
     * @param stack The owning {@link ItemStack} of the screen.
     */
    public static void openStockroomCatalogScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new StockroomCatalogScreen(stack));
    }

    /**
     * Translates the {@link PoseStack} into the block center and rotates it according to the block entity's rotation.
     *
     * @param stack       The pose stack to transform.
     * @param blockEntity The block entity to get the rotation from.
     */
    public static void setupCenteredBER(PoseStack stack, BlockEntity blockEntity) {
        stack.translate(0.5, 0.5, 0.5);
        BlockState state = blockEntity.getBlockState();
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            stack.mulPose(Axis.YP.rotationDegrees(switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case SOUTH -> 0;
                case EAST -> 90;
                default -> 180;
                case WEST -> 270;
            }));
        }
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
        renderItem(item, stack, buffer, light, overlay, ItemDisplayContext.FIXED);
    }

    /**
     * Renders an {@link ItemStack} in the {@link ItemDisplayContext#GUI} pose.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     */
    public static void renderGuiItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        renderItem(item, stack, buffer, light, overlay, ItemDisplayContext.GUI);
    }

    /**
     * Renders an {@link ItemStack} for use in a BER or GUI.
     *
     * @param item    The {@link ItemStack} to render.
     * @param stack   The {@link PoseStack} to use.
     * @param buffer  The {@link MultiBufferSource} to use.
     * @param light   The light value to use.
     * @param overlay The overlay value to use.
     * @param context The {@link ItemDisplayContext} to use.
     */
    public static void renderItem(ItemStack item, PoseStack stack, MultiBufferSource buffer, int light, int overlay, ItemDisplayContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer renderer = minecraft.getItemRenderer();
        renderer.render(item, context, context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND, stack, buffer, light, overlay, renderer.getModel(item, minecraft.level, null, 0));
    }

    /**
     * Renders the given {@link BakedModel} in the world.
     *
     * @param model     The {@link BakedModel} to render.
     * @param stack     The {@link PoseStack} to use.
     * @param buffer    The {@link MultiBufferSource} to use.
     * @param level     The {@link Level} to render the model in.
     * @param pos       The {@link BlockPos} to render the model at.
     * @param state     The {@link BlockState} to render the model for.
     * @param random    The {@link RandomSource} to use for random models.
     * @param modelData The {@link ModelData} to use.
     */
    public static void renderBakedModel(BakedModel model, PoseStack stack, MultiBufferSource buffer, Level level, BlockPos pos, BlockState state, RandomSource random, ModelData modelData) {
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        int color = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        int light = LevelRenderer.getLightColor(level, pos);
        for (RenderType type : model.getRenderTypes(state, random, modelData)) {
            renderer.renderModel(stack.last(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(type, false)), state, model, red, green, blue, light, OverlayTexture.NO_OVERLAY, modelData, type);
        }
    }
}
