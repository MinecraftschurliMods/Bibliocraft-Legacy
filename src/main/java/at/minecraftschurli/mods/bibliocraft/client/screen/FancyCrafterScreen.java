package at.minecraftschurli.mods.bibliocraft.client.screen;

import at.minecraftschurli.mods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FancyCrafterScreen extends BCScreenWithToggleableSlots<FancyCrafterMenu> {
    private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/fancy_crafter.png");

    public FancyCrafterScreen(FancyCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND, 176, 192);
        inventoryLabelY = 99;
    }
}
