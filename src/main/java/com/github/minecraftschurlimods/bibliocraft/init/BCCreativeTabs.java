package com.github.minecraftschurlimods.bibliocraft.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Collection;
import java.util.function.Supplier;

public interface BCCreativeTabs {
    Supplier<CreativeModeTab> BIBLIOCRAFT = BCRegistries.CREATIVE_TABS.register("bibliocraft", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(BCItems.BOOKCASE.get(WoodType.OAK)))
            .displayItems((display, output) -> {
                addToTab(output, BCItems.BOOKCASE.values());
            })
            .build());

    private static void addToTab(CreativeModeTab.Output output, Collection<? extends ItemLike> list) {
        for (ItemLike item : list) {
            output.accept(item);
        }
    }

    static void init() {}
}
