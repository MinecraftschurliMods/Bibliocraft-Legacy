package at.minecraftschurli.mods.bibliocraft.test;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodTypeRegistry;
import at.minecraftschurli.mods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.server.MinecraftServer;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EphemeralTestServerProvider.class)
public class BibliocraftApiTest {
    /// Test that the BibliocraftWoodTypeRegistry is available via the BibliocraftApi
    @Test
    public void testWoodTypeRegistryAvailable(MinecraftServer server) {
        BibliocraftWoodTypeRegistry woodTypeRegistry = BibliocraftApi.getWoodTypeRegistry();
        assertNotNull(woodTypeRegistry, "BibliocraftWoodTypeRegistry is not available");
        assertInstanceOf(BibliocraftWoodTypeRegistryImpl.class, woodTypeRegistry, "BibliocraftWoodTypeRegistry implementation is replaced");
        assertNotNull(woodTypeRegistry.get("oak"), "Oak WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("spruce"), "Spruce WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("birch"), "Birch WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("jungle"), "Jungle WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("acacia"), "Acacia WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("dark_oak"), "Dark Oak WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("crimson"), "Crimson WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("warped"), "Warped WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("mangrove"), "Mangrove WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("bamboo"), "Bamboo WoodType is not registered");
        assertNotNull(woodTypeRegistry.get("cherry"), "Cherry WoodType is not registered");
    }
}
