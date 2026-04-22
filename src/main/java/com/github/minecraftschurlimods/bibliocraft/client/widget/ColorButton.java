package com.github.minecraftschurlimods.bibliocraft.client.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;

public class ColorButton extends Button {
    private final int color;

    public ColorButton(int color, Builder builder) {
        super(builder);
        this.color = 0xff000000 | color;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX(), y = getY();
        int xOffset = width / 8, yOffset = height / 8;
        graphics.fill(x + xOffset, y + yOffset, x + width - xOffset, y + height - yOffset, color);
    }
}
