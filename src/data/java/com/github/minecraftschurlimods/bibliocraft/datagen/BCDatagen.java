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
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
public final class BCDatagen {
    @SubscribeEvent
    private static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BibliocraftApi.getDatagenHelper().addWoodTypesToGenerateByModid("minecraft");

        generator.addProvider(event.includeClient(), new BCEnglishLanguageProvider(output));
        generator.addProvider(event.includeClient(), new BCBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new BCItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new BCSoundDefinitionsProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(), new BCLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new BCRecipeProvider(output, lookupProvider));
        BCBlockTagsProvider blockTags = generator.addProvider(event.includeServer(), new BCBlockTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BCItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }
}
