package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public interface BCMenuTypes {
    Supplier<MenuType<BookcaseMenu>> BOOKCASE = BCRegistries.MENUS.register("bookcase", () -> IMenuTypeExtension.create(BookcaseMenu::new));

    static void init() {}
}
