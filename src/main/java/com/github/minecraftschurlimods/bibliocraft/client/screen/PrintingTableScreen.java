package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMode;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableSetModePacket;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrintingTableScreen extends BCMenuScreen<PrintingTableMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/printing_table.png");
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

    private void setModeButtonMessage() {
        modeButton.setMessage(Component.translatable(Translations.PRINTING_TABLE_MODE_KEY, Component.translatable(menu.getBlockEntity().getMode().getTranslationKey())));
    }
}
