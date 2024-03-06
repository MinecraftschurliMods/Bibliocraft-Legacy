package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface BCCreativeTabs {
    Supplier<CreativeModeTab> BIBLIOCRAFT = BCRegistries.CREATIVE_TABS.register(Bibliocraft.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(BCItems.BOOKCASE.get(BibliocraftWoodTypeRegistry.get().get("minecraft:oak"))))
            .title(Component.translatable("itemGroup." + Bibliocraft.MOD_ID))
            .displayItems((display, output) -> {
                addToTab(output, BCItems.BOOKCASE.values());
                addToTab(output, BCItems.LABEL.values());
                addToTab(output, BCItems.FANCY_ARMOR_STAND.values());
                addToTab(output, BCItems.POTION_SHELF.values());
                addToTab(output, BCItems.SHELF.values());
                addToTab(output, BCItems.TABLE.values());
                addToTab(output, BCItems.TOOL_RACK.values());
                addToTab(output, BCItems.DISPLAY_CASE.values());
                addToTab(output, BCItems.SEAT.values());
                addToTab(output, BCItems.SMALL_SEAT_BACK.values());
                addToTab(output, BCItems.RAISED_SEAT_BACK.values());
                addToTab(output, BCItems.FLAT_SEAT_BACK.values());
                addToTab(output, BCItems.TALL_SEAT_BACK.values());
                addToTab(output, BCItems.FANCY_SEAT_BACK.values());
                output.accept(BCItems.COOKIE_JAR.get());
                output.accept(BCItems.DESK_BELL.get());
                output.accept(BCItems.IRON_FANCY_ARMOR_STAND.get());
                for (DyeColor color : DyeColor.values()) {
                    ItemStack stack = new ItemStack(BCItems.SWORD_PEDESTAL.get());
                    BCUtil.setNBTColor(stack, color.getTextColor());
                    output.accept(stack);
                }
                output.accept(BCItems.REDSTONE_BOOK.get());
            })
            .build());

    /**
     * Helper method to add all {@link ItemLike}s in a list to a creative tab.
     *
     * @param output The {@link CreativeModeTab.Output} to add the elements to.
     * @param list   A list of {@link ItemLike}s to add to the {@link CreativeModeTab.Output}.
     */
    private static void addToTab(CreativeModeTab.Output output, Collection<? extends ItemLike> list) {
        list.forEach(output::accept);
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
