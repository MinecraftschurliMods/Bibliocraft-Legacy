package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarMenu;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackMenu;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandMenu;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelMenu;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public interface BCMenus {
    Supplier<MenuType<BookcaseMenu>>        BOOKCASE          = BCRegistries.MENUS.register("bookcase",          () -> IMenuTypeExtension.create(BookcaseMenu::new));
    Supplier<MenuType<CookieJarMenu>>       COOKIE_JAR        = BCRegistries.MENUS.register("cookie_jar",        () -> IMenuTypeExtension.create(CookieJarMenu::new));
    Supplier<MenuType<DiscRackMenu>>        DISC_RACK         = BCRegistries.MENUS.register("disc_rack",         () -> IMenuTypeExtension.create(DiscRackMenu::new));
    Supplier<MenuType<FancyArmorStandMenu>> FANCY_ARMOR_STAND = BCRegistries.MENUS.register("fancy_armor_stand", () -> IMenuTypeExtension.create(FancyArmorStandMenu::new));
    Supplier<MenuType<LabelMenu>>           LABEL             = BCRegistries.MENUS.register("label",             () -> IMenuTypeExtension.create(LabelMenu::new));
    Supplier<MenuType<PotionShelfMenu>>     POTION_SHELF      = BCRegistries.MENUS.register("potion_shelf",      () -> IMenuTypeExtension.create(PotionShelfMenu::new));
    Supplier<MenuType<ShelfMenu>>           SHELF             = BCRegistries.MENUS.register("shelf",             () -> IMenuTypeExtension.create(ShelfMenu::new));
    Supplier<MenuType<ToolRackMenu>>        TOOL_RACK         = BCRegistries.MENUS.register("tool_rack",         () -> IMenuTypeExtension.create(ToolRackMenu::new));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
