package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class RedstoneBookScreen extends Screen {
    public RedstoneBookScreen() {
        super(Translations.REDSTONE_BOOK_TITLE);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float partialTick) {
        super.render(graphics, x, y, partialTick);
        FormattedText text = FormattedText.of(Translations.REDSTONE_BOOK_TEXT.getString());
        List<FormattedCharSequence> lines = font.split(text, 114);
        int startX = (width - 192) / 2;
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(font, lines.get(i), startX + 36, 20 + i * 9, 0, false);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int x, int y, float partialTick) {
        super.renderBackground(graphics, x, y, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BookViewScreen.BOOK_LOCATION, (this.width - 192) / 2, 2, 0, 0, 192, 192, 256, 256);
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 - 100, 196, 200, 20).build());
    }
}
