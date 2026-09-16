package at.minecraftschurli.mods.bibliocraft.client.jei;

import at.minecraftschurli.mods.bibliocraft.BCConfig;
import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.client.screen.FancyCrafterScreen;
import at.minecraftschurli.mods.bibliocraft.client.screen.PrintingTableScreen;
import at.minecraftschurli.mods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableMenu;
import at.minecraftschurli.mods.bibliocraft.init.BCFluids;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.init.BCRecipes;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.CompatUtil;
import at.minecraftschurli.mods.bibliocraft.util.Translations;
import at.minecraftschurli.mods.bibliocraft.util.holder.CopperSet;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupedHolder;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupingDeferredHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
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
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public final class BCJeiPlugin implements IModPlugin {
    private static final Identifier UID = BCUtil.bcLoc("jei_plugin");
    private static final Lazy<BibliocraftWoodType> OAK = Lazy.of(() -> BCUtil.nonNull(BibliocraftApi.getWoodTypeRegistry().get(BCUtil.mcLoc("oak"))));
    private static final DyeColor WHITE = DyeColor.WHITE;
    private static final Lazy<List<GroupedHolder<BibliocraftWoodType, Item, ?>>> WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.BOOKCASE, BCItems.FANCY_ARMOR_STAND, BCItems.FANCY_CLOCK, BCItems.FANCY_CRAFTER, BCItems.FANCY_SIGN, BCItems.GRANDFATHER_CLOCK, BCItems.LABEL, BCItems.POTION_SHELF, BCItems.SHELF, BCItems.TABLE, BCItems.TOOL_RACK));
    private static final Lazy<List<GroupedHolder<DyeColor, Item, ?>>> COLORED_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.FANCY_GOLD_LAMP, BCItems.FANCY_IRON_LAMP, BCItems.FANCY_GOLD_LANTERN, BCItems.FANCY_IRON_LANTERN));
    private static final Lazy<List<GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?>>> COLORED_WOOD_TYPE_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.DISPLAY_CASE, BCItems.SEAT, BCItems.SMALL_SEAT_BACK, BCItems.RAISED_SEAT_BACK, BCItems.FLAT_SEAT_BACK, BCItems.TALL_SEAT_BACK, BCItems.FANCY_SEAT_BACK));
    private static final Lazy<List<CopperSet<? extends GroupedHolder<DyeColor, Item, ?>, ? extends GroupedHolder<DyeColor, Item, ?>>>> COLORED_COPPER_SET_DEFERRED_HOLDERS =
            Lazy.of(() -> List.of(BCItems.FANCY_COPPER_LAMP, BCItems.FANCY_COPPER_LANTERN));
    private static RecipeMap recipeMap = RecipeMap.EMPTY;

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
        forWoodTypeLists((_, items) -> items.forEach(i -> registration.addIngredientInfo(i, Translations.ALL_WOOD_TYPES)));
        forColorLists((_, items) -> items.forEach(i -> registration.addIngredientInfo(i, Translations.ALL_COLORS)));
        forWoodTypeColorLists((_, items) -> items.forEach(i -> registration.addIngredientInfo(i, Translations.ALL_WOOD_TYPES)), (_, items) -> items.forEach(i -> registration.addIngredientInfo(i, Translations.ALL_COLORS)), (_, items) -> items.forEach(i -> registration.addIngredientInfo(i, Translations.ALL_COLORS_AND_WOOD_TYPES)));
        registration.addRecipes(PrintingTableRecipeCategory.TYPE, recipeMap.byType(BCRecipes.PRINTING_TABLE.get()).stream().toList());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(FancyCrafterMenu.class, BCMenus.FANCY_CRAFTER.get(), RecipeTypes.CRAFTING, 0, 9, 10, 44);
        registration.addRecipeTransferHandler(PrintingTableMenu.class, BCMenus.PRINTING_TABLE.get(), PrintingTableRecipeCategory.TYPE, 0, 10, 11, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RecipeTypes.CRAFTING, new SlotDisplay.TagSlotDisplay(BCTags.Items.FANCY_CRAFTERS));
        registration.addCraftingStation(PrintingTableRecipeCategory.TYPE, BCItems.PRINTING_TABLE, BCItems.IRON_PRINTING_TABLE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FancyCrafterScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(PrintingTableScreen.class, 108, 28, 28, 23, PrintingTableRecipeCategory.TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        forWoodTypeLists((holder, item) -> removeAllExcept(runtime, holder, item));
        forColorLists((holder, item) -> removeAllExcept(runtime, holder, item));
        forWoodTypeColorLists((holder, item) -> removeAllExcept(runtime, holder, item));
        if (!CompatUtil.hasSoulCandles()) {
            remove(runtime, Stream.of(BCItems.SOUL_FANCY_GOLD_LANTERN, BCItems.SOUL_FANCY_IRON_LANTERN).map(Supplier::get).map(ItemStack::new).toList());
        }
        runtime.getIngredientManager().removeIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(BCFluids.EXPERIENCE, 1000)));
    }

    public static void setRecipeMap(RecipeMap recipeMap) {
        BCJeiPlugin.recipeMap = recipeMap;
    }

    private void remove(IJeiRuntime runtime, List<ItemStack> list) {
        runtime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, list);
    }

    private void removeAllExcept(IJeiRuntime runtime, GroupingDeferredHolder<Item, ?> holder, Set<Item> except) {
        remove(runtime, holder.values().stream().filter(e -> !except.contains(e)).map(ItemStack::new).toList());
    }

    private void forWoodTypeLists(BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> consumer) {
        if (BCConfig.JEI_SHOW_WOOD_TYPES.get()) return;
        for (GroupedHolder<BibliocraftWoodType, Item, ?> holder : WOOD_TYPE_DEFERRED_HOLDERS.get()) {
            consumer.accept(holder, Set.of(holder.get(OAK.get())));
        }
    }

    private void forColorLists(BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> consumer) {
        if (BCConfig.JEI_SHOW_COLOR_TYPES.get()) return;
        for (GroupedHolder<DyeColor, Item, ?> holder : COLORED_DEFERRED_HOLDERS.get()) {
            consumer.accept(holder, Set.of(holder.get(WHITE)));
        }
        for (CopperSet<? extends GroupedHolder<DyeColor, Item, ?>, ? extends GroupedHolder<DyeColor, Item, ?>> set : COLORED_COPPER_SET_DEFERRED_HOLDERS.get()) {
            for (GroupedHolder<DyeColor, Item, ? extends Item> holder : set.getWaxed()) {
                consumer.accept(holder, Set.of(holder.get(WHITE)));
            }
            for (GroupedHolder<DyeColor, Item, ? extends Item> holder : set.getWeathering()) {
                consumer.accept(holder, Set.of(holder.get(WHITE)));
            }
        }
    }

    private void forWoodTypeColorLists(BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> woodConsumer, BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> colorConsumer, BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> bothConsumer) {
        boolean dontShowWoodTypes = !BCConfig.JEI_SHOW_WOOD_TYPES.get();
        boolean dontShowColorTypes = !BCConfig.JEI_SHOW_COLOR_TYPES.get();
        if (dontShowWoodTypes && dontShowColorTypes) {
            for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                bothConsumer.accept(holder, Set.of(holder.get(OAK.get(), WHITE)));
            }
        } else if (dontShowWoodTypes) {
            for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                woodConsumer.accept(holder, Arrays.stream(DyeColor.values()).map(e -> holder.get(OAK.get(), e)).collect(Collectors.toSet()));
            }
        } else if (dontShowColorTypes) {
            for (GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Item, ?> holder : COLORED_WOOD_TYPE_DEFERRED_HOLDERS.get()) {
                colorConsumer.accept(holder, BibliocraftApi.getWoodTypeRegistry().getAll().stream().map(e -> holder.get(e, WHITE)).collect(Collectors.toSet()));
            }
        }
    }

    private void forWoodTypeColorLists(BiConsumer<GroupingDeferredHolder<Item, ?>, Set<Item>> consumer) {
        forWoodTypeColorLists(consumer, consumer, consumer);
    }
}
