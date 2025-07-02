package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ExperienceBarButton extends Button {
    private final ResourceLocation backgroundTexture;
    private final ResourceLocation progressTexture;
    private final IntSupplier levelGetter;
    private final Supplier<Float> progressGetter;

    public ExperienceBarButton(Component message, int x, int y, int width, int height, ResourceLocation backgroundTexture, ResourceLocation progressTexture, IntSupplier levelGetter, Supplier<Float> progressGetter, OnPress onPress) {
        super(new Builder(message, onPress).bounds(x, y, width, height).tooltip(Tooltip.create(message)));
        this.backgroundTexture = backgroundTexture;
        this.progressTexture = progressTexture;
        this.levelGetter = levelGetter;
        this.progressGetter = progressGetter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(backgroundTexture, getWidth(), getHeight(), 0, 0, getX(), getY(), getWidth(), getHeight());
        graphics.blitSprite(progressTexture, getWidth(), getHeight(), 0, 0, getX(), getY(), (int) (getWidth() * progressGetter.get()), getHeight());
        ClientUtil.renderXpText(levelGetter.getAsInt() + "", graphics, getX() + getWidth() / 2, getY() - 4);
    }
}
