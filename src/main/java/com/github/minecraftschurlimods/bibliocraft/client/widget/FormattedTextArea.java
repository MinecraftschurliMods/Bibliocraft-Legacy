package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FormattedTextArea extends AbstractWidget {
    private final Font font = ClientUtil.getFont();
    private final List<FormattedLine> lines;
    private int cursorX = 0;
    private int cursorY = 0;
    private int highlightX = 0;
    private long focusedTimestamp = Util.getMillis();
    private Consumer<FormattedLine> onLineChange;

    public FormattedTextArea(int x, int y, int width, int height, List<FormattedLine> lines) {
        super(x, y, width, height, Component.empty());
        this.lines = new ArrayList<>(lines);
    }

    public static void renderLines(List<FormattedLine> lines, PoseStack stack, MultiBufferSource bufferSource, int x, int y, int width, int height) {
        int i = y;
        for (FormattedLine line : lines) {
            renderLine(line, stack, bufferSource, x, i, width, height);
            i += line.size();
        }
    }

    public static void renderLine(FormattedLine line, PoseStack poseStack, MultiBufferSource bufferSource, int x, int y, int width, int height, int cursor, DrawCursor drawCursor) {
        String text = line.text();
        Style style = line.style();
        int size = line.size();
        FormattedLine.Mode mode = line.mode();
        int color = 0xff000000 | (style.getColor() == null ? 0 : style.getColor().getValue());
        float scale = getScale(size);
        int textX = x + getLineLeftX(line, scale, width);
        FormattedCharSequence formattedText = format(text, style);
        drawText(poseStack, bufferSource, formattedText, textX, y, color, size, mode);
        Font font = ClientUtil.getFont();
        if (drawCursor == DrawCursor.VERTICAL) {
            int textWidth = font.width(format(text.substring(0, cursor), style));
            fill(poseStack, bufferSource, RenderType.guiOverlay(), textX + (int) ((textWidth - 1) * scale), y - 1, textX + (int) (textWidth * scale), (int) (y + 9 * scale + 1), color);
        } else if (drawCursor == DrawCursor.HORIZONTAL) {
            drawText(poseStack, bufferSource, format("_", style), textX + font.width(formattedText) * scale, y, color, size, mode);
        }
    }

    public static void renderLine(FormattedLine line, PoseStack poseStack, MultiBufferSource bufferSource, int x, int y, int width, int height) {
        renderLine(line, poseStack, bufferSource, x, y, width, height, 0, DrawCursor.NONE);
    }

    /**
     * Static version of {@link GuiGraphics#fill(RenderType, int, int, int, int, int)}.
     */
    private static void fill(PoseStack stack, MultiBufferSource bufferSource, RenderType renderType, float minX, float minY, float maxX, float maxY, int color) {
        Matrix4f matrix4f = stack.last().pose();
        if (minX < maxX) {
            float x = minX;
            minX = maxX;
            maxX = x;
        }
        if (minY < maxY) {
            float y = minY;
            minY = maxY;
            maxY = y;
        }
        VertexConsumer vc = bufferSource.getBuffer(renderType);
        vc.addVertex(matrix4f, minX, minY, 0).setColor(color);
        vc.addVertex(matrix4f, minX, maxY, 0).setColor(color);
        vc.addVertex(matrix4f, maxX, maxY, 0).setColor(color);
        vc.addVertex(matrix4f, maxX, minY, 0).setColor(color);
        if (bufferSource instanceof MultiBufferSource.BufferSource guiBuffer) {
            RenderSystem.disableDepthTest();
            guiBuffer.endBatch();
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY() + 1;
        for (int i = 0; i < lines.size(); i++) {
            renderLine(graphics, i, x, y);
            y += lines.get(i).size();
        }
    }

    private void renderLine(GuiGraphics graphics, int index, int x, int y) {
        FormattedLine line = lines.get(index);
        String text = line.text();
        boolean cursorBlink = (Util.getMillis() - focusedTimestamp) / 300L % 2 == 0;
        DrawCursor draw = !isFocused()
                ? DrawCursor.NONE
                : cursorY == index && cursorX < text.length()
                ? DrawCursor.VERTICAL
                : cursorY == index
                ? DrawCursor.HORIZONTAL
                : DrawCursor.NONE;
        renderLine(line, graphics.pose(), graphics.bufferSource(), x, y, width, height, cursorX, cursorBlink ? DrawCursor.NONE : draw);
        if (draw != DrawCursor.NONE && cursorX != highlightX) {
            int min = Math.clamp(Math.min(cursorX, highlightX), 0, text.length());
            int max = Math.clamp(Math.max(cursorX, highlightX), 0, text.length());
            Style style = line.style();
            float scale = getScale(line.size());
            int textX = x + getLineLeftX(line, scale, width);
            int minWidth = (int) (font.width(format(text.substring(0, min), style)) * scale);
            int maxWidth = (int) (font.width(format(text.substring(0, max), style)) * scale);
            graphics.fill(RenderType.guiTextHighlight(), textX + minWidth - 1, y - 1, textX + maxWidth - 1, (int) (y + 9 * scale + 1), 0xff0000ff);
        }
    }

    private static void drawText(PoseStack poseStack, MultiBufferSource bufferSource, FormattedCharSequence text, float x, float y, int color, int size, FormattedLine.Mode mode) {
        Font font = ClientUtil.getFont();
        float scale = getScale(size);
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(scale, scale, 1);
        if (mode == FormattedLine.Mode.GLOWING) {
            int outlineColor = color == 0 ? 0xfff0ebcc : FastColor.ARGB32.color(255,
                    (int) ((double) FastColor.ARGB32.red(color) * 0.4),
                    (int) ((double) FastColor.ARGB32.green(color) * 0.4),
                    (int) ((double) FastColor.ARGB32.blue(color) * 0.4));
            font.drawInBatch8xOutline(text, 0, 0, color, outlineColor, poseStack.last().pose(), bufferSource, LightTexture.FULL_BRIGHT);
        } else {
            font.drawInBatch(text, 0, 0, color, mode == FormattedLine.Mode.SHADOW, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        }
        poseStack.popPose();
    }

    private static FormattedCharSequence format(String text, Style style) {
        return FormattedCharSequence.forward(text, style);
    }

    private static float getScale(int size) {
        // scale the text, 8 is the default font size, and we subtract a padding of 1 on each side
        return (size - 2) / 8f;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActive() || !isFocused()) return false;
        FormattedLine line = lines.get(cursorY);
        String text = line.text();
        int min = Math.clamp(Math.min(cursorX, highlightX), 0, text.length());
        int max = Math.clamp(Math.max(cursorX, highlightX), 0, text.length());
        switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER:
                if (Screen.hasShiftDown()) {
                    moveCursor(text.length(), cursorY, true);
                } else if (cursorY < getEffectiveMaxLines()) {
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
                    moveCursor(x, cursorY, false);
                }
                return true;
            case GLFW.GLFW_KEY_DELETE:
                if (highlightX != cursorX) {
                    deleteHighlight();
                } else if (cursorX < lines.get(cursorY).text().length()) {
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
            cursorX = text.length();
            highlightX = 0;
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            ClientUtil.getMc().keyboardHandler.setClipboard(text.substring(min, max));
            return true;
        }
        if (Screen.isPaste(keyCode) || keyCode == GLFW.GLFW_KEY_INSERT) {
            insertText(ClientUtil.getMc().keyboardHandler.getClipboard());
            return true;
        }
        if (Screen.isCut(keyCode)) {
            ClientUtil.getMc().keyboardHandler.setClipboard(text.substring(min, max));
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return super.mouseClicked(mouseX, mouseY, button);
        mouseX -= getX();
        mouseY -= getY();
        if (!Screen.hasShiftDown() || !isFocused()) {
            cursorY = lines.size() - 1;
            int y = 0;
            for (int i = 0; i < lines.size(); i++) {
                y += lines.get(i).size();
                if (y > height) {
                    cursorY = i - 1;
                    break;
                }
                if (y > mouseY) {
                    cursorY = i;
                    break;
                }
            }
        }
        FormattedLine line = lines.get(cursorY);
        onLineChange.accept(line);
        float scale = getScale(line.size());
        int startX = getLineLeftX(line, scale, width);
        int targetWidth = (int) (mouseX - startX);
        int index = 0, width = 0, prevWidth = -1;
        while (Math.abs(targetWidth - width) < Math.abs(targetWidth - prevWidth)) {
            if (index >= line.text().length()) break;
            prevWidth = width;
            width += (int) (font.width(format(String.valueOf(line.text().charAt(index)), line.style())) * scale);
            index++;
        }
        cursorX = Mth.clamp(index, 0, line.text().length());
        if (!Screen.hasShiftDown() && isFocused()) {
            highlightX = cursorX;
        }
        setFocused(true);
        return true;
    }

    public void setOnLineChange(Consumer<FormattedLine> onLineChange) {
        this.onLineChange = onLineChange;
    }

    public List<FormattedLine> getLines() {
        return lines;
    }

    public void toggleStyle(Function<Style, Boolean> styleGetter, BiFunction<Style, Boolean, Style> styleSetter) {
        FormattedLine line = lines.get(cursorY);
        Style style = line.style();
        boolean oldValue = styleGetter.apply(style);
        tryEdit(
                () -> lines.set(cursorY, line.withStyle(styleSetter.apply(style, !oldValue))),
                () -> lines.set(cursorY, line.withStyle(styleSetter.apply(style, oldValue)))
        );
    }

    public void setColor(int color) {
        FormattedLine line = lines.get(cursorY);
        lines.set(cursorY, line.withStyle(line.style().withColor(color)));
    }

    public void setSize(int size) {
        int oldValue = lines.get(cursorY).size();
        tryEdit(
                () -> lines.set(cursorY, lines.get(cursorY).withSize(size)),
                () -> lines.set(cursorY, lines.get(cursorY).withSize(oldValue))
        );
    }

    public int getSize() {
        return lines.get(cursorY).size();
    }

    public void toggleAlignment() {
        FormattedLine line = lines.get(cursorY);
        FormattedLine.Alignment oldValue = line.alignment();
        tryEdit(
                () -> lines.set(cursorY, line.withAlignment(switch (oldValue) {
                    case LEFT -> FormattedLine.Alignment.CENTER;
                    case CENTER -> FormattedLine.Alignment.RIGHT;
                    case RIGHT -> FormattedLine.Alignment.LEFT;
                })),
                () -> lines.set(cursorY, line.withAlignment(oldValue))
        );
    }

    public FormattedLine.Alignment getAlignment() {
        return lines.get(cursorY).alignment();
    }

    public void toggleMode() {
        FormattedLine line = lines.get(cursorY);
        FormattedLine.Mode oldValue = line.mode();
        tryEdit(
                () -> lines.set(cursorY, line.withMode(switch (oldValue) {
                    case NORMAL -> FormattedLine.Mode.SHADOW;
                    case SHADOW -> FormattedLine.Mode.GLOWING;
                    case GLOWING -> FormattedLine.Mode.NORMAL;
                })),
                () -> lines.set(cursorY, line.withMode(oldValue))
        );
    }

    public FormattedLine.Mode getMode() {
        return lines.get(cursorY).mode();
    }

    private boolean isValid() {
        int y = 0;
        for (FormattedLine line : lines) {
            int size = line.size();
            y += size;
            if (line.text().isEmpty()) continue;
            if (y > height) return false;
            float textWidth = font.width(format(line.text(), line.style())) * getScale(size);
            if (textWidth > width - 2) return false;
        }
        return true;
    }

    private boolean tryEdit(Runnable edit, Runnable revert) {
        edit.run();
        if (isValid()) return true;
        revert.run();
        return false;
    }

    private void deleteHighlight() {
        if (highlightX == cursorX) return;
        FormattedLine line = lines.get(cursorY);
        String text = line.text();
        int min = Math.clamp(Math.min(cursorX, highlightX), 0, text.length());
        int max = Math.clamp(Math.max(cursorX, highlightX), 0, text.length());
        lines.set(cursorY, line.withText(text.substring(0, min) + text.substring(max)));
        moveCursor(min, cursorY, false);
    }

    private void insertText(String s) {
        String text = StringUtil.filterText(s);
        String oldText = lines.get(cursorY).text();
        int oldHighlight = highlightX;
        int oldCursor = cursorX;
        if (!tryEdit(
                () -> {
                    deleteHighlight();
                    FormattedLine line = lines.get(cursorY);
                    lines.set(cursorY, line.withText(line.text().substring(0, cursorX) + text + line.text().substring(cursorX)));
                },
                () -> {
                    lines.set(cursorY, lines.get(cursorY).withText(oldText));
                    highlightX = oldHighlight;
                    cursorX = oldCursor;
                }
        )) return;
        cursorX += text.length();
        highlightX = cursorX;
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

    private static int getLineLeftX(FormattedLine line, float scale, int width) {
        int textWidth = (int) (ClientUtil.getFont().width(format(line.text(), line.style())) * scale);
        return switch (line.alignment()) {
            case LEFT -> 1;
            case CENTER -> width / 2 - textWidth / 2;
            case RIGHT -> width - 1 - textWidth;
        };
    }

    private static int getLineRightX(FormattedLine line, float scale, int width) {
        int textWidth = (int) (ClientUtil.getFont().width(format(line.text(), line.style())) * scale);
        return switch (line.alignment()) {
            case LEFT -> 1 + textWidth;
            case CENTER -> width / 2 + textWidth / 2;
            case RIGHT -> width - 1;
        };
    }

    private int getEffectiveMaxLines() {
        int size = 0;
        for (int i = 0; i < lines.size(); i++) {
            FormattedLine line = lines.get(i);
            size += line.size();
            if (size > height) return i - 1;
        }
        return lines.size() - 1;
    }

    public enum DrawCursor {
        NONE, VERTICAL, HORIZONTAL
    }
}
