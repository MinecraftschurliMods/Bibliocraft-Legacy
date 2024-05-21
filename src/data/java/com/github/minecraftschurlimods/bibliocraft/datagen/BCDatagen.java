package com.github.minecraftschurlimods.bibliocraft.datagen;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.datagen.assets.BCBlockStateProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.assets.BCEnglishLanguageProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.assets.BCItemModelProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.assets.BCSoundDefinitionsProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.data.BCBlockTagsProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.data.BCItemTagsProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.data.BCLootTableProvider;
import com.github.minecraftschurlimods.bibliocraft.datagen.data.BCRecipeProvider;
import com.mojang.datafixers.util.Function4;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.commons.lang3.function.TriFunction;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
public final class BCDatagen {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BibliocraftApi.getDatagenHelper().addWoodTypesToGenerateByModid("minecraft");
        DataGenerator.PackGenerator client = generator.getVanillaPack(event.includeClient());
        DataGenerator.PackGenerator server = generator.getVanillaPack(event.includeServer());

        client.addProvider(BCEnglishLanguageProvider::new);
        client.addProvider(wrapWith(BCBlockStateProvider::new, existingFileHelper));
        client.addProvider(wrapWith(BCItemModelProvider::new, existingFileHelper));
        client.addProvider(wrapWith(BCSoundDefinitionsProvider::new, existingFileHelper));

        server.addProvider(BCLootTableProvider::new);
        server.addProvider(BCRecipeProvider::new);
        BCBlockTagsProvider blockTags = server.addProvider(wrapWith(BCBlockTagsProvider::new, lookupProvider, existingFileHelper));
        server.addProvider(wrapWith(BCItemTagsProvider::new, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }

    private static <T extends DataProvider, S> DataProvider.Factory<T> wrapWith(BiFunction<PackOutput, S, T> factory, S s) {
        return (output) -> factory.apply(output, s);
    }

    private static <T extends DataProvider, S, U> DataProvider.Factory<T> wrapWith(TriFunction<PackOutput, S, U, T> factory, S s, U u) {
        return (output) -> factory.apply(output, s, u);
    }

    private static <T extends DataProvider, S, U, V> DataProvider.Factory<T> wrapWith(Function4<PackOutput, S, U, V, T> factory, S s, U u, V v) {
        return (output) -> factory.apply(output, s, u, v);
    }
}
