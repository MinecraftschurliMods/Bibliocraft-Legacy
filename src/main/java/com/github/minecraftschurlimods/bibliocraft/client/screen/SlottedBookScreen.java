package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.slottedbook.SlottedBookMenu;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class SlottedBookScreen extends AbstractContainerScreen<SlottedBookMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/slotted_book.png");

    public SlottedBookScreen(SlottedBookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 223;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    public void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        FormattedText text = FormattedText.of(Translations.SLOTTED_BOOK_TEXT.getString());
        List<FormattedCharSequence> lines = font.split(text, 114);
        int startX = (width - 192) / 2;
        int startY = topPos + 111 - lines.size() * 9;
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(font, lines.get(i), startX + 38, startY + i * 9, 0, false);
        }
        renderTooltip(graphics, mouseX, mouseY);
    }
}
