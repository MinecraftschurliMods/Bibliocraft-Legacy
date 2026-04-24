package at.minecraftschurli.mods.bibliocraft.init;

import at.minecraftschurli.mods.bibliocraft.content.bigbook.BigBookCloningRecipe;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.EnchantmentLevelsNumberProvider;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableBindingTypewriterPagesRecipe;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableCloningRecipe;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableCloningWithEnchantmentsRecipe;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableMergingRecipe;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableRecipe;
import at.minecraftschurli.mods.bibliocraft.content.typewriter.TypewriterPageCloningRecipe;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface BCRecipes {
    DeferredHolder<RecipeType<?>, RecipeType<PrintingTableRecipe>> PRINTING_TABLE = BCRegistries.RECIPE_TYPES.register("printing_table", () -> RecipeType.simple(BCUtil.bcLoc("printing_table")));

    Supplier<RecipeSerializer<BigBookCloningRecipe>> BIG_BOOK_CLONING =
            BCRegistries.RECIPE_SERIALIZERS.register("big_book_cloning", serializer(BigBookCloningRecipe.CODEC, BigBookCloningRecipe.STREAM_CODEC));
    Supplier<RecipeSerializer<TypewriterPageCloningRecipe>> TYPEWRITER_PAGE_CLONING =
            BCRegistries.RECIPE_SERIALIZERS.register("typewriter_page_cloning", serializer(TypewriterPageCloningRecipe.CODEC, TypewriterPageCloningRecipe.STREAM_CODEC));
    Supplier<RecipeSerializer<PrintingTableBindingTypewriterPagesRecipe>> PRINTING_TABLE_BINDING_TYPEWRITER_PAGES =
            BCRegistries.RECIPE_SERIALIZERS.register("printing_table_binding_typewriter_pages", serializer(PrintingTableBindingTypewriterPagesRecipe.CODEC, PrintingTableBindingTypewriterPagesRecipe.STREAM_CODEC));
    Supplier<RecipeSerializer<PrintingTableCloningRecipe>> PRINTING_TABLE_CLONING =
            BCRegistries.RECIPE_SERIALIZERS.register("printing_table_cloning", serializer(PrintingTableCloningRecipe.CODEC, PrintingTableCloningRecipe.STREAM_CODEC));
    Supplier<RecipeSerializer<PrintingTableCloningWithEnchantmentsRecipe>> PRINTING_TABLE_CLONING_WITH_ENCHANTMENTS =
            BCRegistries.RECIPE_SERIALIZERS.register("printing_table_cloning_with_enchantments", serializer(PrintingTableCloningWithEnchantmentsRecipe.CODEC, PrintingTableCloningWithEnchantmentsRecipe.STREAM_CODEC));
    Supplier<RecipeSerializer<PrintingTableMergingRecipe>> PRINTING_TABLE_MERGING =
            BCRegistries.RECIPE_SERIALIZERS.register("printing_table_merging", serializer(PrintingTableMergingRecipe.CODEC, PrintingTableMergingRecipe.STREAM_CODEC));

    Supplier<MapCodec<EnchantmentLevelsNumberProvider>> ENCHANTMENT_LEVELS_NUMBER_PROVIDER =
            BCRegistries.NUMBER_PROVIDERS.register("enchantment_levels", () -> EnchantmentLevelsNumberProvider.CODEC);

    Supplier<RecipeBookCategory> PRINTING_TABLE_RECIPE_CATEGORY =
            BCRegistries.RECIPE_CATEGORIES.register("printing_table", RecipeBookCategory::new);

    /// Returns a [Supplier] for a [RecipeSerializer] created using the given [MapCodec] and [StreamCodec].
    ///
    /// @param codec       The [MapCodec] to use.
    /// @param streamCodec The [StreamCodec] to use.
    /// @param <T>         The generic type of the [RecipeSerializer].
    /// @return A [Supplier] for a [RecipeSerializer].
    static <T extends Recipe<?>> Supplier<RecipeSerializer<T>> serializer(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return () -> new RecipeSerializer<>(codec, streamCodec);
    }

    /// Empty method, called by [BCRegistries#init()] to classload this class.
    @ApiStatus.Internal
    static void init() {}
}
