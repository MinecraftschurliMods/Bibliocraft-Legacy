package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bibliocraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        woodenBlock(BCItems.BOOKCASE, "bookcase");
        BCItems.DISPLAY_CASE.map().forEach((wood, colorHolder) -> colorHolder.map().forEach((color, holder) -> withExistingParent(holder.getId().getPath(), modLoc("block/" + holder.getId().getPath() + "_open"))));
        BCItems.FANCY_ARMOR_STAND.types().forEach(wood -> withExistingParent(wood.name() + "_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/inventory")).texture("texture", BCBlockStateProvider.TYPE_TO_PLANKS.apply(wood)));
        woodenBlock(BCItems.POTION_SHELF, "potion_shelf");
        woodenBlock(BCItems.SHELF, "shelf");
        woodenBlock(BCItems.TOOL_RACK, "tool_rack");
        withExistingParent("iron_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/iron_inventory"));
        withExistingParent("sword_pedestal", modLoc("block/sword_pedestal"));
        withExistingParent("redstone_book", mcLoc("item/written_book"));
    }

    /**
     * Adds item models for all variants of a {@link WoodTypeDeferredHolder}.
     *
     * @param holder The {@link WoodTypeDeferredHolder} to add the translations for.
     * @param suffix The suffix of the item model name.
     */
    private void woodenBlock(WoodTypeDeferredHolder<Item, ?> holder, String suffix) {
        woodenBlock(holder, suffix, name -> modLoc("block/" + name));
    }

    /**
     * Adds item models for all variants of a {@link WoodTypeDeferredHolder}.
     *
     * @param holder        The {@link WoodTypeDeferredHolder} to add the translations for.
     * @param suffix        The suffix of the item model name.
     * @param parentFactory A {@link Function} mapping from an item name to a parent id.
     */
    private void woodenBlock(WoodTypeDeferredHolder<Item, ?> holder, String suffix, Function<String, ResourceLocation> parentFactory) {
        holder.map().forEach((k, v) -> {
            String name = k.name() + "_" + suffix;
            withExistingParent(name, parentFactory.apply(name));
        });
    }
}
