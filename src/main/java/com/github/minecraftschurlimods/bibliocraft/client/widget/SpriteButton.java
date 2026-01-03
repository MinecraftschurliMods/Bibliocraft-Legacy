package com.github.minecraftschurlimods.bibliocraft.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class SpriteButton extends Button {
    public SpriteButton(int x, int y, int width, int height, OnPress onPress) {
        super(new Builder(Component.empty(), onPress).bounds(x, y, width, height));
    }

    @Nullable
    protected abstract Identifier getSprite();

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Identifier sprite = getSprite();
        if (sprite != null) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), getWidth(), getHeight());
        }
    }

    public static class RegularAndHighlightSprite extends SpriteButton {
        private final Identifier sprite;
        private final Identifier highlightedSprite;

        public RegularAndHighlightSprite(Identifier sprite, Identifier highlightedSprite, int x, int y, int width, int height, OnPress onPress) {
            super(x, y, width, height, onPress);
            this.sprite = sprite;
            this.highlightedSprite = highlightedSprite;
        }

        @Override
        protected Identifier getSprite() {
            return isHoveredOrFocused() ? highlightedSprite : sprite;
        }
    }
}
