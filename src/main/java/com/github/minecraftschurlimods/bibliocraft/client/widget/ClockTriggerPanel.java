package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.client.screen.ClockScreen;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

public class ClockTriggerPanel extends ScrollPanel {
    public final ClockScreen owner;
    public final int width;
    public final int height;
    private final List<ClockTriggerElement> elements;

    public ClockTriggerPanel(int x, int y, int width, int height, List<ClockTrigger> triggers, ClockScreen owner) {
        super(ClientUtil.getMc(), width, height, y, x, 0);
        this.owner = owner;
        this.width = width;
        this.height = height;
        elements = new ArrayList<>();
        rebuildElements(triggers);
    }

    @Override
    protected int getContentHeight() {
        return elements.size() * ClockTriggerElement.HEIGHT;
    }

    @Override
    protected void drawPanel(GuiGraphics graphics, int entryRight, int relativeY, int mouseX, int mouseY) {
        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(left, relativeY);
        for (int i = 0; i < elements.size(); i++) {
            pose.pushMatrix();
            elements.get(i).render(graphics, mouseX - left, mouseY - i * ClockTriggerElement.HEIGHT - relativeY, 1);
            pose.popMatrix();
            pose.translate(0, ClockTriggerElement.HEIGHT);
        }
        pose.popMatrix();
    }

    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        ClockTriggerElement hovered = getHovered(mouseX, mouseY);
        if (hovered == null) return;
        float y = mouseY - top + scrollDistance;
        Matrix3x2fStack pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(left, mouseY);
        hovered.renderTooltip(graphics, mouseX - left, (int) (y % ClockTriggerElement.HEIGHT));
        pose.popMatrix();
    }

    @Override
    protected int getScrollAmount() {
        return hasScrollbar(elements.size()) ? ClockTriggerElement.HEIGHT / 2 : 0;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        ClockTriggerElement hovered = getHovered(event.x(), event.y());
        return hovered == null ? super.mouseClicked(event, doubleClick) : hovered.mouseClicked(new MouseButtonEvent(event.x() - left, (event.y() - top + scrollDistance) % ClockTriggerElement.HEIGHT, event.buttonInfo()), doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        ClockTriggerElement hovered = getHovered(event.x(), event.y());
        return hovered == null ? super.mouseDragged(event, deltaX, deltaY) : hovered.mouseDragged(new MouseButtonEvent(event.x() - left, (event.y() - top + scrollDistance) % ClockTriggerElement.HEIGHT, event.buttonInfo()), deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        ClockTriggerElement hovered = getHovered(event.x(), event.y());
        return hovered == null ? super.mouseReleased(event) : hovered.mouseReleased(new MouseButtonEvent(event.x() - left, (event.y() - top + scrollDistance) % ClockTriggerElement.HEIGHT, event.buttonInfo()));
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_, double p_294830_) {
        return hasScrollbar(elements.size()) && super.mouseScrolled(p_94686_, p_94687_, p_94688_, p_294830_);
    }

    public boolean hasScrollbar(int elements) {
        return elements * ClockTriggerElement.HEIGHT > height;
    }

    public void rebuildElements(List<ClockTrigger> triggers) {
        elements.clear();
        for (int i = 0; i < triggers.size(); i++) {
            elements.add(new ClockTriggerElement(triggers.get(i), this, triggers.size()));
        }
    }

    @Nullable
    public ClockTriggerElement getHovered(double mouseX, double mouseY) {
        double x = mouseX - left;
        if (x < 0 || x >= width) return null;
        double y = mouseY - top + scrollDistance;
        if (y < scrollDistance || y >= height + scrollDistance) return null;
        int index = (int) (y / ClockTriggerElement.HEIGHT);
        return index < 0 || index >= elements.size() ? null : elements.get(index);
    }
}
