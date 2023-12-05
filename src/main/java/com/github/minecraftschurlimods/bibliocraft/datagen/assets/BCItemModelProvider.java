package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        woodenBlock(BCItems.BOOKCASE, "bookcase");
        woodenBlock(BCItems.POTION_SHELF, "potion_shelf");
        woodenBlock(BCItems.SHELF, "shelf");
        woodenBlock(BCItems.TOOL_RACK, "tool_rack");
    }

    private void woodenBlock(WoodTypeDeferredHolder<Item, ?> holder, String suffix) {
        holder.map().forEach((k, v) -> {
            String name = k.name() + "_" + suffix;
            withExistingParent(name, new ResourceLocation(Bibliocraft.MOD_ID, "block/" + name));
        });
    }
}
