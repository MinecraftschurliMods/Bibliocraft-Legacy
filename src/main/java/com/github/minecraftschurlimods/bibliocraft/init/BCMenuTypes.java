package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public interface BCMenuTypes {
    Supplier<MenuType<BookcaseMenu>>    BOOKCASE     = BCRegistries.MENUS.register("bookcase",     () -> IMenuTypeExtension.create(BookcaseMenu::new));
    Supplier<MenuType<PotionShelfMenu>> POTION_SHELF = BCRegistries.MENUS.register("potion_shelf", () -> IMenuTypeExtension.create(PotionShelfMenu::new));
    Supplier<MenuType<ToolRackMenu>>    TOOL_RACK    = BCRegistries.MENUS.register("tool_rack",    () -> IMenuTypeExtension.create(ToolRackMenu::new));

    static void init() {}
}
