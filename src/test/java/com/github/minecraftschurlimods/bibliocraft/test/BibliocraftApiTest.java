package com.github.minecraftschurlimods.bibliocraft.test;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.ExtendedGameTestHelper;

@ForEachTest(groups = BibliocraftApi.MOD_ID + ".api")
public class BibliocraftApiTest {
    @GameTest
    @EmptyTemplate
    @TestHolder(description = "Test that the BibliocraftDatagenHelper is available via the BibliocraftApi")
    public static void testDatagenHelperAvailable(ExtendedGameTestHelper helper) {
        BibliocraftDatagenHelper datagenHelper = BibliocraftApi.getDatagenHelper();
        GametestAssertions.assertNotNull(datagenHelper, "BibliocraftDatagenHelper is not available");
        GametestAssertions.assertInstance(datagenHelper, BibliocraftDatagenHelperImpl.class, "BibliocraftDatagenHelper implementation is replaced");
        helper.succeed();
    }

    @GameTest
    @EmptyTemplate
    @TestHolder(description = "Test that the BibliocraftWoodTypeRegistry is available via the BibliocraftApi")
    public static void testWoodTypeRegistryAvailable(ExtendedGameTestHelper helper) {
        BibliocraftWoodTypeRegistry woodTypeRegistry = BibliocraftApi.getWoodTypeRegistry();
        GametestAssertions.assertNotNull(woodTypeRegistry, "BibliocraftWoodTypeRegistry is not available");
        GametestAssertions.assertInstance(woodTypeRegistry, BibliocraftWoodTypeRegistryImpl.class, "BibliocraftWoodTypeRegistry implementation is replaced");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("oak"), "Oak WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("spruce"), "Spruce WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("birch"), "Birch WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("jungle"), "Jungle WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("acacia"), "Acacia WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("dark_oak"), "Dark Oak WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("crimson"), "Crimson WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("warped"), "Warped WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("mangrove"), "Mangrove WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("bamboo"), "Bamboo WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get("cherry"), "Cherry WoodType is not registered");
        helper.succeed();
    }
}
