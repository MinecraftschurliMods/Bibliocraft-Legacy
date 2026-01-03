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
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClipboardScreen extends Screen {
    private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/clipboard.png");
    private final ItemStack stack;
    private final InteractionHand hand;
    private final CheckboxButton[] checkboxes = new CheckboxButton[ClipboardContent.MAX_LINES];
    private final EditBox[] lines = new EditBox[ClipboardContent.MAX_LINES];
    private ClipboardContent data;
    private EditBox titleBox;
    private PageButton forwardButton;
    private PageButton backButton;

    public ClipboardScreen(ItemStack stack, InteractionHand hand) {
        super(stack.getHoverName());
        this.stack = stack;
        this.hand = hand;
        this.data = stack.get(BCDataComponents.CLIPBOARD_CONTENT);
    }

    @Override
    public void onClose() {
        super.onClose();
        stack.set(BCDataComponents.CLIPBOARD_CONTENT, data);
        ClientPacketDistributor.sendToServer(new ClipboardSyncPacket(data, hand));
    }

    @Override
    protected void init() {
        int x = (width - 192) / 2;
        titleBox = addRenderableWidget(new EditBox(getMinecraft().font, x + 57, 14, 72, 8, Component.empty()));
        titleBox.setTextColor(0xff000000);
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
            lines[i].setTextColor(0xff000000);
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
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, (width - 192) / 2, 2, 0, 0, 192, 192, 256, 256);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY < 0 && forwardButton.visible) {
            forwardButton.onPress(new MouseButtonInfo(0, 0));
            return true;
        }
        if (scrollY > 0 && backButton.visible) {
            backButton.onPress(new MouseButtonInfo(0, 0));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) return true;
        return switch (event.key()) {
            case GLFW.GLFW_KEY_PAGE_UP -> {
                backButton.onPress(event);
                yield true;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN -> {
                forwardButton.onPress(event);
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

    @Override
    public boolean isInGameUi() {
        return true;
    }

    private static class CheckboxButton extends SpriteButton {
        private static final Identifier CHECK_TEXTURE = BCUtil.bcLoc("check");
        private static final Identifier X_TEXTURE = BCUtil.bcLoc("x");
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
        public void onClick(MouseButtonEvent event, boolean doubleClick) {
            state = switch (state) {
                case EMPTY -> CheckboxState.CHECK;
                case CHECK -> CheckboxState.X;
                case X -> CheckboxState.EMPTY;
            };
            super.onClick(event, doubleClick);
        }

        @Override
        @Nullable
        protected Identifier getSprite() {
            return switch (state) {
                case EMPTY -> null;
                case CHECK -> CHECK_TEXTURE;
                case X -> X_TEXTURE;
            };
        }
    }
}
