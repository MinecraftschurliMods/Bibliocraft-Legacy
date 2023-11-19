package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BookcaseScreen extends BCMenuScreen<BookcaseMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/bookcase.png");

    public BookcaseScreen(BookcaseMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }
}
