package com.github.minecraftschurlimods.bibliocraft.datagen;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("bookcase", new ResourceLocation(Bibliocraft.MOD_ID, "block/oak_bookcase"));
    }
}
