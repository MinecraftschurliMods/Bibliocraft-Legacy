package com.github.minecraftschurlimods.bibliocraft.client.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("DataFlowIssue")
public class DyedColorSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final DyedColorSubtypeInterpreter INSTANCE = new DyedColorSubtypeInterpreter();

    private DyedColorSubtypeInterpreter() {
    }

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
