package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ColorButton;
import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookContent;
import com.github.minecraftschurlimods.bibliocraft.content.bigbook.BigBookSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

public class BigBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/big_book.png");
    private static final Component SIGN_BUTTON = Component.translatable("book.signButton");
    private static final int BACKGROUND_WIDTH = 220;
    private static final int BACKGROUND_HEIGHT = 256;
    private static final int TEXT_WIDTH = 188;
    private static final int TEXT_HEIGHT = 204;
    private final ItemStack stack;
    private final boolean writable;
    private final List<List<FormattedLine>> pages;
    private int currentPage = 0;
    private FormattedTextArea textArea;
    private boolean isSigning = false;
    private Button modeButton;
    private Button alignmentButton;
    private EditBox colorBox;
    private EditBox sizeBox;
    private Button scaleDownButton;
    private Button scaleUpButton;
    private PageButton backButton;
    private PageButton forwardButton;

    public BigBookScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.stack = stack;
        if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
            pages = new ArrayList<>(Objects.requireNonNull(stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)).pages());
            writable = false;
        } else if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
            pages = new ArrayList<>(Objects.requireNonNull(stack.get(BCDataComponents.BIG_BOOK_CONTENT)).pages());
            writable = true;
        } else {
            pages = new ArrayList<>();
            writable = true;
        }
        if (pages.isEmpty()) {
            pages.add(new ArrayList<>());
            pages.getFirst().add(FormattedLine.DEFAULT);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void init() {
        if (writable) {
            int leftX = (width - BACKGROUND_WIDTH - 80) / 2;
            int rightX = (width + BACKGROUND_WIDTH - 80) / 2;
            textArea = addRenderableWidget(new FormattedTextArea(rightX + 16, 26, TEXT_WIDTH, TEXT_HEIGHT, pages.get(currentPage)));
            textArea.setOnLineChange(this::onLineChange);

            // Color buttons and text box
            ChatFormatting[] colors = BCUtil.getChatFormattingColors().toArray(ChatFormatting[]::new);
            int colorRows = Math.floorDiv(colors.length, 4);
            colorBox = addRenderableWidget(new EditBox(font, rightX + 16, 80 + 16 * colorRows, 64, 16, Component.empty()));
            colorBox.setHint(Translations.FANCY_TEXT_AREA_COLOR_HINT);
            colorBox.setMaxLength(7);
            colorBox.setFilter(s -> s.isEmpty() || s.charAt(0) == '#' && s.substring(1).codePoints().allMatch(HexFormat::isHexDigit));
            colorBox.setResponder(s -> {
                if (s.length() <= 1) return;
                textArea.setColor(Integer.parseInt(s.substring(1), 16));
            });
            TextColor color = textArea.getLines().getFirst().style().getColor();
            if (color != null) {
                setColor(color.getValue());
            }
            for (int i = 0; i < colors.length; i++) {
                final int j = i; // I love Java
                ColorButton button = addRenderableWidget(new ColorButton(colors[i].getColor(), Button.builder(Component.translatable("color." + colors[i].getName()), $ -> setColor(colors[j].getColor()))
                        .bounds(rightX + 80 - 16 * (4 - i % 4), 80 + 16 * Math.floorDiv(i, 4), 16, 16)));
                button.setTooltip(Tooltip.create(Component.translatable("color." + colors[i].getName())));
            }

            // Formatting buttons
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_BOLD_SHORT, $ -> textArea.toggleStyle(Style::isBold, Style::withBold))
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_BOLD))
                    .bounds(rightX + 16, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_ITALIC_SHORT, $ -> textArea.toggleStyle(Style::isItalic, Style::withItalic))
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ITALIC))
                    .bounds(rightX + 32, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_UNDERLINED_SHORT, $ -> textArea.toggleStyle(Style::isUnderlined, Style::withUnderlined))
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_UNDERLINED))
                    .bounds(rightX + 48, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_SHORT, $ -> textArea.toggleStyle(Style::isStrikethrough, Style::withStrikethrough))
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_STRIKETHROUGH))
                    .bounds(rightX + 64, 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_OBFUSCATED_SHORT, $ -> textArea.toggleStyle(Style::isObfuscated, Style::withObfuscated))
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_OBFUSCATED))
                    .bounds(rightX + 16, 32, 16, 16)
                    .build());
            modeButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getMode().getTranslationKey()), button -> {
                        textArea.toggleMode();
                        updateModeButton();
                    })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_MODE))
                    .bounds(rightX + 32, 32, 48, 16)
                    .build());
            alignmentButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getAlignment().getTranslationKey()), button -> {
                        textArea.toggleAlignment();
                        updateAlignmentButton();
                    })
                    .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ALIGNMENT))
                    .bounds(rightX + 16, 48, 64, 16)
                    .build());

            // Size buttons and text box
            sizeBox = addRenderableWidget(new EditBox(font, rightX + 32, 112 + 16 * colorRows, 32, 16, Component.empty()));
            sizeBox.setFilter(s -> {
                try {
                    int i = Integer.parseInt(s);
                    return i >= FormattedLine.MIN_SIZE && i <= FormattedLine.MAX_SIZE;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
            sizeBox.setResponder(s -> {
                try {
                    textArea.setSize(Integer.parseInt(s));
                } catch (NumberFormatException ignored) {}
            });
            scaleDownButton = addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_SCALE_DOWN, button -> {
                int size = textArea.getSize() - 1;
                sizeBox.setValue(String.valueOf(size));
                // call again to account for invalid values
                sizeBox.setValue(String.valueOf(textArea.getSize()));
                updateSizeButtons(size);
            }).bounds(rightX + 16, 112 + 16 * colorRows, 16, 16).tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP)).build());
            scaleUpButton = addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_SCALE_UP, button -> {
                int size = textArea.getSize() + 1;
                sizeBox.setValue(String.valueOf(size));
                // call again to account for invalid values
                sizeBox.setValue(String.valueOf(textArea.getSize()));
                updateSizeButtons(size);
            }).bounds(rightX + 64, 112 + 16 * colorRows, 16, 16).tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_UP_TOOLTIP)).build());
            onLineChange(textArea.getLines().getFirst());

            backButton = addRenderableWidget(new PageButton(leftX + 43, BACKGROUND_HEIGHT - 32, false, $ -> {
                if (currentPage > 0) {
                    currentPage--;
                }
                updateButtonVisibility();
            }, true));
            forwardButton = addRenderableWidget(new PageButton(leftX + 144, BACKGROUND_HEIGHT - 32, true, $ -> {
                if (currentPage < 255) {
                    currentPage++;
                    if (currentPage >= pages.size()) {
                        pages.add(new ArrayList<>());
                        pages.get(currentPage).add(FormattedLine.DEFAULT);
                    }
                }
                updateButtonVisibility();
            }, true));
            addRenderableWidget(Button.builder(SIGN_BUTTON, button -> {
                //TODO
            }).bounds(rightX + 16, BACKGROUND_HEIGHT - 48, 64, 16).build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
                onClose();
                minecraft.setScreen(null);
            }).bounds(rightX + 16, BACKGROUND_HEIGHT - 32, 64, 16).build());
        } else {
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
                onClose();
                minecraft.setScreen(null);
            }).bounds(width - BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT + 4, BACKGROUND_WIDTH, 20).build());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if (!writable) {
            FormattedTextArea.renderLines(pages.get(currentPage), graphics.pose(), graphics.bufferSource(), 0, 0, TEXT_WIDTH, TEXT_HEIGHT);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        int x = (width - (writable ? BACKGROUND_WIDTH + 80 : BACKGROUND_WIDTH)) / 2;
        graphics.blit(BACKGROUND, x, 0, 0, 0, 256, 256);
    }

    @Override
    public void onClose() {
        super.onClose();
        BigBookContent content = new BigBookContent(pages);
        stack.set(BCDataComponents.BIG_BOOK_CONTENT, content);
        PacketDistributor.sendToServer(new BigBookSyncPacket(content));
    }

    private void onLineChange(FormattedLine line) {
        updateModeButton();
        updateAlignmentButton();
        TextColor color = line.style().getColor();
        setColor(color == null ? 0 : color.getValue());
        sizeBox.setValue(String.valueOf(line.size()));
        updateSizeButtons(line.size());
    }

    private void updateModeButton() {
        modeButton.setMessage(Component.translatable(textArea.getMode().getTranslationKey()));
    }

    private void updateAlignmentButton() {
        alignmentButton.setMessage(Component.translatable(textArea.getAlignment().getTranslationKey()));
    }

    private void updateSizeButtons(int size) {
        scaleDownButton.active = size > FormattedLine.MIN_SIZE;
        scaleUpButton.active = size < FormattedLine.MAX_SIZE;
    }

    private void setColor(int color) {
        textArea.setColor(color);
        String hexString = Integer.toHexString(color);
        colorBox.setValue("#" + "0".repeat(6 - hexString.length()) + hexString);
    }
    private void updateButtonVisibility() {
        this.backButton.visible = !isSigning && currentPage > 0;
        this.forwardButton.visible = !isSigning && (writable || currentPage < 255);
    }
}
