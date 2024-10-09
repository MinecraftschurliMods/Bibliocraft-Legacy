package com.github.minecraftschurlimods.bibliocraft.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class SpriteButton extends Button {
    public SpriteButton(int x, int y, int width, int height, OnPress onPress) {
        super(new Builder(Component.empty(), onPress).bounds(x, y, width, height));
    }

    @Nullable
    protected abstract ResourceLocation getSprite();

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation sprite = getSprite();
        if (sprite != null) {
            guiGraphics.blitSprite(sprite, getX(), getY(), getWidth(), getHeight());
        }
    }
}
