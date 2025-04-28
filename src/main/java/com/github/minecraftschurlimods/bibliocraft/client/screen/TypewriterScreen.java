package com.github.minecraftschurlimods.bibliocraft.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class TypewriterScreen extends Screen {
    private final BlockPos pos;

    public TypewriterScreen(BlockPos pos) {
        super(Component.empty());
        this.pos = pos;
    }
}
