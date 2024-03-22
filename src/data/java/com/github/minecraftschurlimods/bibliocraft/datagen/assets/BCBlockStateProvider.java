package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BCBlockStateProvider extends BlockStateProvider {
    public BCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BibliocraftApi.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BibliocraftApi.getDatagenHelper().generateBlockStates(this);
        getVariantBuilder(BCBlocks.COOKIE_JAR.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/template/cookie_jar" + (state.getValue(CookieJarBlock.OPEN) ? "_open" : ""))))
                .build());
        simpleBlock(BCBlocks.DESK_BELL.get(), models().getExistingFile(modLoc("block/desk_bell")));
        DatagenUtil.doubleHighHorizontalBlockModel(this, BCBlocks.IRON_FANCY_ARMOR_STAND, models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")), false);
        DatagenUtil.horizontalBlockModel(this, BCBlocks.SWORD_PEDESTAL, state -> models().getExistingFile(modLoc("block/sword_pedestal")), false);
        for (TableBlock.Type type : TableBlock.Type.values()) {
            String name = type.getSerializedName();
            for (DyeColor color : DyeColor.values()) {
                models().withExistingParent("table_cloth_" + name + "_" + color.getSerializedName(), modLoc("block/template/table/" + name + "_cloth")).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }
        }
    }
}
