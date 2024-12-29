package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface BCCreativeTabs {
    Supplier<CreativeModeTab> BIBLIOCRAFT = BCRegistries.CREATIVE_TABS.register(BibliocraftApi.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(BCItems.BOOKCASE.get(BibliocraftApi.getWoodTypeRegistry().get(BCUtil.mcLoc("oak")))))
            .title(Component.translatable("itemGroup." + BibliocraftApi.MOD_ID))
            .withSearchBar()
            .displayItems((display, output) -> {
                addToTab(output, BCItems.BOOKCASE.values());
                addToTab(output, BCItems.FANCY_ARMOR_STAND.values());
                addToTab(output, BCItems.FANCY_CLOCK.values());
                addToTab(output, BCItems.FANCY_CRAFTER.values());
                addToTab(output, BCItems.GRANDFATHER_CLOCK.values());
                addToTab(output, BCItems.LABEL.values());
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
                output.accept(BCItems.CLEAR_FANCY_GOLD_LAMP);
                addToTab(output, BCItems.FANCY_GOLD_LAMP.values());
                output.accept(BCItems.CLEAR_FANCY_IRON_LAMP);
                addToTab(output, BCItems.FANCY_IRON_LAMP.values());
                output.accept(BCItems.CLEAR_FANCY_GOLD_LANTERN);
                addToTab(output, BCItems.FANCY_GOLD_LANTERN.values());
                output.accept(BCItems.CLEAR_FANCY_IRON_LANTERN);
                addToTab(output, BCItems.FANCY_IRON_LANTERN.values());
                output.accept(BCItems.CLIPBOARD);
                output.accept(BCItems.COOKIE_JAR);
                output.accept(BCItems.DESK_BELL);
                output.accept(BCItems.DINNER_PLATE);
                output.accept(BCItems.DISC_RACK);
                output.accept(BCItems.GOLD_CHAIN);
                output.accept(BCItems.GOLD_LANTERN);
                output.accept(BCItems.GOLD_SOUL_LANTERN);
                output.accept(BCItems.IRON_FANCY_ARMOR_STAND);
                for (DyeColor color : DyeColor.values()) {
                    ItemStack stack = new ItemStack(BCItems.SWORD_PEDESTAL.get());
                    stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextureDiffuseColor(), true));
                    output.accept(stack);
                }
                output.accept(BCItems.LOCK_AND_KEY);
                output.accept(BCItems.PLUMB_LINE);
                output.accept(BCItems.REDSTONE_BOOK);
                output.accept(BCItems.SLOTTED_BOOK);
                output.accept(BCItems.STOCKROOM_CATALOG);
                output.accept(BCItems.TAPE_MEASURE);
                output.accept(BCItems.TAPE_REEL);
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
