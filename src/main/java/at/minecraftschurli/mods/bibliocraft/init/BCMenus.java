package at.minecraftschurli.mods.bibliocraft.init;

import at.minecraftschurli.mods.bibliocraft.content.bookcase.BookcaseMenu;
import at.minecraftschurli.mods.bibliocraft.content.cookiejar.CookieJarMenu;
import at.minecraftschurli.mods.bibliocraft.content.discrack.DiscRackMenu;
import at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand.FancyArmorStandMenu;
import at.minecraftschurli.mods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import at.minecraftschurli.mods.bibliocraft.content.label.LabelMenu;
import at.minecraftschurli.mods.bibliocraft.content.potionshelf.PotionShelfMenu;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableMenu;
import at.minecraftschurli.mods.bibliocraft.content.shelf.ShelfMenu;
import at.minecraftschurli.mods.bibliocraft.content.slottedbook.SlottedBookMenu;
import at.minecraftschurli.mods.bibliocraft.content.toolrack.ToolRackMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface BCMenus {
    // @formatter:off
    Supplier<MenuType<BookcaseMenu>>        BOOKCASE          = register("bookcase",          BookcaseMenu::new);
    Supplier<MenuType<CookieJarMenu>>       COOKIE_JAR        = register("cookie_jar",        CookieJarMenu::new);
    Supplier<MenuType<DiscRackMenu>>        DISC_RACK         = register("disc_rack",         DiscRackMenu::new);
    Supplier<MenuType<FancyArmorStandMenu>> FANCY_ARMOR_STAND = register("fancy_armor_stand", FancyArmorStandMenu::new);
    Supplier<MenuType<FancyCrafterMenu>>    FANCY_CRAFTER     = register("fancy_crafter",     FancyCrafterMenu::new);
    Supplier<MenuType<LabelMenu>>           LABEL             = register("label",             LabelMenu::new);
    Supplier<MenuType<PotionShelfMenu>>     POTION_SHELF      = register("potion_shelf",      PotionShelfMenu::new);
    Supplier<MenuType<PrintingTableMenu>>   PRINTING_TABLE    = register("printing_table",    PrintingTableMenu::new);
    Supplier<MenuType<ShelfMenu>>           SHELF             = register("shelf",             ShelfMenu::new);
    Supplier<MenuType<SlottedBookMenu>>     SLOTTED_BOOK      = register("slotted_book",      SlottedBookMenu::new);
    Supplier<MenuType<ToolRackMenu>>        TOOL_RACK         = register("tool_rack",         ToolRackMenu::new);
    // @formatter:on

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return BCRegistries.MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    /// Empty method, called by [BCRegistries#init()] to classload this class.
    @ApiStatus.Internal
    static void init() {}
}
