package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public interface BCMenuTypes {
    Supplier<MenuType<BookcaseMenu>> BOOKCASE = BCRegistries.MENU_TYPES.register("bookcase", () -> IMenuTypeExtension.create(BookcaseMenu::new));

    static void init() {}
}
