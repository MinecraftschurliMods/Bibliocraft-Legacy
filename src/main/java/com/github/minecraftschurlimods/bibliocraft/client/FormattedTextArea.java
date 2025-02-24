package com.github.minecraftschurlimods.bibliocraft.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
    private int highlightX = 0;
    private int highlightY = 0;
    @Nullable
    private Consumer<String> responder;
    private BiFunction<String, Integer, FormattedCharSequence> formatter = (p_94147_, p_94148_) -> FormattedCharSequence.forward(p_94147_, Style.EMPTY);
    private long focusedTime = Util.getMillis();
    private int effectiveMaxLines = MAX_LINES;

    public FormattedTextArea(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        for (int i = 0; i < effectiveMaxLines; i++) {
            // get values
            String text = texts[i];
            if (text.isEmpty()) continue;
            Style style = styles[i];
            FormattedCharSequence formattedText = FormattedCharSequence.forward(text, style);
            int size = sizes[i];
            boolean shadow = shadows[i];
            int color = Objects.requireNonNull(style.getColor()).getValue();
            int x = getX();
            int y = getY();
            boolean cursorInLine = cursorY == i;
            boolean cursorInText = cursorX < text.length();
            // draw text before the cursor
            int textWidth = graphics.drawString(font, formattedText, x, y, color, shadow);
            // draw text after the cursor
            int minX = textWidth;
            if (cursorInText) {
                minX = textWidth - 1;
                textWidth--;
            }
            if (cursorX < text.length()) {
                graphics.drawString(font, formattedText, textWidth, y, color, shadow);
            }
            // draw cursor
            int minY = y - 1;
            int maxY = y + 10;
            if (isFocused() && (Util.getMillis() - focusedTime) / 300L % 2 == 0) {
                if (cursorInText) {
                    graphics.fill(RenderType.guiOverlay(), minX, minY, minX + 1, maxY, 0xffd0d0d0);
                } else {
                    graphics.drawString(font, "_", minX, y, color, shadow);
                }
            }
            // draw text selection
            if (highlightX != cursorX) {
                int maxX = x + font.width(text.substring(0, highlightX)) - 1;
                if (minX < maxX) {
                    int temp = minX;
                    minX = maxX;
                    maxX = temp;
                }
                if (minY < maxY) {
                    int temp = minY;
                    minY = maxY;
                    maxY = temp;
                }
                int totalX = this.getX() + this.width;
                if (maxX > totalX) {
                    maxX = totalX;
                }
                if (minX > totalX) {
                    minX = totalX;
                }
                graphics.fill(RenderType.guiTextHighlight(), minX, minY, maxX, maxY, 0xff0000ff);
            }
        }
    }

    private void drawHighlighted(int line, int startX, int endX) {
        //TODO
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActive() || !isFocused()) return false;
        return switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN -> {
                changeLine(1);
                highlightY = cursorY;
                yield true;
            }
            case GLFW.GLFW_KEY_UP -> {
                changeLine(-1);
                highlightY = cursorY;
                yield true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                cursorX = Math.max(0, cursorX - 1);
                highlightX = cursorX;
                yield true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                cursorX = Math.min(cursorX + 1, texts[cursorY].length() - 1);
                highlightX = cursorX;
                yield true;
            }
            default -> super.keyPressed(keyCode, scanCode, modifiers);
        };
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    private int calculateEffectiveMaxLines() {
        int height = 0;
        for (int i = 0; i < MAX_LINES; i++) {
            height += sizes[i];
            if (height > MAX_HEIGHT) return i + 1;
        }
        return MAX_LINES;
    }
    
    private void changeLine(int direction) {
        cursorX = 0;
        cursorY = Mth.clamp(cursorY + direction, 0, MAX_HEIGHT);
        /*int oldWidth = font.width(texts[cursorY].substring(cursorX));
        int line = cursorY + direction;
        if (line == effectiveMaxLines) {
            line = 0;
        }
        if (line == -1) {
            line = effectiveMaxLines - 1;
        }
        int index = 0;
        int width = 0;
        for (; index < texts[line].length(); index++) {
            width += font.width(FormattedCharSequence.forward(String.valueOf(texts[line].charAt(index)), styles[line]));
            if (oldWidth <= width) break;
        }
        int newWidth = font.width(texts[line].substring(0, index - 1));
        if (Math.abs(newWidth - width) < Math.abs(oldWidth - width)) {
            index--;
        }
        cursorX = index;
        cursorY = line;*/
    }
}
