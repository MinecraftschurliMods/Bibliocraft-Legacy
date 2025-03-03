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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        boolean shadow = line.shadow();
        int color = 0xff000000 | (style.getColor() == null ? 0 : style.getColor().getValue());
        if (text == null || text.isEmpty()) {
            if (cursorY == index && isFocused() && (Util.getMillis() - focusedTimestamp) / 300L % 2 == 0) {
                graphics.fill(RenderType.guiOverlay(), x, y - 1, x + 1, y + 10, color);
            }
            return;
        }

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
                    graphics.fill(RenderType.guiOverlay(), textWidth - 1, y - 1, textWidth, y + 10, color);
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
        switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN: //TODO selection, ctrl
                cursorY = Math.clamp(cursorY + 1, 0, lines.size() - 1);
                cursorX = Math.min(cursorX, lines.get(cursorY).text().length());
                return true;
            case GLFW.GLFW_KEY_UP: //TODO selection, ctrl
                cursorY = Math.clamp(cursorY - 1, 0, lines.size() - 1);
                cursorX = Math.min(cursorX, lines.get(cursorY).text().length());
                return true;
            case GLFW.GLFW_KEY_LEFT: //TODO selection, ctrl
                cursorX = Math.max(0, cursorX - 1);
                return true;
            case GLFW.GLFW_KEY_RIGHT: //TODO selection, ctrl
                cursorX = Math.min(cursorX + 1, lines.get(cursorY).text().length());
                return true;
            case GLFW.GLFW_KEY_BACKSPACE: //TODO ctrl
                if (cursorX > 0) {
                    cursorX--;
                    lines.set(cursorY, lines.get(cursorY).withText(lines.get(cursorY).text().substring(0, cursorX) + lines.get(cursorY).text().substring(cursorX + 1)));
                }
                return true;
            case GLFW.GLFW_KEY_DELETE: //TODO ctrl
                if (cursorX < lines.size() - 1) {
                    lines.set(cursorY, lines.get(cursorY).withText(lines.get(cursorY).text().substring(0, cursorX) + lines.get(cursorY).text().substring(cursorX + 1)));
                }
                return true;
            case GLFW.GLFW_KEY_HOME: //TODO selection
                cursorX = 0;
                return true;
            case GLFW.GLFW_KEY_END: //TODO selection
                cursorX = lines.get(cursorY).text().length();
                return true;
        }
        if (Screen.isSelectAll(keyCode)) {
            //TODO
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            //TODO
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            //TODO
            return true;
        }
        if (Screen.isCut(keyCode)) {
            //TODO
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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
        if (!isActive() || !isFocused() || !StringUtil.isAllowedChatCharacter(codePoint)) return false;
        String oldText = lines.get(cursorY).text();
        return tryEdit(
                () -> insertText(Character.toString(codePoint)),
                () -> lines.set(cursorY, lines.get(cursorY).withText(oldText))
        );
    }

    private boolean tryEdit(Runnable edit, Runnable revert) {
        edit.run();
        if (isValid()) return true;
        revert.run();
        return false;
    }

    private boolean isValid() {
        return true; //TODO
    }

    private boolean insertText(String text) {
        FormattedLine line = lines.get(cursorY);
        String oldText = line.text();
        if (!tryEdit(
                () -> lines.set(cursorY, line.withText(line.text().substring(0, cursorX) + text + line.text().substring(cursorX))),
                () -> lines.set(cursorY, line.withText(oldText))
        )) return false;
        cursorX += text.length();
        return true;
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
}
