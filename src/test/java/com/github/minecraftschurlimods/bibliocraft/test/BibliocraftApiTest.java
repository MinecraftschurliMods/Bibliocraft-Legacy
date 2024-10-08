package com.github.minecraftschurlimods.bibliocraft.test;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BibliocraftApiTest {
    @Test
    @DisplayName("Test that the BibliocraftDatagenHelper is available via the BibliocraftApi")
    public void testDatagenHelperAvailable() {
        BibliocraftDatagenHelper datagenHelper = BibliocraftApi.getDatagenHelper();
        Assertions.assertNotNull(datagenHelper, "BibliocraftDatagenHelper is not available");
        Assertions.assertSame(BibliocraftDatagenHelperImpl.class, datagenHelper.getClass(), "BibliocraftDatagenHelper implementation is replaced");
    }

    @Test
    @DisplayName("Test that the BibliocraftWoodTypeRegistry is available via the BibliocraftApi")
    public void testWoodTypeRegistryAvailable() {
        BibliocraftWoodTypeRegistry woodTypeRegistry = BibliocraftApi.getWoodTypeRegistry();
        Assertions.assertNotNull(woodTypeRegistry, "BibliocraftWoodTypeRegistry is not available");
        Assertions.assertSame(BibliocraftWoodTypeRegistryImpl.class, woodTypeRegistry.getClass(), "BibliocraftWoodTypeRegistry implementation is replaced");
        Assertions.assertNotNull(woodTypeRegistry.get("oak"), "Oak WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("spruce"), "Spruce WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("birch"), "Birch WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("jungle"), "Jungle WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("acacia"), "Acacia WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("dark_oak"), "Dark Oak WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("crimson"), "Crimson WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("warped"), "Warped WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("mangrove"), "Mangrove WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("bamboo"), "Bamboo WoodType is not registered");
        Assertions.assertNotNull(woodTypeRegistry.get("cherry"), "Cherry WoodType is not registered");
    }
}
