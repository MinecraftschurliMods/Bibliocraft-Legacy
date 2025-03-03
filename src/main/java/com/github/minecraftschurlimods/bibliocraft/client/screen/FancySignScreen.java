package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineList;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineListPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

public class FancySignScreen extends Screen {
    private final BlockPos pos;
    private final boolean back;
    private FormattedTextArea textArea;
    
    public FancySignScreen(BlockPos pos, boolean back) {
        super(Component.empty());
        this.pos = pos;
        this.back = back;
    }

    @Override
    protected void init() {
        int x = (width - FormattedTextArea.WIDTH) / 2;
        if (Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign) {
            textArea = addRenderableWidget(new FormattedTextArea(x, 6, Component.empty(), sign.getFrontContent().lines()));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        //TODO
    }

    @Override
    public void onClose() {
        if (!(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign)) return;
        FormattedLineList list = new FormattedLineList(textArea.getLines());
        if (back) {
            sign.setBackContent(list);
        } else {
            sign.setFrontContent(list);
        }
        PacketDistributor.sendToServer(new FormattedLineListPacket(list, pos, back));
        super.onClose();
    }
}
