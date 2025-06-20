package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMode;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableSetModePacket;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrintingTableScreen extends BCScreenWithTogglableSlots<PrintingTableMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/printing_table.png");
    private static final ResourceLocation PROGRESS = BCUtil.bcLoc("printing_table_progress");
    private Button modeButton;

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
            PacketDistributor.sendToServer(new PrintingTableSetModePacket(blockEntity.getBlockPos(), blockEntity.getMode()));
        }).bounds(leftPos + 83, topPos + 6, 80, 20).build());
        setModeButtonMessage();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        int arrowWidth = 24;
        float progress = menu.getBlockEntity().getProgress();
        int width = progress == 1f ? 0 : Mth.ceil(progress * arrowWidth);
        graphics.blitSprite(PROGRESS, arrowWidth, 16, 0, 0, leftPos + 110, topPos + 35, width, 16);
    }

    private void setModeButtonMessage() {
        modeButton.setMessage(Component.translatable(Translations.PRINTING_TABLE_MODE_KEY, Component.translatable(menu.getBlockEntity().getMode().getTranslationKey())));
    }
}
