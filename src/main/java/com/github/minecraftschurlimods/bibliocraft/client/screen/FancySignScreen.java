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
        textArea = addRenderableWidget(new FormattedTextArea(0, 0, 300, 90, Component.empty()));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        //TODO
    }
}
