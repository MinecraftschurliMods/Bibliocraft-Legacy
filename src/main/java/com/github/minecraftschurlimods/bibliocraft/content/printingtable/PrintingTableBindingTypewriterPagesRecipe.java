package com.github.minecraftschurlimods.bibliocraft.content.printingtable;

import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPage;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipes;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.datafixers.util.Pair;
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

import java.util.List;

public class PrintingTableBindingTypewriterPagesRecipe extends PrintingTableBindingRecipe {
    public static final MapCodec<PrintingTableBindingTypewriterPagesRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(e -> e.ingredient),
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
        ItemStack result = this.result.copy();
        List<Filterable<Component>> list = input
                .left()
                .stream()
                .filter(e -> e.has(BCDataComponents.TYPEWRITER_PAGE))
                .map(e -> concatTypewriterPageText(BCUtil.nonNull(e.get(BCDataComponents.TYPEWRITER_PAGE))))
                .toList();
        result.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(Filterable.passThrough(""), "", 0, list, false));
        return result;
    }

    @Override
    public RecipeSerializer<PrintingTableBindingTypewriterPagesRecipe> getSerializer() {
        return BCRecipes.PRINTING_TABLE_BINDING_TYPEWRITER_PAGES.get();
    }

    @Override
    public ItemStack postProcess(ItemStack result, PrintingTableBlockEntity blockEntity) {
        Component name = blockEntity.getPlayerName();
        if (name != null && result.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
            WrittenBookContent content = BCUtil.nonNull(result.get(DataComponents.WRITTEN_BOOK_CONTENT));
            result.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(content.title(), name.getString(), content.generation(), content.pages(), content.resolved()));
        }
        return result;
    }

    @Override
    public Pair<List<Ingredient>, Ingredient> getDisplayIngredients() {
        return Pair.of(List.of(Ingredient.of(BCItems.TYPEWRITER_PAGE)), ingredient);
    }

    private Filterable<Component> concatTypewriterPageText(TypewriterPage page) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < page.lines().size(); i++) {
            String line = page.lines().get(i);
            if (!line.isEmpty()) {
                builder.append(line);
                if (line.length() < TypewriterPage.MAX_LINES) {
                    builder.append('\n');
                }
            } else {
                builder.append('\n');
            }
        }
        return Filterable.passThrough(Component.literal(builder.toString()));
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
