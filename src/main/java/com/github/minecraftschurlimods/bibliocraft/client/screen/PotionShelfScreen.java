package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PotionShelfScreen extends BCMenuScreen<PotionShelfMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/potion_shelf.png");

    public PotionShelfScreen(PotionShelfMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
