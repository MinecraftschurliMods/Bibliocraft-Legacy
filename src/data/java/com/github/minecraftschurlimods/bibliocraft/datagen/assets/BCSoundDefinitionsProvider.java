package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class BCSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public BCSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, BibliocraftApi.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(BCSoundEvents.DESK_BELL.value(), SoundDefinition.definition().with(
                sound(ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "desk_bell_1")),
                sound(ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "desk_bell_2")),
                sound(ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "desk_bell_3")),
                sound(ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "desk_bell_4"))
        ));
    }
}
