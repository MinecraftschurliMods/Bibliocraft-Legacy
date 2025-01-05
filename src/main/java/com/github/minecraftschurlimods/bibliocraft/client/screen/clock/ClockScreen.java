package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public class ClockScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/clock.png");
    private static final Component ADD_TRIGGER = Component.translatable(Translations.CLOCK_ADD_TRIGGER);
    private static final Component TICK = Component.translatable(Translations.CLOCK_TICK);
    private static final Component TITLE = Component.translatable(Translations.CLOCK_TITLE);
    private static final Component TRIGGERS = Component.translatable(Translations.CLOCK_TRIGGERS);
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 166;
    private final BlockPos pos;
    private final ClockBlockEntity clock;
    private final List<ClockTrigger> triggers;
    private int leftPos;
    private int topPos;

    public ClockScreen(BlockPos pos) {
        super(TITLE);
        this.pos = pos;
        clock = (ClockBlockEntity) Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos));
        triggers = clock.getTriggers();
    }

    @Override
    protected void init() {
        leftPos = (width - IMAGE_WIDTH) / 2;
        topPos = (height - IMAGE_HEIGHT) / 2;
        addRenderableWidget(Checkbox.builder(Component.empty(), Minecraft.getInstance().font)
                .pos(leftPos + 7, topPos + 6)
                .selected(clock.tickSound)
                .onValueChange((checkbox, value) -> clock.tickSound = value)
                .build());
        addRenderableWidget(Button.builder(ADD_TRIGGER, $ -> {})
                .bounds(width / 2 - 100, topPos + 170, 98, 20)
                .build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 + 2, topPos + 170, 98, 20)
                .build());
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new ClockSyncPacket(pos, triggers));
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(Minecraft.getInstance().font, TICK, leftPos + 28, topPos + 11, 0x404040, false);
        guiGraphics.drawString(Minecraft.getInstance().font, TRIGGERS, leftPos + 8, topPos + 26, 0x404040, false);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
}
