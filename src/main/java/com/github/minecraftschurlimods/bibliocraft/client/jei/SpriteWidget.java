package com.github.minecraftschurlimods.bibliocraft.client.jei;

import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

@SuppressWarnings("unused")
public class SpriteWidget implements IRecipeWidget {
    private final Identifier sprite;
    private final ScreenPosition position;
    private final int blitOffset;
    private final int width;
    private final int height;

    public SpriteWidget(Identifier sprite, int x, int y, int blitOffset, int width, int height) {
        this.sprite = sprite;
        this.blitOffset = blitOffset;
        this.width = width;
        this.height = height;
        position = new ScreenPosition(x, y);
    }

    public SpriteWidget(Identifier sprite, int x, int y, int width, int height) {
        this(sprite, x, y, 0, width, height);
    }

    @Override
    public ScreenPosition getPosition() {
        return position;
    }

    @Override
    public void drawWidget(GuiGraphics graphics, double mouseX, double mouseY) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, width, height);
    }
}
