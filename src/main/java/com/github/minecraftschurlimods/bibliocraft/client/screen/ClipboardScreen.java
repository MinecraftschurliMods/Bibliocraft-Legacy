package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.SpriteButton;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.CheckboxState;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

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
        this.data = stack.get(BCDataComponents.CLIPBOARD_CONTENT);
    }

    @Override
    public void onClose() {
        super.onClose();
        stack.set(BCDataComponents.CLIPBOARD_CONTENT, data);
        PacketDistributor.sendToServer(new ClipboardSyncPacket(data));
    }

    @Override
    protected void init() {
        int x = (width - 192) / 2;
        titleBox = addRenderableWidget(new EditBox(getMinecraft().font, x + 57, 14, 72, 8, Component.empty()));
        titleBox.setTextColor(0);
        titleBox.setBordered(false);
        titleBox.setTextShadow(false);
        titleBox.setResponder(e -> data = data.setTitle(e));
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
            lines[i] = addRenderableWidget(new EditBox(getMinecraft().font, x + 45, 15 * i + 28, 109, 8, Component.empty()));
            lines[i].setTextColor(0);
            lines[i].setBordered(false);
            lines[i].setTextShadow(false);
            lines[i].setResponder(e -> {
                List<ClipboardContent.Page> pages = new ArrayList<>(data.pages());
                ClipboardContent.Page page = pages.get(data.active());
                List<String> lines = new ArrayList<>(page.lines());
                lines.set(j, e);
                pages.set(data.active(), page.setLines(lines));
                data = data.setPages(pages);
            });
        }
        forwardButton = addRenderableWidget(new PageButton(x + 116, 159, true, $ -> {
            data = data.nextPage();
            updateContents();
        }, false));
        backButton = addRenderableWidget(new PageButton(x + 43, 159, false, $ -> {
            data = data.prevPage();
            updateContents();
        }, false));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 - 100, 196, 200, 20).build());
        updateContents();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, (width - 192) / 2, 2, 0, 0, 192, 192);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY < 0 && forwardButton.visible) {
            forwardButton.onPress();
            return true;
        }
        if (scrollY > 0 && backButton.visible) {
            backButton.onPress();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) return true;
        return switch (keyCode) {
            case GLFW.GLFW_KEY_PAGE_UP -> {
                backButton.onPress();
                yield true;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN -> {
                forwardButton.onPress();
                yield true;
            }
            default -> false;
        };
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

    private static class CheckboxButton extends SpriteButton {
        private static final ResourceLocation CHECK_TEXTURE = BCUtil.modLoc("check");
        private static final ResourceLocation X_TEXTURE = BCUtil.modLoc("x");
        private CheckboxState state = CheckboxState.EMPTY;

        public CheckboxButton(int x, int y, OnPress onPress) {
            super(x, y, 14, 14, onPress);
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
        @Nullable
        protected ResourceLocation getSprite() {
            return switch (state) {
                case EMPTY -> null;
                case CHECK -> CHECK_TEXTURE;
                case X -> X_TEXTURE;
            };
        }
    }
}
