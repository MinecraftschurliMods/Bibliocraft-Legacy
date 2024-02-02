package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        BibliocraftDatagenHelper.get().generateItemModels(this);
        withExistingParent("iron_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/iron_inventory"));
        withExistingParent("sword_pedestal", modLoc("block/sword_pedestal"));
        withExistingParent("redstone_book", mcLoc("item/written_book"));
    }
}
