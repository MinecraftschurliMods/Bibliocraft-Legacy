package at.minecraftschurli.mods.bibliocraft.client.screen;

import at.minecraftschurli.mods.bibliocraft.content.bookcase.BookcaseMenu;
import at.minecraftschurli.mods.bibliocraft.content.cookiejar.CookieJarMenu;
import at.minecraftschurli.mods.bibliocraft.content.discrack.DiscRackMenu;
import at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand.FancyArmorStandMenu;
import at.minecraftschurli.mods.bibliocraft.content.label.LabelMenu;
import at.minecraftschurli.mods.bibliocraft.content.potionshelf.PotionShelfMenu;
import at.minecraftschurli.mods.bibliocraft.content.shelf.ShelfMenu;
import at.minecraftschurli.mods.bibliocraft.content.toolrack.ToolRackMenu;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public final class BCMenuScreens {
    public static class Bookcase extends BCMenuScreen<BookcaseMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/bookcase.png");

        public Bookcase(BookcaseMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class CookieJar extends BCMenuScreen<CookieJarMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/cookie_jar.png");

        public CookieJar(CookieJarMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class DiscRack extends BCMenuScreen<DiscRackMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/disc_rack.png");

        public DiscRack(DiscRackMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class FancyArmorStand extends BCMenuScreen<FancyArmorStandMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/fancy_armor_stand.png");

        public FancyArmorStand(FancyArmorStandMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class Label extends BCMenuScreen<LabelMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/label.png");

        public Label(LabelMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class PotionShelf extends BCMenuScreen<PotionShelfMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/potion_shelf.png");

        public PotionShelf(PotionShelfMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class Shelf extends BCMenuScreen<ShelfMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/shelf.png");

        public Shelf(ShelfMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }

    public static class ToolRack extends BCMenuScreen<ToolRackMenu> {
        private static final Identifier BACKGROUND = BCUtil.bcLoc("textures/gui/shelf.png");

        public ToolRack(ToolRackMenu menu, Inventory inventory, Component title) {
            super(menu, inventory, title, BACKGROUND);
        }
    }
}
