package at.minecraftschurli.mods.bibliocraft.datagen;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCEnglishLanguageProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCModelProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCSoundDefinitionsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCBlockTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCDataMapProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCEnchantmentTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCItemTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCLootTableProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCRecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = BibliocraftApi.MOD_ID)
public final class BCDatagen {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Client event) {
        createClientProviders(event);
        createServerProviders(event);
    }

    private static void createClientProviders(GatherDataEvent event) {
        event.createProvider(BCEnglishLanguageProvider::new);
        event.createProvider(BCModelProvider::new);
        event.createProvider(BCSoundDefinitionsProvider::new);
    }

    private static void createServerProviders(GatherDataEvent event) {
        event.createProvider(BCLootTableProvider::new);
        event.createProvider(BCRecipeProvider.Runner::new);
        event.createProvider(BCBlockTagsProvider::new);
        event.createProvider(BCItemTagsProvider::new);
        event.createProvider(BCEnchantmentTagsProvider::new);
        event.createProvider(BCDataMapProvider::new);
    }
}
