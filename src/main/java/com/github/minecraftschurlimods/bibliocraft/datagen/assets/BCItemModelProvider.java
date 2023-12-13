package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class BCItemModelProvider extends ItemModelProvider {
    private final Function<WoodType, ResourceLocation> TYPE_TO_PLANKS = wood -> mcLoc("block/" + wood.name() + "_planks");

    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        woodenBlock(BCItems.BOOKCASE, "bookcase");
        BCItems.FANCY_ARMOR_STAND.map().forEach((k, v) -> withExistingParent(k.name() + "_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/inventory")).texture("texture", TYPE_TO_PLANKS.apply(k)));
        withExistingParent("iron_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/iron_inventory"));
        woodenBlock(BCItems.POTION_SHELF, "potion_shelf");
        woodenBlock(BCItems.SHELF, "shelf");
        woodenBlock(BCItems.TOOL_RACK, "tool_rack");
    }

    /**
     * Adds item models for all variants of a {@link WoodTypeDeferredHolder}.
     *
     * @param holder The {@link WoodTypeDeferredHolder} to add the translations for.
     * @param suffix The suffix of the item model name.
     */
    private void woodenBlock(WoodTypeDeferredHolder<Item, ?> holder, String suffix) {
        holder.map().forEach((k, v) -> {
            String name = k.name() + "_" + suffix;
            withExistingParent(name, modLoc("block/" + name));
        });
    }
}
