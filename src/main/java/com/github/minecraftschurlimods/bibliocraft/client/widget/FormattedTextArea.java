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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FormattedTextArea extends AbstractWidget {
    public static final int WIDTH = 140;
    public static final int HEIGHT = 80;
    private final Font font = Minecraft.getInstance().font;
    private final List<FormattedLine> lines;
    private int cursorX = 0;
    private int cursorY = 0;
    private int highlightX = 0;
    private long focusedTimestamp = Util.getMillis();
    private Consumer<FormattedLine> onLineChange;

    public FormattedTextArea(int x, int y, Component message, List<FormattedLine> lines) {
        super(x, y, WIDTH, HEIGHT, message);
        this.lines = new ArrayList<>(lines);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY() + 1;
        graphics.fill(x, y - 1, x + width, y + height, 0xffffffff); //TODO background
        for (int i = 0; i < lines.size(); i++) {
            renderLine(graphics, i, x, y);
            y += lines.get(i).size();
        }
    }

    private void renderLine(GuiGraphics graphics, int index, int x, int y) {
        FormattedLine line = lines.get(index);
        String text = line.text();
        Style style = line.style();
        int size = line.size();
        FormattedLine.Mode mode = line.mode();
        int color = 0xff000000 | (style.getColor() == null ? 0 : style.getColor().getValue());

        // is the current line selected?
        boolean cursorInLine = cursorY == index;
        // is the cursor not at the end?
        boolean cursorInText = cursorInLine && cursorX < text.length();
        boolean shouldRenderCursor = cursorInLine && isFocused() && (Util.getMillis() - focusedTimestamp) / 300L % 2 == 0;

        // draw the text
        int textX = x + 1;
        float scale = getScale(size);
        if (cursorInLine) {
            if (cursorInText) {
                int textWidth = drawText(graphics, format(text.substring(0, cursorX), style), textX, y, color, size, mode);
                drawText(graphics, format(text.substring(cursorX), style), textX + textWidth * scale, y, color, size, mode);
                if (shouldRenderCursor) {
                    graphics.fill(RenderType.guiOverlay(), textX + (int) ((textWidth - 1) * scale), y - 1, textX + (int) (textWidth * scale), (int) (y + 9 * scale + 1), color);
                }
            } else {
                int textWidth = drawText(graphics, format(text, style), textX, y, color, size, mode);
                if (shouldRenderCursor) {
                    drawText(graphics, format("_", style), textX + textWidth * scale, y, color, size, mode);
                }
            }
        } else {
            drawText(graphics, format(text, style), x + 1, y, color, size, mode);
        }
        
        // draw highlight
        if (cursorInLine && cursorX != highlightX) {
            int min = Math.min(cursorX, highlightX);
            int max = Math.max(cursorX, highlightX);
            int minWidth = font.width(format(text.substring(0, min), style));
            int maxWidth = font.width(format(text.substring(0, max), style));
            graphics.fill(RenderType.guiTextHighlight(), x + (int) (minWidth * scale), y - 1, x + (int) (maxWidth * scale), (int) (y + 9 * scale + 1), 0xff0000ff);
        }
    }

    private int drawText(GuiGraphics graphics, FormattedCharSequence text, float x, float y, int color, int size, FormattedLine.Mode mode) {
        float scale = getScale(size);
        int result;
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, 1);
        if (mode == FormattedLine.Mode.GLOWING) {
            int outlineColor = color == 0 ? 0xfff0ebcc : FastColor.ARGB32.color(255,
                    (int) ((double) FastColor.ARGB32.red(color) * 0.4),
                    (int) ((double) FastColor.ARGB32.green(color) * 0.4),
                    (int) ((double) FastColor.ARGB32.blue(color) * 0.4));
            font.drawInBatch8xOutline(text, 0, 0, color, outlineColor, pose.last().pose(), graphics.bufferSource(), LightTexture.FULL_BRIGHT);
            result = font.width(text);
        } else {
            result = graphics.drawString(font, text, 0, 0, color, mode == FormattedLine.Mode.SHADOW);
        }
        pose.popPose();
        return result;
    }

    private FormattedCharSequence format(String text, Style style) {
        return FormattedCharSequence.forward(text, style);
    }

    private float getScale(int size) {
        // scale the text, 8 is the default font size, and we subtract a padding of 1 on each side
        return (size - 2) / 8f;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActive() || !isFocused()) return false;
        FormattedLine line = lines.get(cursorY);
        String text = line.text();
        switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN:
                if (Screen.hasShiftDown()) {
                    moveCursor(text.length(), cursorY, true);
                } else if (cursorY < lines.size() - 1) {
                    moveCursor(getCursorXForNewLine(cursorY, cursorY + 1), cursorY + 1, false);
                }
                return true;
            case GLFW.GLFW_KEY_UP:
                if (Screen.hasShiftDown()) {
                    moveCursor(0, cursorY, true);
                } else if (cursorY > 0) {
                    moveCursor(getCursorXForNewLine(cursorY, cursorY - 1), cursorY - 1, false);
                }
                return true;
            case GLFW.GLFW_KEY_LEFT:
                moveCursor(Screen.hasControlDown() ? getWordPosition(-1) : Math.max(0, cursorX - 1), cursorY, Screen.hasShiftDown());
                return true;
            case GLFW.GLFW_KEY_RIGHT:
                moveCursor(Screen.hasControlDown() ? getWordPosition(1) : Math.min(text.length(), cursorX + 1), cursorY, Screen.hasShiftDown());
                return true;
            case GLFW.GLFW_KEY_BACKSPACE:
                if (highlightX != cursorX) {
                    deleteHighlight();
                } else if (cursorX > 0) {
                    int x = Screen.hasControlDown() ? getWordPosition(-1) : cursorX - 1;
                    lines.set(cursorY, line.withText(text.substring(0, x) + text.substring(cursorX)));
                    cursorX = x;
                }
                return true;
            case GLFW.GLFW_KEY_DELETE:
                if (highlightX != cursorX) {
                    deleteHighlight();
                } else if (cursorX < lines.size() - 1) {
                    int x = Screen.hasControlDown() ? getWordPosition(1) : cursorX + 1;
                    lines.set(cursorY, line.withText(text.substring(0, cursorX) + text.substring(x)));
                }
                return true;
            case GLFW.GLFW_KEY_HOME:
                moveCursor(0, cursorY, Screen.hasShiftDown());
                return true;
            case GLFW.GLFW_KEY_END:
                moveCursor(text.length(), cursorY, Screen.hasShiftDown());
                return true;
        }
        if (Screen.isSelectAll(keyCode)) {
            cursorX = 0;
            highlightX = text.length();
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(Math.min(cursorX, highlightX), Math.max(cursorX, highlightX)));
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        }
        if (Screen.isCut(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(text.substring(Math.min(cursorX, highlightX), Math.max(cursorX, highlightX)));
            deleteHighlight();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private int getWordPosition(int numWords) {
        String text = lines.get(cursorY).text();
        int x = cursorX;
        int abs = Math.abs(numWords);
        boolean reverse = numWords < 0;
        for (int i = 0; i < abs; i++) {
            if (!reverse) {
                int length = text.length();
                x = text.indexOf(' ', x);
                if (x == -1) {
                    x = length;
                } else {
                    while (x < length && text.charAt(x) == ' ') {
                        x++;
                    }
                }
            } else {
                while (x > 0 && text.charAt(x - 1) == ' ') {
                    x--;
                }
                while (x > 0 && text.charAt(x - 1) != ' ') {
                    x--;
                }
            }
        }
        return x;
    }

    private void moveCursor(int x, int y, boolean highlight) {
        int oldY = cursorY;
        cursorX = x;
        cursorY = y;
        if (y != oldY) {
            onLineChange.accept(lines.get(y));
        }
        if (!highlight) {
            highlightX = cursorX;
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!isActive() || !isFocused() || !StringUtil.isAllowedChatCharacter(codePoint)) return false;
        String oldText = lines.get(cursorY).text();
        return tryEdit(
                () -> insertText(Character.toString(codePoint)),
                () -> lines.set(cursorY, lines.get(cursorY).withText(oldText))
        );
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

    public void setOnLineChange(Consumer<FormattedLine> onLineChange) {
        this.onLineChange = onLineChange;
    }

    public List<FormattedLine> getLines() {
        return lines;
    }

    public boolean toggleStyle(Function<Style, Boolean> styleGetter, BiFunction<Style, Boolean, Style> styleSetter) {
        FormattedLine line = lines.get(cursorY);
        Style style = line.style();
        boolean oldValue = styleGetter.apply(style);
        return tryEdit(
                () -> lines.set(cursorY, line.withStyle(styleSetter.apply(style, !oldValue))),
                () -> lines.set(cursorY, line.withStyle(styleSetter.apply(style, oldValue)))
        );
    }

    public void setColor(int color) {
        FormattedLine line = lines.get(cursorY);
        lines.set(cursorY, line.withStyle(line.style().withColor(color)));
    }

    public void setSize(int size) {
        lines.set(cursorY, lines.get(cursorY).withSize(size));
    }

    public int getSize() {
        return lines.get(cursorY).size();
    }

    public void toggleMode() {
        FormattedLine line = lines.get(cursorY);
        lines.set(cursorY, line.withMode(switch (line.mode()) {
            case NORMAL -> FormattedLine.Mode.SHADOW;
            case SHADOW -> FormattedLine.Mode.GLOWING;
            case GLOWING -> FormattedLine.Mode.NORMAL;
        }));
    }

    public FormattedLine.Mode getMode() {
        return lines.get(cursorY).mode();
    }

    private boolean isValid() {
        return true; //TODO
    }

    private boolean tryEdit(Runnable edit, Runnable revert) {
        edit.run();
        if (isValid()) return true;
        revert.run();
        return false;
    }

    private void deleteHighlight() {
        FormattedLine line = lines.get(cursorY);
        lines.set(cursorY, line.withText(line.text().substring(0, Math.min(highlightX, cursorX) + 1) + line.text().substring(Math.max(highlightX, cursorX))));
        moveCursor(Math.min(highlightX, cursorX), cursorY, false);
    }

    private boolean insertText(String s) {
        String text = StringUtil.filterText(s);
        FormattedLine line = lines.get(cursorY);
        String oldText = line.text();
        if (!tryEdit(
                () -> {
                    deleteHighlight();
                    lines.set(cursorY, line.withText(line.text().substring(0, cursorX) + text + line.text().substring(cursorX)));
                },
                () -> lines.set(cursorY, line.withText(oldText))
        )) return false;
        cursorX += text.length();
        return true;
    }

    private int getCursorXForNewLine(int oldIndex, int newIndex) {
        FormattedLine oldLine = lines.get(oldIndex);
        FormattedLine newLine = lines.get(newIndex);
        float scale = (float) newLine.size() / oldLine.size();
        int targetWidth = font.width(format(oldLine.text().substring(0, cursorX), oldLine.style()));
        int index = 0, width = 0, prevWidth = -1;
        while (Math.abs(targetWidth - width) < Math.abs(targetWidth - prevWidth)) {
            if (index >= newLine.text().length()) return index;
            prevWidth = width;
            width += (int) (font.width(format(String.valueOf(newLine.text().charAt(index)), newLine.style())) * scale);
            index++;
        }
        return index - 1;
    }
}
