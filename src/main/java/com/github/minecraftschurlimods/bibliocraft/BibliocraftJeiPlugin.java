package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public final class BibliocraftJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = BCUtil.modLoc("jei_plugin");
    
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
        List<BibliocraftWoodType> woodTypes = BibliocraftApi.getWoodTypeRegistry()
                .getAll()
                .stream()
                .filter(e -> ModList.get().isLoaded(e.getNamespace()))
                .toList();
    }

    private void remove(IRecipeRegistration registration, WoodTypeDeferredHolder<Item, ?> holder, List<BibliocraftWoodType> woodTypesToRemove) {
        registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, woodTypesToRemove
                .stream()
                .map(holder::get)
                .map(ItemStack::new)
                .collect(Collectors.toSet()));
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
