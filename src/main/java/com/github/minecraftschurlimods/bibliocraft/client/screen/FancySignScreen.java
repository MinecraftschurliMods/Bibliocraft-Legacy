package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.FormattedTextArea;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class FancySignScreen extends Screen {
    private FormattedTextArea textArea;
    
    public FancySignScreen(BlockPos pos) {
        super(Component.empty());
    }

    @Override
    protected void init() {
        int x = (width - 192) / 2;
        textArea = addRenderableWidget(new FormattedTextArea(0, 6, 192, 90, Component.empty()));
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        //TODO
    }
}
