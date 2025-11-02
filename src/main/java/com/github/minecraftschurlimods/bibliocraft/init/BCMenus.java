package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarMenu;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackMenu;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandMenu;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelMenu;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.content.slottedbook.SlottedBookMenu;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackMenu;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;

import java.util.function.Supplier;

public interface BCMenus {
    // @formatter:off
    Supplier<MenuType<BookcaseMenu>>        BOOKCASE          = register("bookcase",          BookcaseMenu::new);
    Supplier<MenuType<CookieJarMenu>>       COOKIE_JAR        = register("cookie_jar",        CookieJarMenu::new);
    Supplier<MenuType<DiscRackMenu>>        DISC_RACK         = register("disc_rack",         DiscRackMenu::new);
    Supplier<MenuType<FancyArmorStandMenu>> FANCY_ARMOR_STAND = register("fancy_armor_stand", FancyArmorStandMenu::new);
    Supplier<MenuType<FancyCrafterMenu>>    FANCY_CRAFTER     = register("fancy_crafter",     FancyCrafterMenu::new, BCFeatureFlags.WORK_IN_PROGRESS);
    Supplier<MenuType<LabelMenu>>           LABEL             = register("label",             LabelMenu::new);
    Supplier<MenuType<PotionShelfMenu>>     POTION_SHELF      = register("potion_shelf",      PotionShelfMenu::new);
    Supplier<MenuType<PrintingTableMenu>>   PRINTING_TABLE    = register("printing_table",    PrintingTableMenu::new, BCFeatureFlags.WORK_IN_PROGRESS);
    Supplier<MenuType<ShelfMenu>>           SHELF             = register("shelf",             ShelfMenu::new);
    Supplier<MenuType<SlottedBookMenu>>     SLOTTED_BOOK      = register("slotted_book",      SlottedBookMenu::new);
    Supplier<MenuType<ToolRackMenu>>        TOOL_RACK         = register("tool_rack",         ToolRackMenu::new);
    // @formatter:on

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, IContainerFactory<T> factory, FeatureFlagSet flags) {
        return BCRegistries.MENUS.register(name, () -> new MenuType<>(factory, flags));
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return BCRegistries.MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
    
    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
