package com.github.minecraftschurlimods.bibliocraft.test;


import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodTypeRegistry;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftWoodTypeRegistryImpl;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BibliocraftApiTest {
    @Test
    @DisplayName("Test that the BibliocraftDatagenHelper is available via the BibliocraftApi")
    public void testDatagenHelperAvailable() {
        BibliocraftDatagenHelper datagenHelper = BibliocraftApi.getDatagenHelper();
        assertNotNull(datagenHelper, "BibliocraftDatagenHelper is not available");
        assertSame(BibliocraftDatagenHelperImpl.class, datagenHelper.getClass(), "BibliocraftDatagenHelper implementation is replaced");
    }

    @Test
    @DisplayName("Test that the BibliocraftWoodTypeRegistry is available via the BibliocraftApi")
    public void testWoodTypeRegistryAvailable() {
        BibliocraftWoodTypeRegistry woodTypeRegistry = BibliocraftApi.getWoodTypeRegistry();
        assertNotNull(woodTypeRegistry, "BibliocraftWoodTypeRegistry is not available");
        assertSame(BibliocraftWoodTypeRegistryImpl.class, woodTypeRegistry.getClass(),  "BibliocraftWoodTypeRegistry implementation is replaced");
        assertNotNull(woodTypeRegistry.get(WoodType.OAK), "Oak WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.SPRUCE), "Spruce WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.BIRCH), "Birch WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.JUNGLE), "Jungle WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.ACACIA), "Acacia WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.DARK_OAK), "Dark Oak WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.CRIMSON), "Crimson WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.WARPED), "Warped WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.MANGROVE), "Mangrove WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.BAMBOO), "Bamboo WoodType is not registered");
        assertNotNull(woodTypeRegistry.get(WoodType.CHERRY), "Cherry WoodType is not registered");
    }
}
