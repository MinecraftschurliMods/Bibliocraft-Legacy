package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPage;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;

public class TypewriterScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/typewriter_page.png");
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 164;
    private final BlockPos pos;
    private TypewriterPage page;
    private int leftPos;
    private int topPos;
    private int row;
    private String currentLine;
    private int frameTick;

    public TypewriterScreen(BlockPos pos) {
        super(Component.empty());
        this.pos = pos;
        page = ClientUtil.getLevel().getBlockEntity(pos) instanceof TypewriterBlockEntity typewriter ? typewriter.getPage() : new TypewriterPage();
        row = page.line();
        if (row == TypewriterPage.MAX_LINES) {
            onClose();
        } else {
            currentLine = page.lines().get(row);
        }
    }

    @Override
    protected void init() {
        leftPos = (width - IMAGE_WIDTH) / 2;
        topPos = (height - IMAGE_HEIGHT) / 2;
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 - 100, topPos + IMAGE_HEIGHT + 4, 200, 20)
                .build());
    }

    @Override
    public void tick() {
        super.tick();
        frameTick++;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
        //TODO sync
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        Font font = ClientUtil.getFont();
        for (int i = 0; i < row; i++) {
            if (i >= page.lines().size()) continue;
            int width = graphics.drawString(font, page.lines().get(i), leftPos + 2, topPos + 2 + i * 10, 0, false);
        }
        if (row < TypewriterPage.MAX_LINES) {
            int width = graphics.drawString(font, currentLine, leftPos + 2, topPos + 2 + row * 10, 0, false);
            if (frameTick / 6 % 2 == 0) {
                graphics.drawString(font, "_", width, topPos + 2 + row * 10, 0, false);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (StringUtil.isAllowedChatCharacter(codePoint)) {
            currentLine += codePoint;
            if (currentLine.length() >= TypewriterPage.MAX_LINE_LENGTH) {
                lineBreak();
            }
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    private void lineBreak() {
        //TODO play sound
        page = page.copy();
        page.lines().set(row, currentLine);
        currentLine = "";
        row++;
        if (row >= TypewriterPage.MAX_LINES) {
            onClose();
        }
    }
}
