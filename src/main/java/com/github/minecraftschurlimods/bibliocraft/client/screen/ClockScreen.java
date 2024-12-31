package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ClockScreen extends Screen {
    private static final Component TITLE = Component.translatable(Translations.CLOCK_TITLE);
    private final BlockPos pos;

    public ClockScreen(BlockPos pos) {
        super(TITLE);
        this.pos = pos;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 - 100, 196, 200, 20).build());
    }
}
