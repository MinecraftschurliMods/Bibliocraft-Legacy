package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FancyCrafterScreen extends BCScreenWithToggleableSlots<FancyCrafterMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/fancy_crafter.png");

    public FancyCrafterScreen(FancyCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
        imageHeight = 192;
        inventoryLabelY = 99;
    }
}
