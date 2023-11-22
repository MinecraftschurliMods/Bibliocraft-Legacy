package com.github.minecraftschurlimods.bibliocraft.datagen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
public final class BCDatagen {
    @SubscribeEvent
    static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new BCEnglishLanguageProvider(output));
        generator.addProvider(event.includeClient(), new BCBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new BCItemModelProvider(output, existingFileHelper));
    }
}
