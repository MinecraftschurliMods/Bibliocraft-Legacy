package at.minecraftschurli.mods.bibliocraft.datagen;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.api.datagen.BibliocraftDatagenHelper;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCEnglishLanguageProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCModelProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.assets.BCSoundDefinitionsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCBlockTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCDataMapProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCEnchantmentTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCItemTagsProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCLootTableProvider;
import at.minecraftschurli.mods.bibliocraft.datagen.data.BCRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@EventBusSubscriber(modid = BibliocraftApi.MOD_ID)
public final class BCDatagen {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        boolean includeClient = true;
        boolean includeServer = true;
        DataGenerator.PackGenerator clientPack = generator.getVanillaPack(includeClient);
        DataGenerator.PackGenerator serverPack = generator.getVanillaPack(includeServer);

        BCEnglishLanguageProvider languageProvider = clientPack.addProvider(BCEnglishLanguageProvider::new);
        clientPack.addProvider(BCModelProvider::new);
        clientPack.addProvider(BCSoundDefinitionsProvider::new);

        serverPack.addProvider(wrap(lookupProvider, BCLootTableProvider::new));
        serverPack.addProvider(wrap(lookupProvider, BCRecipeProvider.Runner::new));
        BCBlockTagsProvider blockTagsProvider = serverPack.addProvider(wrap(lookupProvider, BCBlockTagsProvider::new));
        BCItemTagsProvider itemTagsProvider = serverPack.addProvider(wrap(lookupProvider, BCItemTagsProvider::new));
        serverPack.addProvider(wrap(lookupProvider, BCEnchantmentTagsProvider::new));
        serverPack.addProvider(wrap(lookupProvider, BCDataMapProvider::new));

        BibliocraftDatagenHelper helper = BibliocraftApi.getDatagenHelper();
        helper.addWoodTypesToGenerateByModid("minecraft");
        helper.generateAll(BibliocraftApi.MOD_ID, lookupProvider, clientPack, serverPack, languageProvider, blockTagsProvider::tag, itemTagsProvider::tag);
    }

    private static <T extends DataProvider, P1> DataProvider.Factory<T> wrap(P1 p1, BiFunction<PackOutput, P1, T> factory) {
        return output -> factory.apply(output, p1);
    }
}
