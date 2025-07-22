package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DataComponentPresentIngredient implements ICustomIngredient {
    public static final MapCodec<DataComponentPresentIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            HolderSetCodec.create(Registries.DATA_COMPONENT_TYPE, BuiltInRegistries.DATA_COMPONENT_TYPE.holderByNameCodec(), false).fieldOf("data_components").forGetter(e -> e.dataComponents),
            HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).optionalFieldOf("items").forGetter(e -> e.items)
    ).apply(inst, DataComponentPresentIngredient::new));
    private final HolderSet<DataComponentType<?>> dataComponents;
    private final Optional<HolderSet<Item>> items;

    public DataComponentPresentIngredient(HolderSet<DataComponentType<?>> dataComponents, Optional<HolderSet<Item>> items) {
        this.dataComponents = dataComponents;
        this.items = items;
    }

    public DataComponentPresentIngredient(HolderSet<DataComponentType<?>> dataComponents) {
        this(dataComponents, Optional.empty());
    }

    @Override
    public boolean test(ItemStack stack) {
        return items.map(e -> e.contains(stack.getItemHolder())).orElse(true) && dataComponents.stream().map(Holder::value).allMatch(stack::has);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return null;
    }
}
