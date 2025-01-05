package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class ClockTriggerPanel extends ScrollPanel {
    private final List<ClockTriggerElement> elements;

    public ClockTriggerPanel(int left, int top, int width, int height) {
        super(Minecraft.getInstance(), width, height, top, left);
        elements = new ArrayList<>();
        elements.addAll(List.of(
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "00", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "01", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "02", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "03", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "04", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "05", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "06", "00"), this),
                new ClockTriggerElement(0, 0, Component.translatable(Translations.CLOCK_TIME, "07", "00"), this)
        ));
    }

    @Override
    protected int getContentHeight() {
        return elements.size() * ClockTriggerElement.HEIGHT - 4;
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(left, relativeY - ClockTriggerElement.HEIGHT - 4, 0);
        for (ClockTriggerElement element : elements) {
            pose.translate(0, ClockTriggerElement.HEIGHT, 0);
            pose.pushPose();
            element.renderWidget(guiGraphics, mouseX, mouseY, 1);
            pose.popPose();
        }
        pose.popPose();
    }

    @Override
    protected int getScrollAmount() {
        return ClockTriggerElement.HEIGHT / 2;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
    }

    public boolean hasScrollbar() {
        return elements.size() * ClockTriggerElement.HEIGHT > height;
    }
}
