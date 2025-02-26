package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class FancySignScreen extends Screen {
    private final BlockPos pos;
    private FormattedTextArea textArea;
    
    public FancySignScreen(BlockPos pos) {
        super(Component.empty());
        this.pos = pos;
    }

    @Override
    protected void init() {
        int x = (width - 192) / 2;
        if (Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign) {
            textArea = addRenderableWidget(new FormattedTextArea(x, 6, 192, 90, Component.empty(), sign.getFrontContent().lines()));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        //TODO
    }
}
