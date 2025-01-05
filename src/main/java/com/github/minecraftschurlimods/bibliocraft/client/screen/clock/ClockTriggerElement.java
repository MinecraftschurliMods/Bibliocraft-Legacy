package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ClockTriggerElement extends AbstractWidget {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 20;
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/clock_trigger.png");
    private static final Component SEPARATOR = Component.translatable(Translations.CLOCK_TIME_SEPARATOR);
    private static final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);
    private static final ItemStack NOTE_BLOCK = new ItemStack(Items.NOTE_BLOCK);
    private final ClockTrigger trigger;
    private final ClockTriggerPanel owner;
    private int index;

    public ClockTriggerElement(int x, int y, ClockTrigger trigger, ClockTriggerPanel owner, int index) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("%s%s%s", trigger.hour(), SEPARATOR.getString(), trigger.minute()));
        this.trigger = trigger;
        this.owner = owner;
        this.index = index;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hasScrollbar = owner.hasScrollbar();
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
        graphics.drawString(Minecraft.getInstance().font, getMessage(), 36, 7, 0x404040, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
