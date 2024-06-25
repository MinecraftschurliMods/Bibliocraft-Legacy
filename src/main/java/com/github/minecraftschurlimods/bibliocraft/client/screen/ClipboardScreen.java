package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.CheckboxState;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardItemSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ClipboardScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/clipboard.png");
    private final ItemStack stack;
    private ClipboardContent data;
    private final CheckboxButton[] checkboxes = new CheckboxButton[ClipboardContent.MAX_LINES];
    private final EditBox[] lines = new EditBox[ClipboardContent.MAX_LINES];
    private EditBox titleBox;
    private PageButton forwardButton;
    private PageButton backButton;

    public ClipboardScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.stack = stack;
        this.data = stack.get(BCDataComponents.CLIPBOARD);
    }

    @Override
    public void onClose() {
        super.onClose();
        close();
    }

    @Override
    protected void init() {
        int x = (width - 192) / 2;
        titleBox = addRenderableWidget(new NoShadowEditBox(x + 57, 14, 72, e -> data.setTitle(e)));
        for (int i = 0; i < ClipboardContent.MAX_LINES; i++) {
            final int j = i; // I love Java
            checkboxes[i] = addRenderableWidget(new CheckboxButton(x + 30, 15 * i + 26, e -> {
                List<ClipboardContent.Page> pages = new ArrayList<>(data.pages());
                ClipboardContent.Page page = pages.get(data.active());
                List<CheckboxState> checkboxes = new ArrayList<>(page.checkboxes());
                checkboxes.set(j, ((CheckboxButton) e).getState());
                pages.set(data.active(), page.setCheckboxes(checkboxes));
                data = data.setPages(pages);
            }));
            lines[i] = addRenderableWidget(new NoShadowEditBox(x + 45, 15 * i + 28, 109, e -> {
                List<ClipboardContent.Page> pages = new ArrayList<>(data.pages());
                ClipboardContent.Page page = pages.get(data.active());
                List<String> lines = new ArrayList<>(page.lines());
                lines.set(j, e);
                pages.set(data.active(), page.setLines(lines));
                data = data.setPages(pages);
            }));
        }
        forwardButton = addRenderableWidget(new PageButton(x + 116, 159, true, $ -> {
            data = data.nextPage();
            updateContents();
        }, false));
        backButton = addRenderableWidget(new PageButton(x + 43, 159, false, $ -> {
            data = data.prevPage();
            updateContents();
        }, false));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> {
            close();
            Objects.requireNonNull(minecraft).setScreen(null);
        }).bounds(width / 2 - 100, 196, 200, 20).build());
        updateContents();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, (width - 192) / 2, 2, 0, 0, 192, 192);
    }

    private void close() {
        stack.set(BCDataComponents.CLIPBOARD, data);
        PacketDistributor.sendToServer(new ClipboardItemSyncPacket(data));
    }

    private void updateContents() {
        backButton.visible = data.active() > 0;
        titleBox.setValue(data.title());
        ClipboardContent.Page page = data.pages().get(data.active());
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i].setState(page.checkboxes().get(i));
            lines[i].setValue(page.lines().get(i));
        }
    }

    // Necessary to allow rendering the text without a shadow.
    private static class NoShadowEditBox extends EditBox {
        private static final int TEXT_COLOR = 0x404040;

        public NoShadowEditBox(int x, int y, int width, Consumer<String> responder) {
            super(Minecraft.getInstance().font, x, y, width, 8, Component.empty());
            setResponder(responder);
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (!isVisible()) return;
            Font font = Minecraft.getInstance().font;
            int posOnScreen = getCursorPosition() - displayPos;
            String visibleText = font.plainSubstrByWidth(getValue().substring(displayPos), getInnerWidth());
            boolean cursorInside = posOnScreen >= 0 && posOnScreen <= visibleText.length();
            boolean shouldDrawCursor = isFocused() && cursorInside && (Util.getMillis() - focusedTime) / 300L % 2L == 0L;
            int x = getX();
            int y = getY();
            int width = x;
            int highlightStart = Mth.clamp(highlightPos - displayPos, 0, visibleText.length());
            if (!visibleText.isEmpty()) {
                width = graphics.drawString(font, formatter.apply(cursorInside ? visibleText.substring(0, posOnScreen) : visibleText, displayPos), x, y, TEXT_COLOR, false);
            }
            boolean eol = getCursorPosition() < getValue().length() || getValue().length() >= getMaxLength();
            int cursorX = width;
            if (!cursorInside) {
                cursorX = posOnScreen > 0 ? x + this.width : x;
            } else if (eol) {
                cursorX = width - 1;
                width--;
            }
            if (!visibleText.isEmpty() && cursorInside && posOnScreen < visibleText.length()) {
                graphics.drawString(font, formatter.apply(visibleText.substring(posOnScreen), getCursorPosition()), width, y, TEXT_COLOR, false);
            }
            if (shouldDrawCursor) {
                if (eol) {
                    graphics.fill(RenderType.guiOverlay(), cursorX, y - 1, cursorX + 1, y + 1 + 9, -3092272);
                } else {
                    graphics.drawString(font, "_", cursorX, y, TEXT_COLOR, false);
                }
            }
            if (highlightStart != posOnScreen) {
                int highlight = x + font.width(visibleText.substring(0, highlightStart));
                renderHighlight(graphics, cursorX, y - 1, highlight - 1, y + 1 + 9);
            }
        }
    }

    private static class CheckboxButton extends Button {
        private static final ResourceLocation CHECK_TEXTURE = BCUtil.modLoc("check");
        private static final ResourceLocation X_TEXTURE = BCUtil.modLoc("x");
        private CheckboxState state = CheckboxState.EMPTY;

        public CheckboxButton(int x, int y, OnPress onPress) {
            super(new Builder(Component.empty(), onPress).bounds(x, y, 14, 14));
        }

        public CheckboxState getState() {
            return state;
        }

        public void setState(CheckboxState state) {
            this.state = state;
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            state = switch (state) {
                case EMPTY -> CheckboxState.CHECK;
                case CHECK -> CheckboxState.X;
                case X -> CheckboxState.EMPTY;
            };
            super.onClick(mouseX, mouseY, button);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (state == CheckboxState.CHECK) {
                graphics.blitSprite(CHECK_TEXTURE, getX(), getY(), getWidth(), getHeight());
            } else if (state == CheckboxState.X) {
                graphics.blitSprite(X_TEXTURE, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
