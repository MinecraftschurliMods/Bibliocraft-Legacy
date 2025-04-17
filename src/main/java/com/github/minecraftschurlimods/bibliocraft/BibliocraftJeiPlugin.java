package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.holder.ColoredDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.holder.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupingDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.holder.WoodTypeDeferredHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@JeiPlugin
public final class BibliocraftJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = BCUtil.modLoc("jei_plugin");
    private static final Lazy<BibliocraftWoodType> OAK = Lazy.of(() -> BibliocraftApi.getWoodTypeRegistry().get(BCUtil.mcLoc("oak")));
    private static final DyeColor WHITE = DyeColor.WHITE;
    private static final Component ALL_COLORS = Component.translatable(Translations.ALL_COLORS_KEY);
    private static final Component ALL_COLORS_AND_WOOD_TYPES = Component.translatable(Translations.ALL_COLORS_AND_WOOD_TYPES_KEY);
    private static final Component ALL_WOOD_TYPES = Component.translatable(Translations.ALL_WOOD_TYPES_KEY);
    private static final Lazy<List<WoodTypeDeferredHolder<Item, ?>>> WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.BOOKCASE, BCItems.FANCY_ARMOR_STAND, BCItems.FANCY_CLOCK, BCItems.FANCY_CRAFTER, BCItems.GRANDFATHER_CLOCK, BCItems.LABEL, BCItems.POTION_SHELF, BCItems.SHELF, BCItems.TABLE, BCItems.TOOL_RACK));
    private static final Lazy<List<ColoredDeferredHolder<Item, ?>>> COLORED_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.FANCY_GOLD_LAMP, BCItems.FANCY_IRON_LAMP, BCItems.FANCY_GOLD_LANTERN, BCItems.FANCY_IRON_LANTERN));
    private static final Lazy<List<ColoredWoodTypeDeferredHolder<Item, ?>>> COLORED_WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.DISPLAY_CASE, BCItems.SEAT, BCItems.SMALL_SEAT_BACK, BCItems.RAISED_SEAT_BACK, BCItems.FLAT_SEAT_BACK, BCItems.TALL_SEAT_BACK, BCItems.FANCY_SEAT_BACK));

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(BCItems.SWORD_PEDESTAL.get(), ColorSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!Config.JEI_SHOW_WOOD_TYPES.get()) {
            for (WoodTypeDeferredHolder<Item, ?> holder : WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                registration.addIngredientInfo(holder.get(OAK.get()), ALL_WOOD_TYPES);
            }
        }
        if (!Config.JEI_SHOW_COLOR_TYPES.get()) {
            if (!Config.JEI_SHOW_WOOD_TYPES.get()) {
                for (ColoredWoodTypeDeferredHolder<Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    registration.addIngredientInfo(holder.get(OAK.get(), WHITE), ALL_COLORS_AND_WOOD_TYPES);
                }
            } else {
                for (ColoredWoodTypeDeferredHolder<Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    for (BibliocraftWoodType woodType : BibliocraftApi.getWoodTypeRegistry().getAll()) {
                        registration.addIngredientInfo(holder.get(woodType, WHITE), ALL_COLORS);
                    }
                }
            }
            for (ColoredDeferredHolder<Item, ?> holder : COLORED_DEFERRED_HOLDERS.get()) {
                registration.addIngredientInfo(holder.get(WHITE), ALL_COLORS);
            }
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        if (!Config.JEI_SHOW_WOOD_TYPES.get()) {
            for (WoodTypeDeferredHolder<Item, ?> holder : WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                removeAllExcept(jeiRuntime, holder, holder.get(OAK.get()));
            }
        }
        if (!Config.JEI_SHOW_COLOR_TYPES.get()) {
            if (!Config.JEI_SHOW_WOOD_TYPES.get()) {
                for (ColoredWoodTypeDeferredHolder<Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    removeAllExcept(jeiRuntime, holder, holder.get(OAK.get(), WHITE));
                }
            } else {
                for (ColoredWoodTypeDeferredHolder<Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                    for (BibliocraftWoodType woodType : BibliocraftApi.getWoodTypeRegistry().getAll()) {
                        removeAllExcept(jeiRuntime, holder, holder.get(woodType, WHITE));
                    }
                }
            }
            for (ColoredDeferredHolder<Item, ?> holder : COLORED_DEFERRED_HOLDERS.get()) {
                removeAllExcept(jeiRuntime, holder, holder.get(WHITE));
            }
        }
    }

    private void removeAllExcept(IJeiRuntime jeiRuntime, GroupingDeferredHolder<Item, ?> holder, Item except) {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, holder.values().stream().filter(e -> e != except).map(ItemStack::new).toList());
    }

    @SuppressWarnings("DataFlowIssue")
    private static class ColorSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
        public static final ColorSubtypeInterpreter INSTANCE = new ColorSubtypeInterpreter();
        
        private ColorSubtypeInterpreter() {}
        
        @Override
        @Nullable
        public Object getSubtypeData(ItemStack ingredient, UidContext context) {
            return ingredient.has(DataComponents.DYED_COLOR) ? ingredient.get(DataComponents.DYED_COLOR).rgb() : null;
        }

        @Override
        public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
            return ingredient.has(DataComponents.DYED_COLOR) ? String.valueOf(ingredient.get(DataComponents.DYED_COLOR).rgb()) : "";
        }
    }
}
