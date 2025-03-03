package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ColorButton;
import com.github.minecraftschurlimods.bibliocraft.client.widget.FormattedTextArea;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineList;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLineListPacket;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

public class FancySignScreen extends Screen {
    private static final Component BOLD = Component.translatable(Translations.FANCY_SIGN_BOLD);
    private static final Component ITALIC = Component.translatable(Translations.FANCY_SIGN_ITALIC);
    private static final Component UNDERLINED = Component.translatable(Translations.FANCY_SIGN_UNDERLINED);
    private static final Component STRIKETHROUGH = Component.translatable(Translations.FANCY_SIGN_STRIKETHROUGH);
    private static final Component OBFUSCATED = Component.translatable(Translations.FANCY_SIGN_OBFUSCATED);
    private static final Component BOLD_SHORT = Component.translatable(Translations.FANCY_SIGN_BOLD_SHORT).withStyle(Style.EMPTY.withBold(true));
    private static final Component ITALIC_SHORT = Component.translatable(Translations.FANCY_SIGN_ITALIC_SHORT).withStyle(Style.EMPTY.withItalic(true));
    private static final Component UNDERLINED_SHORT = Component.translatable(Translations.FANCY_SIGN_UNDERLINED_SHORT).withStyle(Style.EMPTY.withUnderlined(true));
    private static final Component STRIKETHROUGH_SHORT = Component.translatable(Translations.FANCY_SIGN_STRIKETHROUGH_SHORT).withStyle(Style.EMPTY.withStrikethrough(true));
    private static final Component OBFUSCATED_SHORT = Component.translatable(Translations.FANCY_SIGN_OBFUSCATED_SHORT).withStyle(Style.EMPTY.withObfuscated(true));
    private final BlockPos pos;
    private final boolean back;
    private FormattedTextArea textArea;
    private EditBox colorBox;

    public FancySignScreen(BlockPos pos, boolean back) {
        super(Component.empty());
        this.pos = pos;
        this.back = back;
    }

    @Override
    protected void init() {
        if (Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign) {
            int x = (width - FormattedTextArea.WIDTH) / 2;
            int y = 6;
            textArea = addRenderableWidget(new FormattedTextArea(x, y, Component.empty(), sign.getFrontContent().lines()));

            // Formatting buttons
            addRenderableWidget(Button.builder(BOLD_SHORT, $ -> textArea.toggleStyle(Style::isBold, Style::withBold))
                    .tooltip(Tooltip.create(BOLD))
                    .bounds(x - 64, y, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(ITALIC_SHORT, $ -> textArea.toggleStyle(Style::isItalic, Style::withItalic))
                    .tooltip(Tooltip.create(ITALIC))
                    .bounds(x - 48, y, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(UNDERLINED_SHORT, $ -> textArea.toggleStyle(Style::isUnderlined, Style::withUnderlined))
                    .tooltip(Tooltip.create(UNDERLINED))
                    .bounds(x - 32, y, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(STRIKETHROUGH_SHORT, $ -> textArea.toggleStyle(Style::isStrikethrough, Style::withStrikethrough))
                    .tooltip(Tooltip.create(STRIKETHROUGH))
                    .bounds(x - 64, y + 16, 16, 16)
                    .build());
            addRenderableWidget(Button.builder(OBFUSCATED_SHORT, $ -> textArea.toggleStyle(Style::isObfuscated, Style::withObfuscated))
                    .tooltip(Tooltip.create(OBFUSCATED))
                    .bounds(x - 48, y + 16, 16, 16)
                    .build());

            // Color buttons and text box
            int[] colors = Arrays.stream(ChatFormatting.values())
                    .map(ChatFormatting::getColor)
                    .filter(Objects::nonNull)
                    .mapToInt(e -> e)
                    .toArray();
            int columns = 4;
            colorBox = addRenderableWidget(new EditBox(font, x - 16 - 16 * columns, y + 64 + 16 * Math.floorDiv(colors.length, columns), columns * 16, 16, Component.empty()));
            colorBox.setMaxLength(7);
            colorBox.setFilter(s -> s.isEmpty() || s.charAt(0) == '#' && s.substring(1).codePoints().allMatch(HexFormat::isHexDigit));
            colorBox.setResponder(s -> {
                if (s.length() <= 1) return;
                textArea.setColor(Integer.parseInt(s.substring(1), 16));
            });
            for (int i = 0; i < colors.length; i++) {
                final int j = i; // I love Java
                addRenderableWidget(new ColorButton(colors[i], Button.builder(Component.empty(), $ -> {
                    textArea.setColor(colors[j]);
                    String hexString = Integer.toHexString(colors[j]);
                    colorBox.setValue("#" + "0".repeat(6 - hexString.length()) + hexString);
                }).bounds(x - 16 - 16 * (columns - i % columns), y + 48 + 16 * Math.floorDiv(i, columns), 16, 16)));
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        //TODO
    }

    @Override
    public void onClose() {
        if (!(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos) instanceof FancySignBlockEntity sign)) return;
        FormattedLineList list = new FormattedLineList(textArea.getLines());
        if (back) {
            sign.setBackContent(list);
        } else {
            sign.setFrontContent(list);
        }
        PacketDistributor.sendToServer(new FormattedLineListPacket(list, pos, back));
        super.onClose();
    }
    
    private static boolean isHexDigit(char c) {
        return Character.isDigit(c) || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }
    
    private static boolean isHexDigit(int c) {
        return isHexDigit((char) c);
    }
}
