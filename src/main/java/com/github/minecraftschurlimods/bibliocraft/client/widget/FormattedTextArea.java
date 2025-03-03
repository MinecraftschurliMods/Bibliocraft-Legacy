package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FormattedTextArea extends AbstractWidget {
    public static final int WIDTH = 140;
    public static final int HEIGHT = 80;
    private final Font font = Minecraft.getInstance().font;
    private final List<FormattedLine> lines;
    private int cursorX = 0;
    private int cursorY = 0;
    private long focusedTimestamp = Util.getMillis();

    public FormattedTextArea(int x, int y, Component message, List<FormattedLine> lines) {
        super(x, y, WIDTH, HEIGHT, message);
        this.lines = new ArrayList<>(lines);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        graphics.fill(x, y, x + width, y + height, 0xffffffff); //TODO background
        for (int i = 0; i < lines.size(); i++) {
            renderLine(graphics, i, x, y);
            y += lines.get(i).size();
        }
    }

    private void renderLine(GuiGraphics graphics, int index, int x, int y) {
        FormattedLine line = lines.get(index);
        String text = line.text();
        if (text == null || text.isEmpty()) return;
        Style style = line.style();
        int size = line.size();
        boolean shadow = line.shadow();
        int color = style.getColor() == null ? 0 : style.getColor().getValue();

        // is the current line selected?
        boolean cursorInLine = cursorY == index;
        // is the cursor not at the end?
        boolean cursorInText = cursorInLine && cursorX < text.length();
        boolean shouldRenderCursor = cursorInLine && isFocused() && (Util.getMillis() - focusedTimestamp) / 300L % 2 == 0;

        // scale the text, 8 is the default font size, and we subtract a padding of 1 on each side
        PoseStack pose = graphics.pose();
        pose.pushPose();
        float scale = (size - 2) / 8f;
        pose.scale(scale, scale, 1);

        // draw the text
        int textX = x + 1;
        if (cursorInLine) {
            if (cursorInText) {
                int textWidth = graphics.drawString(font, format(text.substring(0, cursorX), style), textX, y, color, shadow);
                graphics.drawString(font, format(text.substring(cursorX), style), textWidth, y, color, shadow);
                if (shouldRenderCursor) {
                    graphics.fill(RenderType.guiOverlay(), textWidth, y - 1, textWidth + 1, y + 10, 0xff000000 | color);
                }
            } else {
                int textWidth = graphics.drawString(font, format(text, style), textX, y, color, shadow);
                if (shouldRenderCursor) {
                    graphics.drawString(font, "_", textWidth, y, color, shadow);
                }
            }
        } else {
            graphics.drawString(font, format(text, style), textX, y, color, shadow);
        }
        pose.popPose();
    }

    private FormattedCharSequence format(String text, Style style) {
        return FormattedCharSequence.forward(text, style);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActive() || !isFocused()) return false;
        return switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN -> {
                cursorY = Math.clamp(cursorY + 1, 0, lines.size());
                cursorX = Math.min(cursorX, lines.get(cursorY).text().length());
                yield true;
            }
            case GLFW.GLFW_KEY_UP -> {
                cursorY = Math.clamp(cursorY - 1, 0, lines.size());
                cursorX = Math.min(cursorX, lines.get(cursorY).text().length());
                yield true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                cursorX = Math.max(0, cursorX - 1);
                yield true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                cursorX = Math.min(cursorX + 1, lines.get(cursorY).text().length());
                yield true;
            }
            default -> super.keyPressed(keyCode, scanCode, modifiers);
        };
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        focusedTimestamp = Util.getMillis();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!(isActive() && isFocused() && isEditable())) return false;
        if (!StringUtil.isAllowedChatCharacter(codePoint)) return false;
        if (isEditable()) {
            insertText(Character.toString(codePoint));
        }
        return true;
    }

    private void insertText(String text) {
        //TODO check if in bounds
        FormattedLine line = lines.get(cursorY);
        lines.set(cursorY, line.withText(line.text().substring(0, cursorX) + text + line.text().substring(cursorX)));
        cursorX += text.length();
    }

    private boolean isEditable() {
        return true; //TODO
    }

    public List<FormattedLine> getLines() {
        return lines;
    }
}
