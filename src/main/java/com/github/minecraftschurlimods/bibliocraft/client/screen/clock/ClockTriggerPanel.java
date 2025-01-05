package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.List;

public class ClockTriggerPanel extends ScrollPanel {
    private final List<ClockTriggerElement> elements;

    public ClockTriggerPanel(int left, int top, int width, int height, List<ClockTrigger> triggers) {
        super(Minecraft.getInstance(), width, height, top, left);
        elements = new ArrayList<>();
        buildElements(triggers);
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

    public void buildElements(List<ClockTrigger> triggers) {
        elements.clear();
        for (int i = 0; i < triggers.size(); i++) {
            elements.add(new ClockTriggerElement(0, i * ClockTriggerElement.HEIGHT, triggers.get(i), this, i));
        }
    }
}
