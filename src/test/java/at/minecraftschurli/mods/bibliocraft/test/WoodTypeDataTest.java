package at.minecraftschurli.mods.bibliocraft.test;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/// Checks the content that is generated at runtime for the wood types: recipes, tags and loot tables.
@ExtendWith(EphemeralTestServerProvider.class)
public class WoodTypeDataTest {
    /// Test that the recipes of an oak wood type exist, both plain and colored.
    @Test
    public void testRecipesGenerated(MinecraftServer server) {
        RecipeManager recipes = server.getRecipeManager();
        assertTrue(recipes.byKey(recipeKey("wood/oak/bookcase")).isPresent(), "bibliocraft:wood/oak/bookcase is missing");
        assertTrue(recipes.byKey(recipeKey("color/red/wood/oak/display_case")).isPresent(), "bibliocraft:color/red/wood/oak/display_case is missing");
    }

    /// Test that the oak bookcase is part of the bookcase item and block tags.
    @Test
    public void testTagsGenerated(MinecraftServer server) {
        BibliocraftWoodType oak = oak();
        Item item = BCItems.BOOKCASE.get(oak);
        assertTrue(item.builtInRegistryHolder().is(BCTags.Items.BOOKCASES), "Oak bookcase item is not in " + BCTags.Items.BOOKCASES);
        Block block = BCBlocks.BOOKCASE.get(oak);
        assertTrue(block.builtInRegistryHolder().is(BCTags.Blocks.BOOKCASES), "Oak bookcase block is not in " + BCTags.Blocks.BOOKCASES);
    }

    /// Test that the oak bookcase has a loot table.
    @Test
    public void testLootTableGenerated(MinecraftServer server) {
        Block block = BCBlocks.BOOKCASE.get(oak());
        ResourceKey<LootTable> lootTable = block.getLootTable().orElseThrow(() -> new AssertionError("Oak bookcase has no loot table key"));
        assertTrue(server.reloadableRegistries().lookup().lookupOrThrow(Registries.LOOT_TABLE).get(lootTable).isPresent(), lootTable.identifier() + " is missing");
    }

    private static BibliocraftWoodType oak() {
        BibliocraftWoodType oak = BibliocraftApi.getWoodTypeRegistry().get("oak");
        assertNotNull(oak, "Oak WoodType is not registered");
        return oak;
    }

    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(BibliocraftApi.MOD_ID, path));
    }
}
