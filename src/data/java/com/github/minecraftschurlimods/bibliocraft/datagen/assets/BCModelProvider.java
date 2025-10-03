package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BCModelProvider extends ModelProvider {
    public BCModelProvider(PackOutput output) {
        super(output, BibliocraftApi.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerItemModels(itemModels);
        registerBlockModels(blockModels);
    }

    private void registerItemModels(ItemModelGenerators itemModels) {
        basicItem(BCItems.BIG_BOOK.get());
        basicItem(BCItems.CLIPBOARD.get());
        basicItem(BCItems.GOLD_CHAIN.get());
        basicItem(BCItems.GOLD_LANTERN.get());
        basicItem(BCItems.GOLD_SOUL_LANTERN.get());
        basicItem(BCItems.LOCK_AND_KEY.get());
        basicItem(BCItems.PLUMB_LINE.get());
        basicItem(BCItems.STOCKROOM_CATALOG.get());
        basicItem(BCItems.TAPE_MEASURE.get());
        basicItem(BCItems.TAPE_REEL.get());
        basicItem(BCItems.TYPEWRITER_PAGE.get());
        // @formatter:off
        withExistingParent("fancy_gold_lamp",         modLoc("block/fancy_gold_lamp_standing"));
        withExistingParent("fancy_iron_lamp",         modLoc("block/fancy_iron_lamp_standing"));
        withExistingParent("fancy_gold_lantern",      modLoc("block/fancy_gold_lantern_standing"));
        withExistingParent("fancy_iron_lantern",      modLoc("block/fancy_iron_lantern_standing"));
        withExistingParent("soul_fancy_gold_lantern", modLoc("block/soul_fancy_gold_lantern_standing"));
        withExistingParent("soul_fancy_iron_lantern", modLoc("block/soul_fancy_iron_lantern_standing"));
        withExistingParent("typewriter",              modLoc("block/typewriter_0"));
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            withExistingParent(name + "_fancy_gold_lamp",    modLoc("block/color/" + name + "/fancy_gold_lamp_standing"));
            withExistingParent(name + "_fancy_iron_lamp",    modLoc("block/color/" + name + "/fancy_iron_lamp_standing"));
            withExistingParent(name + "_fancy_gold_lantern", modLoc("block/color/" + name + "/fancy_gold_lantern_standing"));
            withExistingParent(name + "_fancy_iron_lantern", modLoc("block/color/" + name + "/fancy_iron_lantern_standing"));
            withExistingParent(name + "_typewriter",         modLoc("block/color/" + name + "/typewriter_0"));
        }
        withExistingParent("cookie_jar",             modLoc("block/cookie_jar"));
        withExistingParent("desk_bell",              modLoc("block/desk_bell"));
        withExistingParent("dinner_plate",           modLoc("block/dinner_plate"));
        withExistingParent("disc_rack",              modLoc("block/disc_rack"));
        withExistingParent("iron_fancy_armor_stand", modLoc("block/template/fancy_armor_stand/iron_inventory"));
        withExistingParent("printing_table",         modLoc("block/printing_table"));
        withExistingParent("iron_printing_table",    modLoc("block/iron_printing_table"));
        withExistingParent("sword_pedestal",         modLoc("block/sword_pedestal"));
        withExistingParent("redstone_book",          mcLoc("item/written_book"));
        withExistingParent("slotted_book",           mcLoc("item/written_book"));
        withExistingParent("written_big_book",       modLoc("item/big_book"));
        // @formatter:on
    }

    private void registerBlockModels(BlockModelGenerators blockModels) {
        DatagenUtil.fancyLampModel(this, BCBlocks.CLEAR_FANCY_GOLD_LAMP, "block/", "gold", mcLoc("block/glass"));
        for (DyeColor color : DyeColor.values()) {
            DatagenUtil.fancyLampModel(this, BCBlocks.FANCY_GOLD_LAMP.holder(color), "block/color/" + color.getSerializedName() + "/", "gold", DatagenUtil.GLASS_TEXTURES.get(color));
        }
        DatagenUtil.fancyLampModel(this, BCBlocks.CLEAR_FANCY_IRON_LAMP, "block/", "iron", mcLoc("block/glass"));
        for (DyeColor color : DyeColor.values()) {
            DatagenUtil.fancyLampModel(this, BCBlocks.FANCY_IRON_LAMP.holder(color), "block/color/" + color.getSerializedName() + "/", "iron", DatagenUtil.GLASS_TEXTURES.get(color));
        }
        DatagenUtil.fancyLanternModel(this, BCBlocks.CLEAR_FANCY_GOLD_LANTERN, "block/", "gold", mcLoc("block/candle_lit"));
        for (DyeColor color : DyeColor.values()) {
            DatagenUtil.fancyLanternModel(this, BCBlocks.FANCY_GOLD_LANTERN.holder(color), "block/color/" + color.getSerializedName() + "/", "gold", DatagenUtil.CANDLE_TEXTURES.get(color));
        }
        DatagenUtil.fancyLanternModel(this, BCBlocks.SOUL_FANCY_GOLD_LANTERN, "block/soul_", "gold", BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"));
        DatagenUtil.fancyLanternModel(this, BCBlocks.CLEAR_FANCY_IRON_LANTERN, "block/", "iron", mcLoc("block/candle_lit"));
        for (DyeColor color : DyeColor.values()) {
            DatagenUtil.fancyLanternModel(this, BCBlocks.FANCY_IRON_LANTERN.holder(color), "block/color/" + color.getSerializedName() + "/", "iron", DatagenUtil.CANDLE_TEXTURES.get(color));
        }
        DatagenUtil.fancyLanternModel(this, BCBlocks.SOUL_FANCY_IRON_LANTERN, "block/soul_", "iron", BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"));
        DatagenUtil.horizontalBlockModel(this, BCBlocks.CLEAR_TYPEWRITER, state -> models()
                        .withExistingParent("block/typewriter_" + state.getValue(TypewriterBlock.PAPER), modLoc("block/template/typewriter/" + state.getValue(TypewriterBlock.PAPER)))
                        .texture("color", mcLoc("block/terracotta")),
                false);
        for (DyeColor color : DyeColor.values()) {
            String name = color.getSerializedName();
            DatagenUtil.horizontalBlockModel(this, BCBlocks.TYPEWRITER.holder(color), state -> models()
                            .withExistingParent("block/color/" + name + "/typewriter_" + state.getValue(TypewriterBlock.PAPER), modLoc("block/template/typewriter/" + state.getValue(TypewriterBlock.PAPER)))
                            .texture("color", mcLoc("block/" + name + "_terracotta")),
                    false);
        }
        horizontalBlock(BCBlocks.CLIPBOARD.get(), models().getExistingFile(modLoc("block/clipboard")));
        getVariantBuilder(BCBlocks.COOKIE_JAR.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/cookie_jar" + (state.getValue(CookieJarBlock.OPEN) ? "_open" : ""))))
                .build());
        simpleBlock(BCBlocks.DESK_BELL.get(), models().getExistingFile(modLoc("block/desk_bell")));
        simpleBlock(BCBlocks.DINNER_PLATE.get(), models().getExistingFile(modLoc("block/dinner_plate")));
        horizontalBlock(BCBlocks.DISC_RACK.get(), models().getExistingFile(modLoc("block/disc_rack")));
        horizontalBlock(BCBlocks.WALL_DISC_RACK.get(), models().getExistingFile(modLoc("block/wall_disc_rack")));
        DatagenUtil.doubleHighHorizontalBlockModel(this, BCBlocks.IRON_FANCY_ARMOR_STAND, models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")), false);
        getVariantBuilder(BCBlocks.GOLD_CHAIN.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().withExistingParent("block/gold_chain", mcLoc("block/chain")).renderType("cutout").texture("all", modLoc("block/gold_chain")).texture("particle", modLoc("block/gold_chain")))
                .rotationX(state.getValue(ChainBlock.AXIS) != Direction.Axis.Y ? 90 : 0)
                .rotationY(state.getValue(ChainBlock.AXIS) == Direction.Axis.X ? 90 : 0)
                .build());
        getVariantBuilder(BCBlocks.GOLD_LANTERN.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().withExistingParent(
                                state.getValue(BlockStateProperties.HANGING) ? "block/gold_lantern_hanging" : "block/gold_lantern",
                                mcLoc(state.getValue(BlockStateProperties.HANGING) ? "block/template_hanging_lantern" : "block/template_lantern")
                        ).renderType("cutout").texture("lantern", modLoc("block/gold_lantern"))
                ).build());
        getVariantBuilder(BCBlocks.GOLD_SOUL_LANTERN.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().withExistingParent(
                                state.getValue(BlockStateProperties.HANGING) ? "block/gold_soul_lantern_hanging" : "block/gold_soul_lantern",
                                mcLoc(state.getValue(BlockStateProperties.HANGING) ? "block/template_hanging_lantern" : "block/template_lantern")
                        ).renderType("cutout").texture("lantern", modLoc("block/gold_soul_lantern"))
                ).build());
        DatagenUtil.horizontalBlockModel(this, BCBlocks.PRINTING_TABLE, state -> models().getExistingFile(modLoc("block/printing_table")), false);
        DatagenUtil.horizontalBlockModel(this, BCBlocks.IRON_PRINTING_TABLE, state -> models().getExistingFile(modLoc("block/iron_printing_table")), false);
        DatagenUtil.horizontalBlockModel(this, BCBlocks.SWORD_PEDESTAL, state -> models().getExistingFile(modLoc("block/sword_pedestal")), false);
        for (TableBlock.Type type : TableBlock.Type.values()) {
            String name = type.getSerializedName();
            for (DyeColor color : DyeColor.values()) {
                models().withExistingParent("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName(), modLoc("block/template/table/" + name + "_cloth")).texture("color", DatagenUtil.WOOL_TEXTURES.get(color));
            }
        }
    }
}
