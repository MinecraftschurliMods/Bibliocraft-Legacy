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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.MutableDataComponentHolder;

import java.util.Objects;
import java.util.function.Consumer;

public class ClipboardScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/clipboard.png");
    private final CheckboxButton[] checkboxes = new CheckboxButton[ClipboardContent.ClipboardPage.LINES];
    private final EditBox[] lines = new EditBox[ClipboardContent.ClipboardPage.LINES];
    private final PatchedDataComponentMap components;
    private final Consumer<DataComponentPatch> onClose;
    private EditBox titleBox;
    private PageButton forwardButton;
    private PageButton backButton;

    public ClipboardScreen(DataComponentMap initial, Consumer<DataComponentPatch> onClose) {
        super(initial.getOrDefault(DataComponents.ITEM_NAME, Component.empty()));
        this.components = new PatchedDataComponentMap(initial);
        this.onClose = onClose;
    }

    public ClipboardScreen(MutableDataComponentHolder holder) {
        this(holder.getComponents(), patch -> {
            holder.applyComponents(patch);
            ClipboardItemSyncPacket.sync(patch);
        });
    }

    @Override
    public void onClose() {
        super.onClose();
        close();
    }

    public void close() {
        this.onClose.accept(this.components.asPatch());
    }

    @Override
    protected void init() {
        int x = (this.width - 192) / 2;
        this.titleBox = addRenderableWidget(new NoShadowEditBox(x + 57, 14, 72, this::setTitle));
        for (int i = 0; i < ClipboardContent.ClipboardPage.LINES; i++) {
            final int j = i; // I love Java
            this.checkboxes[i] = addRenderableWidget(new CheckboxButton(x + 30, 15 * i + 26, e -> setState(j, e)));
            this.lines[i] = addRenderableWidget(new NoShadowEditBox(x + 45, 15 * i + 28, 109, e -> setText(j, e)));
        }
        this.forwardButton = addRenderableWidget(new PageButton(x + 116, 159, true, $ -> {
            nextPage();
            updateContents();
        }, false));
        this.backButton = addRenderableWidget(new PageButton(x + 43, 159, false, $ -> {
            prevPage();
            updateContents();
        }, false));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> {
            close();
            Objects.requireNonNull(this.minecraft).setScreen(null);
        }).bounds(this.width / 2 - 100, 196, 200, 20).build());
        updateContents();
    }

    private void setTitle(String title) {
        this.components.set(BCDataComponents.CLIPBOARD_CONTENT.get(), getClipboardContent().setTitle(title));
    }

    private void setState(int lineIndex, CheckboxState state) {
        this.components.set(BCDataComponents.CLIPBOARD_CONTENT.get(), getClipboardContent().setState(getActivePageIndex(), lineIndex, state));
    }

    private void setText(int lineIndex, String text) {
        this.components.set(BCDataComponents.CLIPBOARD_CONTENT.get(), getClipboardContent().setText(getActivePageIndex(), lineIndex, text));
    }

    private void prevPage() {
        this.components.set(BCDataComponents.CLIPBOARD_ACTIVE_PAGE.get(), Mth.clamp(getActivePageIndex() - 1, 0, ClipboardContent.MAX_PAGES));
    }

    private void nextPage() {
        this.components.set(BCDataComponents.CLIPBOARD_ACTIVE_PAGE.get(), Mth.clamp(getActivePageIndex() + 1, 0, ClipboardContent.MAX_PAGES));
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, (this.width - 192) / 2, 2, 0, 0, 192, 192);
    }

    private void updateContents() {
        this.backButton.visible = getActivePageIndex() > 0;
        this.forwardButton.visible = getActivePageIndex() < ClipboardContent.MAX_PAGES;
        this.titleBox.setValue(getClipboardTitle());
        ClipboardContent.ClipboardPage page = getActivePage();
        for (int i = 0; i < this.checkboxes.length; i++) {
            this.checkboxes[i].setState(page.getState(i));
            this.lines[i].setValue(page.getText(i));
        }
    }

    private ClipboardContent.ClipboardPage getActivePage() {
        return getClipboardContent().getPage(getActivePageIndex());
    }

    private String getClipboardTitle() {
        return getClipboardContent().title();
    }

    private int getActivePageIndex() {
        return this.components.getOrDefault(BCDataComponents.CLIPBOARD_ACTIVE_PAGE.get(), 0);
    }

    private ClipboardContent getClipboardContent() {
        return this.components.getOrDefault(BCDataComponents.CLIPBOARD_CONTENT.get(), ClipboardContent.EMPTY);
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
            int posOnScreen = getCursorPosition() - this.displayPos;
            String visibleText = font.plainSubstrByWidth(getValue().substring(this.displayPos), getInnerWidth());
            boolean cursorInside = posOnScreen >= 0 && posOnScreen <= visibleText.length();
            boolean shouldDrawCursor = isFocused() && cursorInside && (Util.getMillis() - focusedTime) / 300L % 2L == 0L;
            int x = getX();
            int y = getY();
            int width = x;
            int highlightStart = Mth.clamp(this.highlightPos - this.displayPos, 0, visibleText.length());
            if (!visibleText.isEmpty()) {
                width = graphics.drawString(font, this.formatter.apply(cursorInside ? visibleText.substring(0, posOnScreen) : visibleText, this.displayPos), x, y, TEXT_COLOR, false);
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
                graphics.drawString(font, this.formatter.apply(visibleText.substring(posOnScreen), getCursorPosition()), width, y, TEXT_COLOR, false);
            }
            if (shouldDrawCursor) {
                if (eol) {
                    graphics.fill(RenderType.guiOverlay(), cursorX, y - 1, cursorX + 1, y + 1 + 9, 0xffd0d0d0);
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

        public CheckboxButton(int x, int y, Consumer<CheckboxState> onPress) {
            super(new Builder(Component.empty(), e -> onPress.accept(((CheckboxButton) e).getState())).bounds(x, y, 14, 14));
        }

        public CheckboxState getState() {
            return this.state;
        }

        public void setState(CheckboxState state) {
            this.state = state;
        }

        @Override
        public void onClick(double mouseX, double mouseY, int button) {
            this.state = this.state.next();
            super.onClick(mouseX, mouseY, button);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (this.state == CheckboxState.CHECK) {
                graphics.blitSprite(CHECK_TEXTURE, getX(), getY(), getWidth(), getHeight());
            } else if (this.state == CheckboxState.CROSS) {
                graphics.blitSprite(X_TEXTURE, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}
