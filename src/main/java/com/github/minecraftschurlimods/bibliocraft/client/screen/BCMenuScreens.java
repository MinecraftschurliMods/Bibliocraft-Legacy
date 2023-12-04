package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class BCMenuScreens {
    public static class Bookcase extends BCMenuScreen<BookcaseMenu> {
        private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/bookcase.png");

        public Bookcase(BookcaseMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class PotionShelf extends BCMenuScreen<PotionShelfMenu> {
        private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/potion_shelf.png");

        public PotionShelf(PotionShelfMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class ToolRack extends BCMenuScreen<ToolRackMenu> {
        private static final ResourceLocation BACKGROUND = new ResourceLocation(Bibliocraft.MOD_ID, "textures/gui/tool_rack.png");

        public ToolRack(ToolRackMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }
}
