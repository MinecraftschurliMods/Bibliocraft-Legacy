package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandMenu;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.block.shelf.ShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public interface BCMenus {
    Supplier<MenuType<BookcaseMenu>>        BOOKCASE          = BCRegistries.MENUS.register("bookcase",          () -> IMenuTypeExtension.create(BookcaseMenu::new));
    Supplier<MenuType<FancyArmorStandMenu>> FANCY_ARMOR_STAND = BCRegistries.MENUS.register("fancy_armor_stand", () -> IMenuTypeExtension.create(FancyArmorStandMenu::new));
    Supplier<MenuType<PotionShelfMenu>>     POTION_SHELF      = BCRegistries.MENUS.register("potion_shelf",      () -> IMenuTypeExtension.create(PotionShelfMenu::new));
    Supplier<MenuType<ShelfMenu>>           SHELF             = BCRegistries.MENUS.register("shelf",             () -> IMenuTypeExtension.create(ShelfMenu::new));
    Supplier<MenuType<ToolRackMenu>>        TOOL_RACK         = BCRegistries.MENUS.register("tool_rack",         () -> IMenuTypeExtension.create(ToolRackMenu::new));

    static void init() {}
}
