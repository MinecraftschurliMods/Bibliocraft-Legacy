package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarMenu;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackMenu;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandMenu;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelMenu;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackMenu;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class BCMenuScreens {
    public static class Bookcase extends BCMenuScreen<BookcaseMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/bookcase.png");

        public Bookcase(BookcaseMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class CookieJar extends BCMenuScreen<CookieJarMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/cookie_jar.png");

        public CookieJar(CookieJarMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class DiscRack extends BCMenuScreen<DiscRackMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/disc_rack.png");

        public DiscRack(DiscRackMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class FancyArmorStand extends BCMenuScreen<FancyArmorStandMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/fancy_armor_stand.png");

        public FancyArmorStand(FancyArmorStandMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class Label extends BCMenuScreen<LabelMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/label.png");

        public Label(LabelMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class PotionShelf extends BCMenuScreen<PotionShelfMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/potion_shelf.png");

        public PotionShelf(PotionShelfMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class Shelf extends BCMenuScreen<ShelfMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/shelf.png");

        public Shelf(ShelfMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class ToolRack extends BCMenuScreen<ToolRackMenu> {
        private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/shelf.png");

        public ToolRack(ToolRackMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }
}
