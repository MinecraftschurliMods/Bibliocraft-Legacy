package com.github.minecraftschurlimods.bibliocraft.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class FormattedTextArea extends AbstractWidget implements Renderable {
    private static final int MAX_HEIGHT = 90;
    private static final int MAX_LINES = 15;
    private final String[] texts = new String[MAX_LINES];
    private final Style[] styles = new Style[MAX_LINES];
    private final int[] sizes = new int[MAX_LINES];
    private final boolean[] shadows = new boolean[MAX_LINES];
    private final Font font = Minecraft.getInstance().font;
    private int cursorX = 0;
    private int cursorY = 0;
    private long focusedTimestamp = Util.getMillis();
    private int effectiveMaxLines = MAX_LINES;

    public FormattedTextArea(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        for (int i = 0; i < effectiveMaxLines; i++) {
            renderLine(graphics, i, x, y);
            y += sizes[i];
        }
    }

    private void renderLine(GuiGraphics graphics, int line, int x, int y) {
        String text = texts[line];
        if (text.isEmpty()) return;
        Style style = styles[line];
        int size = sizes[line];
        boolean shadow = shadows[line];
        int color = Objects.requireNonNull(style.getColor()).getValue();

        // is the current line selected?
        boolean cursorInLine = cursorY == line;
        // is the cursor not at the end?
        boolean cursorInText = cursorInLine && cursorX < text.length();
        boolean shouldRenderCursor = cursorInLine && isFocused() && (Util.getMillis() - focusedTimestamp) / 300L % 2 == 0;

        if (cursorInLine) {
            if (cursorInText) {
                int textWidth = graphics.drawString(font, format(text.substring(0, cursorX), style), x, y, color, shadow);
                graphics.drawString(font, format(text.substring(cursorX), style), x + textWidth - 1, y, color, shadow);
                if (shouldRenderCursor) {
                    graphics.fill(RenderType.guiOverlay(), textWidth, y - 1, textWidth + 1, y + 10, color);
                }
            } else {
                int textWidth = graphics.drawString(font, format(text, style), x, y, color, shadow);
                if (shouldRenderCursor) {
                    graphics.drawString(font, "_", textWidth, y, color, shadow);
                }
            }
        } else {
            graphics.drawString(font, format(text, style), x, y, color, shadow);
        }
    }

    private FormattedCharSequence format(String text, Style style) {
        return FormattedCharSequence.forward(text, style);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActive() || !isFocused()) return false;
        return switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN -> {
                cursorY = Math.clamp(cursorY + 1, 0, MAX_LINES);
                cursorX = Math.min(cursorX, texts[cursorY].length() - 1);
                yield true;
            }
            case GLFW.GLFW_KEY_UP -> {
                cursorY = Math.clamp(cursorY - 1, 0, MAX_LINES);
                cursorX = Math.min(cursorX, texts[cursorY].length() - 1);
                yield true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                cursorX = Math.max(0, cursorX - 1);
                yield true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                cursorX = Math.min(cursorX + 1, texts[cursorY].length() - 1);
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
}
