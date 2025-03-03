package com.github.minecraftschurlimods.bibliocraft.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

public class ColorButton extends Button {
    private final int color;
    
    public ColorButton(int color, Builder builder) {
        super(builder);
        this.color = color;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX(), y = getY();
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        graphics.fill(x, y, x + width, y + height, color);
    }
}
