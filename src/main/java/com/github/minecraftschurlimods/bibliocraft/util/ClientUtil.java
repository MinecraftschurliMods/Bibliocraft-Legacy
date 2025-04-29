package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.Config;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BigBookScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.ClipboardScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.ClockScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.FancySignScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.RedstoneBookScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.StockroomCatalogScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.TypewriterScreen;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Calendar;

/**
 * Utility class holding various helper methods. Kept separate from {@link BCUtil} for classloading reasons.
 */
public final class ClientUtil {
    /**
     * Helper to get the {@link Minecraft} instance.
     *
     * @return The {@link Minecraft} instance.
     */
    public static Minecraft getMc() {
        return Minecraft.getInstance();
    }

    /**
     * Helper to get the {@link ClientLevel} instance from the {@link Minecraft} instance.
     *
     * @return The {@link ClientLevel} instance.
     */
    public static ClientLevel getLevel() {
        return getMc().level;
    }

    /**
     * Helper to get the {@link LocalPlayer} instance from the {@link Minecraft} instance.
     *
     * @return The {@link LocalPlayer} instance.
     */
    public static LocalPlayer getPlayer() {
        return getMc().player;
    }

    /**
     * Helper to get the {@link Font} instance from the {@link Minecraft} instance.
     *
     * @return The {@link Font} instance.
     */
    public static Font getFont() {
        return getMc().font;
    }

    /**
     * Opens a {@link BigBookScreen} on the client.
     *
     * @param stack  The owning {@link ItemStack} of the screen.
     * @param player The owning {@link Player} of the screen.
     * @param hand   The {@link InteractionHand} in which the big book is held.
     */
    public static void openBigBookScreen(ItemStack stack, Player player, InteractionHand hand) {
        getMc().setScreen(new BigBookScreen(stack, player, hand));
    }

    /**
     * Opens a {@link BigBookScreen} on the client.
     *
     * @param stack   The owning {@link ItemStack} of the screen.
     * @param player  The owning {@link Player} of the screen.
     * @param lectern The owning lectern's {@link BlockPos}.
     */
    public static void openBigBookScreen(ItemStack stack, Player player, BlockPos lectern) {
        getMc().setScreen(new BigBookScreen(stack, player, lectern));
    }

    /**
     * Opens a {@link ClipboardScreen} on the client.
     *
     * @param stack The owning {@link ItemStack} of the screen.
     * @param hand  The {@link InteractionHand} in which the clipboard is held.
     */
    public static void openClipboardScreen(ItemStack stack, InteractionHand hand) {
        getMc().setScreen(new ClipboardScreen(stack, hand));
    }

    /**
     * Opens a {@link ClockScreen} on the client.
     *
     * @param pos The {@link BlockPos} of the clock owning the screen.
     */
    public static void openClockScreen(BlockPos pos) {
        getMc().setScreen(new ClockScreen(pos));
    }

    /**
     * Opens a {@link FancySignScreen} on the client.
     *
     * @param pos  The {@link BlockPos} of the clock owning the screen.
     * @param back Whether the back of the sign was clicked or not.
     */
    public static void openFancySignScreen(BlockPos pos, boolean back) {
        getMc().setScreen(new FancySignScreen(pos, back));
    }

    /**
     * Opens a {@link RedstoneBookScreen} on the client.
     */
    public static void openRedstoneBookScreen() {
        getMc().setScreen(new RedstoneBookScreen());
    }

    /**
     * Opens a {@link StockroomCatalogScreen} on the client.
     *
     * @param stack  The owning {@link ItemStack} of the screen.
     * @param player The owning {@link Player} of the screen.
     * @param hand   The {@link InteractionHand} in which the stockroom catalog is held.
     */
    public static void openStockroomCatalogScreen(ItemStack stack, Player player, InteractionHand hand) {
        getMc().setScreen(new StockroomCatalogScreen(stack, player, hand));
    }

    /**
     * Opens a {@link StockroomCatalogScreen} on the client.
     *
     * @param stack   The owning {@link ItemStack} of the screen.
     * @param player  The owning {@link Player} of the screen.
     * @param lectern The owning lectern's {@link BlockPos}.
     */
    public static void openStockroomCatalogScreen(ItemStack stack, Player player, BlockPos lectern) {
        getMc().setScreen(new StockroomCatalogScreen(stack, player, lectern));
    }

    /**
     * Opens a {@link TypewriterScreen} on the client.
     *
     * @param pos The typewriter's {@link BlockPos}.
     */
    public static void openTypewriterScreen(BlockPos pos) {
        getMc().setScreen(new TypewriterScreen(pos));
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
        Minecraft minecraft = getMc();
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
        ModelBlockRenderer renderer = getMc().getBlockRenderer().getModelRenderer();
        int color = getMc().getBlockColors().getColor(state, level, pos, 0);
        float red = (float) (color >> 16 & 255) / 255f;
        float green = (float) (color >> 8 & 255) / 255f;
        float blue = (float) (color & 255) / 255f;
        int light = LevelRenderer.getLightColor(level, pos);
        for (RenderType type : model.getRenderTypes(state, random, modelData)) {
            renderer.renderModel(stack.last(), buffer.getBuffer(RenderTypeHelper.getEntityRenderType(type, false)), state, model, red, green, blue, light, OverlayTexture.NO_OVERLAY, modelData, type);
        }
    }

    /**
     * @return Whether pride-themed content should be displayed.
     */
    public static boolean isPride() {
        return Config.ENABLE_PRIDE.get() && (Config.ENABLE_PRIDE_ALWAYS.get() || Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE);
    }

    /**
     * Classloading guard for setting the stockroom catalog contents from a packet.
     * @param packet The packet containing the stockroom catalog contents.
     */
    public static void setStockroomCatalogList(StockroomCatalogListPacket packet) {
        if (getMc().screen instanceof StockroomCatalogScreen screen) {
            screen.setFromPacket(packet);
        }
    }
}
