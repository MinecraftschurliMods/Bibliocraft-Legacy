package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ToolRackScreen extends BCMenuScreen<ToolRackMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/tool_rack.png");

    public ToolRackScreen(ToolRackMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
