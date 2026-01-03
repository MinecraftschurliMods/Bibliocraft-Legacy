package com.github.minecraftschurlimods.bibliocraft.client.jei;

import com.github.minecraftschurlimods.bibliocraft.BCConfig;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.client.screen.FancyCrafterScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.PrintingTableScreen;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.CompatUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.holder.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JeiPlugin
public final class BCJeiPlugin implements IModPlugin {
    private static final Identifier UID = BCUtil.bcLoc("jei_plugin");
    private static final Lazy<BibliocraftWoodType> OAK = Lazy.of(() -> BibliocraftApi.getWoodTypeRegistry().get(BCUtil.mcLoc("oak")));
    private static final DyeColor WHITE = DyeColor.WHITE;
    private static final Lazy<List<GroupedHolder<BibliocraftWoodType, Item, ?>>> WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.BOOKCASE, BCItems.FANCY_ARMOR_STAND, BCItems.FANCY_CLOCK, BCItems.FANCY_CRAFTER, BCItems.GRANDFATHER_CLOCK, BCItems.LABEL, BCItems.POTION_SHELF, BCItems.SHELF, BCItems.TABLE, BCItems.TOOL_RACK));
    private static final Lazy<List<GroupedHolder<DyeColor, Item, ?>>> COLORED_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.FANCY_GOLD_LAMP, BCItems.FANCY_IRON_LAMP, BCItems.FANCY_GOLD_LANTERN, BCItems.FANCY_IRON_LANTERN));
    private static final Lazy<List<GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?>>> COLORED_WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.DISPLAY_CASE, BCItems.SEAT, BCItems.SMALL_SEAT_BACK, BCItems.RAISED_SEAT_BACK, BCItems.FLAT_SEAT_BACK, BCItems.TALL_SEAT_BACK, BCItems.FANCY_SEAT_BACK));

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(BCItems.SWORD_PEDESTAL.get(), DyedColorSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PrintingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!BCConfig.JEI_SHOW_WOOD_TYPES.get()) {
            for (GroupedHolder<BibliocraftWoodType, Item, ?> holder : WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                registration.addIngredientInfo(holder.get(OAK.get()), Translations.ALL_WOOD_TYPES);
            }
        }
        if (!BCConfig.JEI_SHOW_COLOR_TYPES.get()) {
            if (!BCConfig.JEI_SHOW_WOOD_TYPES.get()) {
                for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    registration.addIngredientInfo(holder.get(OAK.get(), WHITE), Translations.ALL_COLORS_AND_WOOD_TYPES);
                }
            } else {
                for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    for (BibliocraftWoodType woodType : BibliocraftApi.getWoodTypeRegistry().getAll()) {
                        registration.addIngredientInfo(holder.get(woodType, WHITE), Translations.ALL_COLORS);
                    }
                }
            }
            for (GroupedHolder<DyeColor, Item, ?> holder : COLORED_DEFERRED_HOLDERS.get()) {
                registration.addIngredientInfo(holder.get(WHITE), Translations.ALL_COLORS);
            }
        }
        registration.addRecipes(PrintingTableRecipeCategory.TYPE, ClientUtil.getRecipeMap().byType(BCRecipes.PRINTING_TABLE.get()).stream().toList());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(FancyCrafterMenu.class, BCMenus.FANCY_CRAFTER.get(), RecipeTypes.CRAFTING, 0, 9, 10, 44);
        registration.addRecipeTransferHandler(PrintingTableMenu.class, BCMenus.PRINTING_TABLE.get(), PrintingTableRecipeCategory.TYPE, 0, 10, 11, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (BCConfig.JEI_SHOW_WOOD_TYPES.get()) {
            registration.addCraftingStation(RecipeTypes.CRAFTING, BCItems.FANCY_CRAFTER.values().toArray(ItemLike[]::new));
        } else {
            registration.addCraftingStation(RecipeTypes.CRAFTING, BCItems.FANCY_CRAFTER.get(OAK.get()));
        }
        registration.addCraftingStation(PrintingTableRecipeCategory.TYPE, BCItems.PRINTING_TABLE, BCItems.IRON_PRINTING_TABLE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FancyCrafterScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(PrintingTableScreen.class, 108, 28, 28, 23, PrintingTableRecipeCategory.TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        if (!BCConfig.JEI_SHOW_WOOD_TYPES.get()) {
            for (GroupedHolder<BibliocraftWoodType, Item, ?> holder : WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                removeAllExcept(runtime, holder, holder.get(OAK.get()));
            }
        }
        if (!BCConfig.JEI_SHOW_COLOR_TYPES.get()) {
            if (!BCConfig.JEI_SHOW_WOOD_TYPES.get()) {
                for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    removeAllExcept(runtime, holder, holder.get(OAK.get(), WHITE));
                }
            } else {
                for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    for (BibliocraftWoodType woodType : BibliocraftApi.getWoodTypeRegistry().getAll()) {
                        removeAllExcept(runtime, holder, holder.get(woodType, WHITE));
                    }
                }
            }
            for (GroupedHolder<DyeColor, Item, ?> holder : COLORED_DEFERRED_HOLDERS.get()) {
                removeAllExcept(runtime, holder, holder.get(WHITE));
            }
        }
        if (!CompatUtil.hasSoulCandles()) {
            remove(runtime, Stream.of(BCItems.SOUL_FANCY_GOLD_LANTERN, BCItems.SOUL_FANCY_IRON_LANTERN).map(Supplier::get).map(ItemStack::new).toList());
        }
    }

    private void remove(IJeiRuntime runtime, List<ItemStack> list) {
        runtime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, list);
    }

    private void removeAllExcept(IJeiRuntime runtime, GroupingDeferredHolder<Item, ?> holder, Item except) {
        remove(runtime, holder.values().stream().filter(e -> e != except).map(ItemStack::new).toList());
    }
}
