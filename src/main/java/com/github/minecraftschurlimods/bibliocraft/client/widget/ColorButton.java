package com.github.minecraftschurlimods.bibliocraft.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;

public class ColorButton extends Button {
    private final int color;

    public ColorButton(int color, Builder builder) {
        super(builder);
        this.color = 0xff000000 | color;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX(), y = getY();
        int xOffset = width / 8, yOffset = height / 8;
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        graphics.fill(RenderType.guiOverlay(), x + xOffset, y + yOffset, x + width - xOffset, y + height - yOffset, color);
    }
}
