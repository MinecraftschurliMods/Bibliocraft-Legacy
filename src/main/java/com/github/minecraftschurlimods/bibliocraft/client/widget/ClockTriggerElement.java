package com.github.minecraftschurlimods.bibliocraft.client.widget;

import com.github.minecraftschurlimods.bibliocraft.client.screen.ClockTriggerEditScreen;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ClockTriggerElement extends Screen {
    public static final int WIDTH = 160;
    public static final int HEIGHT = 20;
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/clock_trigger.png");
    private static final ResourceLocation EDIT = BCUtil.bcLoc("edit");
    private static final ResourceLocation EDIT_HIGHLIGHTED = BCUtil.bcLoc("edit_highlighted");
    private static final ResourceLocation DELETE = BCUtil.bcLoc("delete");
    private static final ResourceLocation DELETE_HIGHLIGHTED = BCUtil.bcLoc("delete_highlighted");
    private static final ItemStack REDSTONE = new ItemStack(Items.REDSTONE);
    private static final ItemStack NOTE_BLOCK = new ItemStack(Items.NOTE_BLOCK);
    public final ClockTriggerPanel owner;
    private final ClockTrigger trigger;
    private final int listSize;
    private final Button editButton;
    private final Button deleteButton;

    public ClockTriggerElement(ClockTrigger trigger, ClockTriggerPanel owner, int listSize) {
        super(Component.literal(String.format("%02d%s%02d", trigger.hour(), Translations.CLOCK_TIME_SEPARATOR.getString(), trigger.minute())));
        this.width = WIDTH;
        this.height = HEIGHT;
        this.trigger = trigger;
        this.owner = owner;
        this.listSize = listSize;
        int width = owner.hasScrollbar(listSize) ? WIDTH - 6 : WIDTH;
        editButton = addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(EDIT, EDIT_HIGHLIGHTED, width - 34, 2, 16, 16, $ -> ClientUtil.getMc().pushGuiLayer(new ClockTriggerEditScreen(owner.owner, trigger))));
        deleteButton = addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(DELETE, DELETE_HIGHLIGHTED, width - 18, 2, 16, 16, $ -> owner.owner.removeTrigger(trigger)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        boolean hasScrollbar = owner.hasScrollbar(listSize);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, 0, 0, 0, hasScrollbar ? 20 : 0, hasScrollbar ? WIDTH - 6 : WIDTH, HEIGHT, 256, 256);
        if (trigger.redstone()) {
            graphics.renderItem(REDSTONE, 2, 1);
        }
        if (trigger.sound()) {
            graphics.renderItem(NOTE_BLOCK, 19, 1);
        }
        graphics.drawString(ClientUtil.getFont(), getTitle(), 42, 6, 0xFF111111, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    }

    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (mouseY >= 2 && mouseY < 18) {
            Font font = ClientUtil.getFont();
            if (trigger.redstone() && mouseX >= 2 && mouseX < 18) {
                graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.CLOCK_EMIT_REDSTONE), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            } else if (trigger.sound() && mouseX >= 19 && mouseX < 37) {
                graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.CLOCK_EMIT_SOUND), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            } else if (editButton.getRectangle().containsPoint(mouseX, mouseY)) {
                graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.CLOCK_EDIT_TRIGGER), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            } else if (deleteButton.getRectangle().containsPoint(mouseX, mouseY)) {
                graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.CLOCK_DELETE_TRIGGER), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
            }
        }
    }
}
