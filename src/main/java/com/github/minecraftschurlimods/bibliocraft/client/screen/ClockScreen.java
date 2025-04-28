package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ClockTriggerPanel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClockScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/clock.png");
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 166;
    private final BlockPos pos;
    private final ClockBlockEntity clock;
    private final List<ClockTrigger> triggers;
    private int leftPos;
    private int topPos;
    private Checkbox tickSound;
    private ClockTriggerPanel triggerPanel;

    public ClockScreen(BlockPos pos) {
        super(Translations.CLOCK_TITLE);
        this.pos = pos;
        clock = (ClockBlockEntity) Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos));
        triggers = new ArrayList<>(clock.getTriggers());
    }

    @Override
    protected void init() {
        leftPos = (width - IMAGE_WIDTH) / 2;
        topPos = (height - IMAGE_HEIGHT) / 2;
        tickSound = addRenderableWidget(Checkbox.builder(Component.empty(), Minecraft.getInstance().font)
                .pos(leftPos + 7, topPos + 6)
                .selected(clock.tickSound)
                .build());
        triggerPanel = addRenderableWidget(new ClockTriggerPanel(leftPos + 8, topPos + 36, 160, 122, triggers, this));
        addRenderableWidget(Button.builder(Translations.CLOCK_ADD_TRIGGER, $ -> Minecraft.getInstance().pushGuiLayer(new ClockTriggerEditScreen(this, null)))
                .bounds(width / 2 - 100, topPos + 170, 98, 20)
                .build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 + 2, topPos + 170, 98, 20)
                .build());
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new ClockSyncPacket(pos, tickSound.selected(), triggers));
        super.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(Minecraft.getInstance().font, Translations.CLOCK_TICK, leftPos + 28, topPos + 11, 0x404040, false);
        graphics.drawString(Minecraft.getInstance().font, Translations.CLOCK_TRIGGERS, leftPos + 8, topPos + 26, 0x404040, false);
        triggerPanel.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    public void addTrigger(ClockTrigger trigger) {
        triggers.add(trigger);
        triggers.sort(ClockTrigger::compareTo);
        triggerPanel.rebuildElements(triggers);
    }

    public void removeTrigger(ClockTrigger trigger) {
        triggers.remove(trigger);
        triggers.sort(ClockTrigger::compareTo);
        triggerPanel.rebuildElements(triggers);
    }
}
