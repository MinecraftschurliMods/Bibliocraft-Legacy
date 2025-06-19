package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ClockTriggerEditScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/clock_edit.png");
    private static final int WIDTH = 144;
    private static final int HEIGHT = 72;
    private final ClockScreen parent;
    @Nullable
    private final ClockTrigger old;
    private final int timeWidth;
    private final int separatorWidth;
    private final int redstoneWidth;
    private final int soundWidth;
    private int leftPos;
    private int topPos;
    private int contentLeftPos;
    private int contentTopPos;
    private EditBox hours;
    private EditBox minutes;
    private Checkbox redstone;
    private Checkbox sound;

    public ClockTriggerEditScreen(ClockScreen parent, @Nullable ClockTrigger old) {
        super(Translations.CLOCK_TITLE);
        this.parent = parent;
        this.old = old;
        Font font = ClientUtil.getFont();
        timeWidth = font.width(Translations.CLOCK_TIME);
        separatorWidth = font.width(Translations.CLOCK_TIME_SEPARATOR);
        redstoneWidth = font.width(Translations.CLOCK_EMIT_REDSTONE);
        soundWidth = font.width(Translations.CLOCK_EMIT_SOUND);
    }

    @Override
    protected void init() {
        leftPos = (width - WIDTH) / 2;
        topPos = (height - HEIGHT) / 2;
        contentLeftPos = (width - Math.min(WIDTH - 12, BCUtil.max(timeWidth + separatorWidth + 90, redstoneWidth + 19, soundWidth + 19))) / 2;
        contentTopPos = topPos + 6;
        Font font = ClientUtil.getFont();
        hours = addRenderableWidget(new EditBox(font, contentLeftPos + timeWidth + 2, contentTopPos, 40, 20, Translations.CLOCK_HOURS));
        hours.setHint(Translations.CLOCK_HOURS_HINT);
        hours.setFilter(s -> {
            try {
                int i = Integer.parseInt(s);
                return i >= 0 && i < 24;
            } catch (NumberFormatException e) {
                return s.isEmpty();
            }
        });
        minutes = addRenderableWidget(new EditBox(font, contentLeftPos + timeWidth + separatorWidth + 44, contentTopPos, 40, 20, Translations.CLOCK_MINUTES));
        minutes.setHint(Translations.CLOCK_MINUTES_HINT);
        minutes.setFilter(s -> {
            try {
                int i = Integer.parseInt(s);
                return i >= 0 && i < 60;
            } catch (NumberFormatException e) {
                return s.isEmpty();
            }
        });
        redstone = addRenderableWidget(Checkbox.builder(Component.empty(), font).pos(contentLeftPos, contentTopPos + 22).build());
        sound = addRenderableWidget(Checkbox.builder(Component.empty(), font).pos(contentLeftPos, contentTopPos + 41).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> {
            try {
                if (old != null) {
                    parent.removeTrigger(old);
                }
                parent.addTrigger(new ClockTrigger(Integer.parseInt(hours.getValue()), Integer.parseInt(minutes.getValue()), redstone.selected(), sound.selected()));
            } catch (NumberFormatException ignored) {
            }
            onClose();
        }).bounds(leftPos, topPos + HEIGHT + 4, WIDTH, 20).build());
        if (old != null) {
            hours.setValue(String.valueOf(old.hour()));
            minutes.setValue(String.valueOf(old.minute()));
            if (old.redstone()) {
                redstone.onPress();
            }
            if (old.sound()) {
                sound.onPress();
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_TIME, contentLeftPos, contentTopPos + 6, 0x404040, false);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_TIME_SEPARATOR, contentLeftPos + timeWidth + 43, contentTopPos + 6, 0x404040, false);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_EMIT_REDSTONE, contentLeftPos + 19, contentTopPos + 27, 0x404040, false);
        graphics.drawString(ClientUtil.getFont(), Translations.CLOCK_EMIT_SOUND, contentLeftPos + 19, contentTopPos + 46, 0x404040, false);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, WIDTH, HEIGHT);
    }
}
