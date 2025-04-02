package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BigBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/fancy_sign.png");
    private static final Component BOLD = Component.translatable(Translations.FANCY_TEXT_AREA_BOLD);
    private static final Component ITALIC = Component.translatable(Translations.FANCY_TEXT_AREA_ITALIC);
    private static final Component UNDERLINED = Component.translatable(Translations.FANCY_TEXT_AREA_UNDERLINED);
    private static final Component STRIKETHROUGH = Component.translatable(Translations.FANCY_TEXT_AREA_STRIKETHROUGH);
    private static final Component OBFUSCATED = Component.translatable(Translations.FANCY_TEXT_AREA_OBFUSCATED);
    private static final Component BOLD_SHORT = Component.translatable(Translations.FANCY_TEXT_AREA_BOLD_SHORT).withStyle(Style.EMPTY.withBold(true));
    private static final Component ITALIC_SHORT = Component.translatable(Translations.FANCY_TEXT_AREA_ITALIC_SHORT).withStyle(Style.EMPTY.withItalic(true));
    private static final Component UNDERLINED_SHORT = Component.translatable(Translations.FANCY_TEXT_AREA_UNDERLINED_SHORT).withStyle(Style.EMPTY.withUnderlined(true));
    private static final Component STRIKETHROUGH_SHORT = Component.translatable(Translations.FANCY_TEXT_AREA_STRIKETHROUGH_SHORT).withStyle(Style.EMPTY.withStrikethrough(true));
    private static final Component OBFUSCATED_SHORT = Component.translatable(Translations.FANCY_TEXT_AREA_OBFUSCATED_SHORT).withStyle(Style.EMPTY.withObfuscated(true));
    private static final Component MODE = Component.translatable(Translations.FANCY_TEXT_AREA_MODE);
    private static final Component ALIGNMENT = Component.translatable(Translations.FANCY_TEXT_AREA_ALIGNMENT);
    private static final Component COLOR_HINT = Component.translatable(Translations.FANCY_TEXT_AREA_COLOR_HINT);
    private static final Component SCALE_DOWN = Component.translatable(Translations.FANCY_TEXT_AREA_SCALE_DOWN);
    private static final Component SCALE_DOWN_TOOLTIP = Component.translatable(Translations.FANCY_TEXT_AREA_SCALE_DOWN_TOOLTIP);
    private static final Component SCALE_UP = Component.translatable(Translations.FANCY_TEXT_AREA_SCALE_UP);
    private static final Component SCALE_UP_TOOLTIP = Component.translatable(Translations.FANCY_TEXT_AREA_SCALE_UP_TOOLTIP);
    private final ItemStack stack;
    private final boolean writable;
    private List<List<FormattedLine>> pages;
    private int currentPage = 0;

    public BigBookScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.stack = stack;
        if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
            pages = Objects.requireNonNull(stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)).pages();
            writable = false;
        } else if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
            pages = Objects.requireNonNull(stack.get(BCDataComponents.BIG_BOOK_CONTENT)).pages();
            writable = true;
        } else {
            pages = new ArrayList<>();
            writable = true;
        }
    }

    @Override
    protected void init() {
    }
}
