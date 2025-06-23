package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ExperienceBarButton;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMode;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableSetModePacket;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrintingTableScreen extends BCScreenWithToggleableSlots<PrintingTableMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/printing_table.png");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND = BCUtil.bcLoc("experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS = BCUtil.bcLoc("experience_bar_progress");
    private static final ResourceLocation PROGRESS = BCUtil.bcLoc("printing_table_progress");
    private Button modeButton;
    private Button experienceBarButton;

    public PrintingTableScreen(PrintingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }

    @Override
    protected void init() {
        leftPos = (width - 192) / 2;
        topPos = (height - 192) / 2;
        modeButton = addRenderableWidget(Button.builder(Component.empty(), $ -> {
            PrintingTableBlockEntity blockEntity = menu.getBlockEntity();
            blockEntity.setMode(switch (blockEntity.getMode()) {
                case BIND -> PrintingTableMode.CLONE;
                case CLONE -> PrintingTableMode.MERGE;
                case MERGE -> PrintingTableMode.BIND;
            });
            setModeButtonMessage();
            experienceBarButton.visible = blockEntity.getMode() == PrintingTableMode.CLONE;
            PacketDistributor.sendToServer(new PrintingTableSetModePacket(blockEntity.getBlockPos(), blockEntity.getMode()));
        }).bounds(leftPos + 81, topPos + 6, 82, 20).build());
        setModeButtonMessage();
        experienceBarButton = addRenderableWidget(new ExperienceBarButton(leftPos + 81, topPos + 65, 82, 5, EXPERIENCE_BAR_BACKGROUND, EXPERIENCE_BAR_PROGRESS, () -> 1, () -> 0.25f, $ -> {
        }));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        float progress = menu.getBlockEntity().getProgress();
        int width = progress == 1f ? 0 : Mth.ceil(progress * 24);
        graphics.blitSprite(PROGRESS, 24, 16, 0, 0, leftPos + 110, topPos + 35, width, 16);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        int experienceCost = menu.getBlockEntity().getExperienceCost();
        if (experienceCost > 0) {
            ClientUtil.renderXpText(experienceCost + "", graphics, leftPos + 122, topPos + 39);
        }
    }

    private void setModeButtonMessage() {
        modeButton.setMessage(Component.translatable(Translations.PRINTING_TABLE_MODE_KEY, Component.translatable(menu.getBlockEntity().getMode().getTranslationKey())));
    }
}
