package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PrintingTableScreen extends BCMenuScreen<PrintingTableMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/printing_table.png");

    public PrintingTableScreen(PrintingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
