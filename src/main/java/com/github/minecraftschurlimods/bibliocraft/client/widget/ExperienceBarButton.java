package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ExperienceBarButton extends Button {
    private final Identifier backgroundTexture;
    private final Identifier progressTexture;
    private final IntSupplier levelGetter;
    private final Supplier<Float> progressGetter;

    public ExperienceBarButton(Component message, int x, int y, int width, int height, Identifier backgroundTexture, Identifier progressTexture, IntSupplier levelGetter, Supplier<Float> progressGetter, OnPress onPress) {
        super(new Builder(message, onPress).bounds(x, y, width, height).tooltip(Tooltip.create(message)));
        this.backgroundTexture = backgroundTexture;
        this.progressTexture = progressTexture;
        this.levelGetter = levelGetter;
        this.progressGetter = progressGetter;
    }

    @Override
    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, backgroundTexture, getWidth(), getHeight(), 0, 0, getX(), getY(), getWidth(), getHeight());
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, progressTexture, getWidth(), getHeight(), 0, 0, getX(), getY(), (int) (getWidth() * progressGetter.get()), getHeight());
        ClientUtil.renderXpText(levelGetter.getAsInt() + "", graphics, getX() + getWidth() / 2, getY() - 4);
    }
}
