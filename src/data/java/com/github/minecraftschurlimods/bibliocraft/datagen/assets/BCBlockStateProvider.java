package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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
        DatagenUtil.fancyLightBlockModel(this, BCBlocks.CLEAR_FANCY_GOLD_LAMP,
                models().withExistingParent("block/clear_fancy_gold_lamp_standing", modLoc("block/template/fancy_lamp/standing_gold")).texture("color", mcLoc("block/glass")),
                models().withExistingParent("block/clear_fancy_gold_lamp_hanging", modLoc("block/template/fancy_lamp/hanging_gold")).texture("color", mcLoc("block/glass")),
                models().withExistingParent("block/clear_fancy_gold_lamp_wall", modLoc("block/template/fancy_lamp/wall_gold")).texture("color", mcLoc("block/glass")),
                false);
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            ResourceLocation texture = DatagenUtil.GLASS_TEXTURES.get(color);
            DatagenUtil.fancyLightBlockModel(this, BCBlocks.FANCY_GOLD_LAMP.holder(color),
                    models().withExistingParent("block/color/" + name + "/fancy_gold_lamp_standing", modLoc("block/template/fancy_lamp/standing_gold")).texture("color", texture),
                    models().withExistingParent("block/color/" + name + "/fancy_gold_lamp_hanging", modLoc("block/template/fancy_lamp/hanging_gold")).texture("color", texture),
                    models().withExistingParent("block/color/" + name + "/fancy_gold_lamp_wall", modLoc("block/template/fancy_lamp/wall_gold")).texture("color", texture),
                    false);
        }
        DatagenUtil.fancyLightBlockModel(this, BCBlocks.CLEAR_FANCY_IRON_LAMP,
                models().withExistingParent("block/clear_fancy_iron_lamp_standing", modLoc("block/template/fancy_lamp/standing_iron")).texture("color", mcLoc("block/glass")),
                models().withExistingParent("block/clear_fancy_iron_lamp_hanging", modLoc("block/template/fancy_lamp/hanging_iron")).texture("color", mcLoc("block/glass")),
                models().withExistingParent("block/clear_fancy_iron_lamp_wall", modLoc("block/template/fancy_lamp/wall_iron")).texture("color", mcLoc("block/glass")),
                false);
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            ResourceLocation texture = DatagenUtil.GLASS_TEXTURES.get(color);
            DatagenUtil.fancyLightBlockModel(this, BCBlocks.FANCY_IRON_LAMP.holder(color),
                    models().withExistingParent("block/color/" + name + "/fancy_iron_lamp_standing", modLoc("block/template/fancy_lamp/standing_iron")).texture("color", texture),
                    models().withExistingParent("block/color/" + name + "/fancy_iron_lamp_hanging", modLoc("block/template/fancy_lamp/hanging_iron")).texture("color", texture),
                    models().withExistingParent("block/color/" + name + "/fancy_iron_lamp_wall", modLoc("block/template/fancy_lamp/wall_iron")).texture("color", texture),
                    false);
        }
        getVariantBuilder(BCBlocks.COOKIE_JAR.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/cookie_jar" + (state.getValue(CookieJarBlock.OPEN) ? "_open" : ""))))
                .build());
        simpleBlock(BCBlocks.DESK_BELL.get(), models().getExistingFile(modLoc("block/desk_bell")));
        simpleBlock(BCBlocks.DINNER_PLATE.get(), models().getExistingFile(modLoc("block/dinner_plate")));
        horizontalBlock(BCBlocks.DISC_RACK.get(), models().getExistingFile(modLoc("block/disc_rack")));
        horizontalBlock(BCBlocks.WALL_DISC_RACK.get(), models().getExistingFile(modLoc("block/wall_disc_rack")));
        DatagenUtil.doubleHighHorizontalBlockModel(this, BCBlocks.IRON_FANCY_ARMOR_STAND, models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")), false);
        DatagenUtil.horizontalBlockModel(this, BCBlocks.SWORD_PEDESTAL, state -> models().getExistingFile(modLoc("block/sword_pedestal")), false);
        for (TableBlock.Type type : TableBlock.Type.values()) {
            String name = type.getSerializedName();
            for (DyeColor color : DyeColor.values()) {
                models().withExistingParent("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName(), modLoc("block/template/table/" + name + "_cloth")).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }
        }
    }
}
