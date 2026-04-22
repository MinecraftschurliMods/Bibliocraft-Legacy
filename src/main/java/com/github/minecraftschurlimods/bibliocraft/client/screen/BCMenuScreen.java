package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BCMenuScreen<T extends BCMenu<?>> extends AbstractContainerScreen<T> {
    private final Identifier background;

    public BCMenuScreen(T menu, Inventory inventory, Component title, Identifier background, int imageWidth, int imageHeight) {
        super(menu, inventory, title, imageWidth, imageHeight);
        this.background = background;
    }

    public BCMenuScreen(T menu, Inventory inventory, Component title, Identifier background) {
        super(menu, inventory, title);
        this.background = background;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public Component getTitle() {
        return menu.getDisplayName();
    }
}
