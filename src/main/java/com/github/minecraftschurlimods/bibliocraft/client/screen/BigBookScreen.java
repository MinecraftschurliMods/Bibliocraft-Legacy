package com.github.minecraftschurlimods.bibliocraft.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public class BigBookScreen extends Screen {
    private final ItemStack stack;
    private final boolean writable;

    public BigBookScreen(ItemStack stack, boolean writable) {
        super(stack.getHoverName());
        this.stack = stack;
        this.writable = writable;
    }
}
