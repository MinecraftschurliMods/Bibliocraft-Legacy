package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FormattedLine;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BigBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/fancy_sign.png");
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
