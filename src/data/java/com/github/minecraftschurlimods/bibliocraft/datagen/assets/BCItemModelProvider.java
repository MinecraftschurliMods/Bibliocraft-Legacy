package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BCItemModelProvider extends ItemModelProvider {
    public BCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BibliocraftApi.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        BibliocraftApi.getDatagenHelper().generateItemModels(this);
        basicItem(BCItems.CLIPBOARD.get());
        basicItem(BCItems.GOLD_CHAIN.get());
        basicItem(BCItems.GOLD_LANTERN.get());
        basicItem(BCItems.GOLD_SOUL_LANTERN.get());
        basicItem(BCItems.LOCK_AND_KEY.get());
        basicItem(BCItems.PLUMB_LINE.get());
        withExistingParent("fancy_gold_lamp",    modLoc("block/fancy_gold_lamp_standing"));
        withExistingParent("fancy_iron_lamp",    modLoc("block/fancy_iron_lamp_standing"));
        withExistingParent("fancy_gold_lantern", modLoc("block/fancy_gold_lantern_standing"));
        withExistingParent("fancy_iron_lantern", modLoc("block/fancy_iron_lantern_standing"));
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            withExistingParent(name + "_fancy_gold_lamp",    modLoc("block/color/" + name + "/fancy_gold_lamp_standing"));
            withExistingParent(name + "_fancy_iron_lamp",    modLoc("block/color/" + name + "/fancy_iron_lamp_standing"));
            withExistingParent(name + "_fancy_gold_lantern", modLoc("block/color/" + name + "/fancy_gold_lantern_standing"));
            withExistingParent(name + "_fancy_iron_lantern", modLoc("block/color/" + name + "/fancy_iron_lantern_standing"));
        }
        withExistingParent("cookie_jar",             modLoc("block/cookie_jar"));
        withExistingParent("desk_bell",              modLoc("block/desk_bell"));
        withExistingParent("dinner_plate",           modLoc("block/dinner_plate"));
        withExistingParent("disc_rack",              modLoc("block/disc_rack"));
        withExistingParent("iron_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/iron_inventory"));
        withExistingParent("sword_pedestal",         modLoc("block/sword_pedestal"));
        withExistingParent("redstone_book",          mcLoc("item/written_book"));
        withExistingParent("slotted_book",           mcLoc("item/written_book"));
    }
}
