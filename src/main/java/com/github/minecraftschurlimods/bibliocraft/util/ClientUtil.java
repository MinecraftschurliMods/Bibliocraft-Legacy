package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.BCConfig;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BigBookScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.ClipboardScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.ClockScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.FancySignScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.RedstoneBookScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.StockroomCatalogScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.TypewriterPageScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.TypewriterScreen;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.List;

/**
 * Utility class holding various helper methods. Kept separate from {@link BCUtil} for classloading reasons.
 */
public final class ClientUtil {
    private static RecipeMap recipeMap = RecipeMap.EMPTY;
    
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
    public static @Nullable ClientLevel getLevel() {
        return getMc().level;
    }

    /**
     * Helper to get the {@link LocalPlayer} instance from the {@link Minecraft} instance.
     *
     * @return The {@link LocalPlayer} instance.
     */
    public static @Nullable LocalPlayer getPlayer() {
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
     * Opens a {@link TypewriterPageScreen} on the client.
     *
     * @param stack The typewriter page's {@link ItemStack}.
     */
    public static void openTypewriterPageScreen(ItemStack stack) {
        getMc().setScreen(new TypewriterPageScreen(stack));
    }

    /**
     * Translates the {@link PoseStack} into the block center and rotates it according to the block entity's rotation.
     *
     * @param stack       The pose stack to transform.
     * @param blockEntity The block entity to get the rotation from.
     */
    public static void setupCenteredBER(PoseStack stack, BlockEntityRenderState blockEntity) {
        stack.translate(0.5, 0.5, 0.5);
        BlockState state = blockEntity.blockState;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            stack.mulPose(Axis.YP.rotationDegrees(switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case SOUTH -> 0;
                case EAST -> 90;
                case WEST -> 270;
                default -> 180;
            }));
        }
    }

    /**
     * Renders text in the formatting of the experience level number above the hotbar.
     *
     * @param text     The text to render.
     * @param graphics The {@link GuiGraphics} to use.
     * @param centerX  The horizontal center of the text.
     * @param startY   The y coordinate of the text. Be aware that there will be a 1px outline above this position.
     * @see net.minecraft.client.gui.Gui#renderExperienceLevel(GuiGraphics, DeltaTracker)
     */
    public static void renderXpText(String text, GuiGraphics graphics, int centerX, int startY) {
        Font font = getFont();
        int startX = centerX - font.width(text) / 2;
        graphics.drawString(font, text, startX + 1, startY, 0, false);
        graphics.drawString(font, text, startX - 1, startY, 0, false);
        graphics.drawString(font, text, startX, startY + 1, 0, false);
        graphics.drawString(font, text, startX, startY - 1, 0, false);
        graphics.drawString(font, text, startX, startY, 0xff80ff20, false);
    }

    /**
     * @return Whether pride-themed content should be displayed.
     */
    public static boolean isPride() {
        return BCConfig.ENABLE_PRIDE.get() && (BCConfig.ENABLE_PRIDE_ALWAYS.get() || Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE);
    }

    /**
     * Classloading guard for setting the stockroom catalog contents from a packet.
     *
     * @param packet The packet containing the stockroom catalog contents.
     */
    public static void setStockroomCatalogList(StockroomCatalogListPacket packet) {
        if (getMc().screen instanceof StockroomCatalogScreen screen) {
            screen.setFromPacket(packet);
        }
    }

    public static List<ClientTooltipComponent> forTooltip(Component component) {
        return List.of(ClientTooltipComponent.create(component.getVisualOrderText()));
    }

    public static void onReceiveRecipes(RecipesReceivedEvent event) {
        recipeMap = event.getRecipeMap();
    }

    public static RecipeMap getRecipeMap() {
        return recipeMap;
    }
}
