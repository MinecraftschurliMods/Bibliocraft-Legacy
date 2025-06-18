package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrintingTableBindingTypewriterPagesRecipe extends PrintingTableBindingRecipe {
    public static final MapCodec<PrintingTableBindingTypewriterPagesRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(e -> e.ingredient),
            Codec.INT.fieldOf("duration").forGetter(e -> e.duration)
    ).apply(inst, PrintingTableBindingTypewriterPagesRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PrintingTableBindingTypewriterPagesRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, e -> e.ingredient,
            ByteBufCodecs.INT, e -> e.duration,
            PrintingTableBindingTypewriterPagesRecipe::new);
    private final Ingredient ingredient;

    public PrintingTableBindingTypewriterPagesRecipe(Ingredient ingredient, int duration) {
        super(new ItemStack(Items.WRITTEN_BOOK), duration);
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(PrintingTableRecipeInput input, Level level) {
        if (input.left().isEmpty()) return false;
        if (input.right().isEmpty()) return false;
        if (!ingredient.test(input.right())) return false;
        for (ItemStack stack : input.left()) {
            if (!stack.isEmpty() && !stack.has(BCDataComponents.TYPEWRITER_PAGE)) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(PrintingTableRecipeInput input, HolderLookup.Provider registries) {
        ItemStack result = getResultItem(registries).copy();
        List<Filterable<Component>> list = input
                .left()
                .stream()
                .filter(e -> e.has(BCDataComponents.TYPEWRITER_PAGE))
                .map(e -> BCUtil.nonNull(e.get(BCDataComponents.TYPEWRITER_PAGE)))
                .<Filterable<Component>>map(e -> Filterable.passThrough(Component.literal(String.join("", e.lines()))))
                .toList();
        result.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(Filterable.passThrough(""), "", 0, list, false));
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipes.PRINTING_TABLE_BINDING_TYPEWRITER_PAGES.get();
    }

    public static class Builder extends PrintingTableRecipe.Builder {
        private final Ingredient ingredient;

        public Builder(Ingredient ingredient, int duration) {
            super(ItemStack.EMPTY, duration);
            this.ingredient = ingredient;
        }

        @Override
        public PrintingTableRecipe build() {
            return new PrintingTableBindingTypewriterPagesRecipe(ingredient, duration);
        }
    }
}
