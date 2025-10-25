package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ClockTriggerPanel;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;

public class ClockScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/clock.png");
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 166;
    private final BlockPos pos;
    private final ClockBlockEntity clock;
    private final List<ClockTrigger> triggers;
    private int leftPos;
    private int topPos;
    private @UnknownNullability Checkbox tickSound;
    private @UnknownNullability ClockTriggerPanel triggerPanel;

    public ClockScreen(BlockPos pos) {
        super(Translations.CLOCK_TITLE);
        this.pos = pos;
        clock = (ClockBlockEntity) BCUtil.nonNull(ClientUtil.getLevel().getBlockEntity(pos));
        triggers = new ArrayList<>(clock.getTriggers());
    }

    @Override
    protected void init() {
        leftPos = (width - IMAGE_WIDTH) / 2;
        topPos = (height - IMAGE_HEIGHT) / 2;
        tickSound = addRenderableWidget(Checkbox.builder(Component.empty(), ClientUtil.getFont())
                .pos(leftPos + 7, topPos + 6)
                .selected(clock.getTickSound())
                .build());
        triggerPanel = addRenderableWidget(new ClockTriggerPanel(leftPos + 8, topPos + 36, 160, 122, triggers, this));
        addRenderableWidget(Button.builder(Translations.CLOCK_ADD_TRIGGER, $ -> ClientUtil.getMc().pushGuiLayer(new ClockTriggerEditScreen(this, null)))
                .bounds(width / 2 - 100, topPos + 170, 98, 20)
                .build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 + 2, topPos + 170, 98, 20)
                .build());
    }

    @Override
    public void onClose() {
        ClientPacketDistributor.sendToServer(new ClockSyncPacket(pos, tickSound.selected(), triggers));
        super.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_TICK, leftPos + 28, topPos + 11, 0xFF111111, false);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_TRIGGERS, leftPos + 8, topPos + 26, 0xFF111111, false);
        triggerPanel.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
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

    @Override
    public boolean isInGameUi() {
        return true;
    }
}
