/*
package com.github.minecraftschurlimods.bibliocraft.test;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.level.block.state.properties.WoodType;

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
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.OAK), "Oak WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.SPRUCE), "Spruce WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.BIRCH), "Birch WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.JUNGLE), "Jungle WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.ACACIA), "Acacia WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.DARK_OAK), "Dark Oak WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.CRIMSON), "Crimson WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.WARPED), "Warped WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.MANGROVE), "Mangrove WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.BAMBOO), "Bamboo WoodType is not registered");
        GametestAssertions.assertNotNull(woodTypeRegistry.get(WoodType.CHERRY), "Cherry WoodType is not registered");
        helper.succeed();
    }
}
*/
