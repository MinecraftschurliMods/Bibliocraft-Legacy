package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ColorButton;
import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineList;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineListPacket;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HexFormat;
import java.util.Objects;

public class FancySignScreen extends Screen {
    public static final int WIDTH = 140;
    public static final int HEIGHT = 80;
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/fancy_sign.png");
    private final BlockPos pos;
    private final boolean back;
    private FormattedTextArea textArea;
    private Button modeButton;
    private Button alignmentButton;
    private EditBox colorBox;
    private EditBox sizeBox;
    private Button scaleDownButton;
    private Button scaleUpButton;

    public FancySignScreen(BlockPos pos, boolean back) {
        super(Component.empty());
        this.pos = pos;
        this.back = back;
    }

    @Override
    public void onClose() {
        if (!(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign))
            return;
        FormattedLineList list = new FormattedLineList(textArea.getLines());
        if (back) {
            sign.setBackContent(list);
        } else {
            sign.setFrontContent(list);
        }
        PacketDistributor.sendToServer(new FormattedLineListPacket(list, pos, back));
        super.onClose();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void init() {
        if (!(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign))
            return;
        int leftX = (width - WIDTH) / 2;
        int rightX = (width + WIDTH) / 2;
        int y = (height - HEIGHT) / 2 - 16;
        textArea = addRenderableWidget(new FormattedTextArea(leftX, y, WIDTH, HEIGHT, sign.getFrontContent().lines()));
        textArea.setOnLineChange(this::onLineChange);

        // Color buttons and text box
        ChatFormatting[] colors = BCUtil.getChatFormattingColors().toArray(ChatFormatting[]::new);
        int colorRows = Math.floorDiv(colors.length, 4);
        colorBox = addRenderableWidget(new EditBox(font, rightX + 16, y + 16 * colorRows, 64, 16, Component.empty()));
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
                    .bounds(rightX + 80 - 16 * (4 - i % 4), y + 16 * Math.floorDiv(i, 4), 16, 16)));
            button.setTooltip(Tooltip.create(Component.translatable("color." + colors[i].getName())));
        }

        // Formatting buttons
        addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_BOLD_SHORT, $ -> textArea.toggleStyle(Style::isBold, Style::withBold))
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_BOLD))
                .bounds(leftX - 80, y, 16, 16)
                .build());
        addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_ITALIC_SHORT, $ -> textArea.toggleStyle(Style::isItalic, Style::withItalic))
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ITALIC))
                .bounds(leftX - 64, y, 16, 16)
                .build());
        addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_UNDERLINED_SHORT, $ -> textArea.toggleStyle(Style::isUnderlined, Style::withUnderlined))
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_UNDERLINED))
                .bounds(leftX - 48, y, 16, 16)
                .build());
        addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_SHORT, $ -> textArea.toggleStyle(Style::isStrikethrough, Style::withStrikethrough))
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_STRIKETHROUGH))
                .bounds(leftX - 32, y, 16, 16)
                .build());
        addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_OBFUSCATED_SHORT, $ -> textArea.toggleStyle(Style::isObfuscated, Style::withObfuscated))
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_OBFUSCATED))
                .bounds(leftX - 80, y + 16, 16, 16)
                .build());
        modeButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getMode().getTranslationKey()), button -> {
                    textArea.toggleMode();
                    updateModeButton();
                })
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_MODE))
                .bounds(leftX - 64, y + 16, 48, 16)
                .build());
        alignmentButton = addRenderableWidget(Button.builder(Component.translatable(textArea.getAlignment().getTranslationKey()), button -> {
                    textArea.toggleAlignment();
                    updateAlignmentButton();
                })
                .tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_ALIGNMENT))
                .bounds(leftX - 80, y + 32, 64, 16)
                .build());

        // Size buttons and text box
        sizeBox = addRenderableWidget(new EditBox(font, leftX - 64, y + 64, 32, 16, Component.empty()));
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
        }).bounds(leftX - 80, y + 64, 16, 16).tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP)).build());
        scaleUpButton = addRenderableWidget(Button.builder(Translations.FANCY_TEXT_AREA_SCALE_UP, button -> {
            int size = textArea.getSize() + 1;
            sizeBox.setValue(String.valueOf(size));
            // call again to account for invalid values
            sizeBox.setValue(String.valueOf(textArea.getSize()));
            updateSizeButtons(size);
        }).bounds(leftX - 32, y + 64, 16, 16).tooltip(Tooltip.create(Translations.FANCY_TEXT_AREA_SCALE_UP_TOOLTIP)).build());
        onLineChange(textArea.getLines().getFirst());

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> {
            onClose();
            minecraft.setScreen(null);
        }).bounds(leftX, y + HEIGHT + 8, WIDTH, 20).build());
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, (width - WIDTH) / 2 - 4, (height - HEIGHT) / 2 - 20, 0, 0, 192, 192);
    }

    private void setColor(int color) {
        textArea.setColor(color);
        String hexString = Integer.toHexString(color);
        colorBox.setValue("#" + "0".repeat(6 - hexString.length()) + hexString);
    }

    private void updateSizeButtons(int size) {
        scaleDownButton.active = size > FormattedLine.MIN_SIZE;
        scaleUpButton.active = size < FormattedLine.MAX_SIZE;
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
}
