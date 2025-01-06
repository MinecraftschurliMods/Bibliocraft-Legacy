package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.client.SpriteButton;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ClockTriggerElement extends Screen {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 20;
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/clock_trigger.png");
    private static final ResourceLocation EDIT = BCUtil.modLoc("edit");
    private static final ResourceLocation EDIT_HIGHLIGHTED = BCUtil.modLoc("edit_highlighted");
    private static final ResourceLocation DELETE = BCUtil.modLoc("delete");
    private static final ResourceLocation DELETE_HIGHLIGHTED = BCUtil.modLoc("delete_highlighted");
    private static final Component DELETE_TRIGGER = Component.translatable(Translations.CLOCK_DELETE_TRIGGER);
    private static final Component EDIT_TRIGGER = Component.translatable(Translations.CLOCK_EDIT_TRIGGER);
    private static final Component EMIT_REDSTONE = Component.translatable(Translations.CLOCK_EMIT_REDSTONE);
    private static final Component EMIT_SOUND = Component.translatable(Translations.CLOCK_EMIT_SOUND);
    private static final Component SEPARATOR = Component.translatable(Translations.CLOCK_TIME_SEPARATOR);
    private static final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);
    private static final ItemStack NOTE_BLOCK = new ItemStack(Items.NOTE_BLOCK);
    public final ClockTriggerPanel owner;
    private final ClockTrigger trigger;
    private final int listSize;
    private final Button editButton;
    private final Button deleteButton;

    public ClockTriggerElement(ClockTrigger trigger, ClockTriggerPanel owner, int listSize) {
        super(Component.literal(String.format("%02d%s%02d", trigger.hour(), SEPARATOR.getString(), trigger.minute())));
        this.width = WIDTH;
        this.height = HEIGHT;
        this.trigger = trigger;
        this.owner = owner;
        this.listSize = listSize;
        int width = owner.hasScrollbar(listSize) ? WIDTH - 6 : WIDTH;
        editButton = addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(EDIT, EDIT_HIGHLIGHTED, width - 34, 2, 16, 16, $ -> Minecraft.getInstance().pushGuiLayer(new ClockTriggerEditScreen(owner.owner, trigger))));
        deleteButton = addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(DELETE, DELETE_HIGHLIGHTED, width - 18, 2, 16, 16, $ -> owner.owner.removeTrigger(trigger)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hasScrollbar = owner.hasScrollbar(listSize);
        graphics.blit(BACKGROUND, 0, 0, 0, hasScrollbar ? 20 : 0, hasScrollbar ? WIDTH - 6 : WIDTH, HEIGHT);
        PoseStack pose = graphics.pose();
        MultiBufferSource.BufferSource buffer = graphics.bufferSource();
        pose.pushPose();
        pose.translate(8, 12, 0);
        pose.scale(16, -16, 0);
        pose.translate(0.125, 0.125, 0);
        if (trigger.redstone()) {
            ClientUtil.renderGuiItem(REDSTONE, pose, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
        pose.translate(1.0625, 0, 0);
        if (trigger.sound()) {
            ClientUtil.renderGuiItem(NOTE_BLOCK, pose, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
        pose.popPose();
        graphics.drawString(Minecraft.getInstance().font, getTitle(), 36, 7, 0x404040, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    }

    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (mouseY >= 2 && mouseY < 18) {
            if (trigger.redstone() && mouseX >= 2 && mouseX < 18) {
                graphics.renderTooltip(Minecraft.getInstance().font, EMIT_REDSTONE, mouseX, mouseY);
            } else if (trigger.sound() && mouseX >= 19 && mouseX < 37) {
                graphics.renderTooltip(Minecraft.getInstance().font, EMIT_SOUND, mouseX, mouseY);
            } else if (editButton.isHovered()) {
                graphics.renderTooltip(Minecraft.getInstance().font, EDIT_TRIGGER, mouseX, mouseY);
            } else if (deleteButton.isHovered()) {
                graphics.renderTooltip(Minecraft.getInstance().font, DELETE_TRIGGER, mouseX, mouseY);
            }
        }
    }
}
