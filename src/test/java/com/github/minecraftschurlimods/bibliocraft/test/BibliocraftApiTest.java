package com.github.minecraftschurlimods.bibliocraft.test;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.datagen.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.ExtendedGameTestHelper; 

@SuppressWarnings("ConstantValue")
@ForEachTest(groups = BibliocraftApi.MOD_ID + ".api")
public class BibliocraftApiTest {
    @GameTest
    @EmptyTemplate
    @TestHolder(description = "Test that the BibliocraftDatagenHelper is available via the BibliocraftApi")
    public static void testDatagenHelperAvailable(ExtendedGameTestHelper helper) {
        BibliocraftDatagenHelper datagenHelper = BibliocraftApi.getDatagenHelper();
        helper.assertTrue(datagenHelper != null, "BibliocraftDatagenHelper is not available");
        helper.assertTrue(datagenHelper.getClass() == BibliocraftDatagenHelperImpl.class, "BibliocraftDatagenHelper implementation is replaced");
        helper.succeed();
    }

    @GameTest
    @EmptyTemplate
    @TestHolder(description = "Test that the BibliocraftWoodTypeRegistry is available via the BibliocraftApi")
    public static void testWoodTypeRegistryAvailable(ExtendedGameTestHelper helper) {
        BibliocraftWoodTypeRegistry woodTypeRegistry = BibliocraftApi.getWoodTypeRegistry();
        helper.assertTrue(woodTypeRegistry != null, "BibliocraftWoodTypeRegistry is not available");
        helper.assertTrue(woodTypeRegistry.getClass() == BibliocraftWoodTypeRegistryImpl.class, "BibliocraftWoodTypeRegistry implementation is replaced");
        helper.assertTrue(woodTypeRegistry.get("oak") != null, "Oak WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("spruce") != null, "Spruce WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("birch") != null, "Birch WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("jungle") != null, "Jungle WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("acacia") != null, "Acacia WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("dark_oak") != null, "Dark Oak WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("crimson") != null, "Crimson WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("warped") != null, "Warped WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("mangrove") != null, "Mangrove WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("bamboo") != null, "Bamboo WoodType is not registered");
        helper.assertTrue(woodTypeRegistry.get("cherry") != null, "Cherry WoodType is not registered");
        helper.succeed();
    }
}
