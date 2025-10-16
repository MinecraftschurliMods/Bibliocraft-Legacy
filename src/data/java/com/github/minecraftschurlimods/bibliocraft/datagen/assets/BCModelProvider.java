package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLampBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLanternBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCModelTemplates;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.function.Function;
import java.util.function.Supplier;

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
        itemModels.generateFlatItem(BCItems.BIG_BOOK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.CLIPBOARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.GOLD_CHAIN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.GOLD_LANTERN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.GOLD_SOUL_LANTERN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.LOCK_AND_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.PLUMB_LINE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.STOCKROOM_CATALOG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TAPE_MEASURE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TAPE_REEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TYPEWRITER_PAGE.get(), ModelTemplates.FLAT_ITEM);
        // @formatter:off
        // TODO
        /*
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
        */
        // @formatter:on
    }

    private void registerBlockModels(BlockModelGenerators blockModels) {
        ResourceLocation goldMaterial = BCUtil.mcLoc("block/gold_block");
        ResourceLocation ironMaterial = BCUtil.mcLoc("block/iron_block");
        // todo copper (with aging)
        ResourceLocation clearColor = BCUtil.mcLoc("block/glass");
        fancyLampModel(blockModels, BCBlocks.CLEAR_FANCY_GOLD_LAMP, clearColor, goldMaterial);
        fancyLampModel(blockModels, BCBlocks.CLEAR_FANCY_IRON_LAMP, clearColor, ironMaterial);
        fancyLanternModel(blockModels, BCBlocks.CLEAR_FANCY_GOLD_LANTERN, BCUtil.mcLoc("block/candle_lit"), BCUtil.bcLoc("block/gold_chain"), goldMaterial);
        fancyLanternModel(blockModels, BCBlocks.CLEAR_FANCY_IRON_LANTERN, BCUtil.mcLoc("block/candle_lit"), BCUtil.mcLoc("block/iron_chain"), ironMaterial);
        Function<Integer, ModelTemplate> typewriter = paper -> BCModelTemplates.TYPEWRITER[paper];
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.CLEAR_TYPEWRITER, TypewriterBlock.PAPER, typewriter, BCModelTemplates.color(BCUtil.mcLoc("block/terracotta")), false);
        for (DyeColor color : DyeColor.values()) {
            fancyLampModel(blockModels, BCBlocks.FANCY_GOLD_LAMP.holder(color), DatagenUtil.GLASS_TEXTURES.get(color), goldMaterial);
            fancyLampModel(blockModels, BCBlocks.FANCY_IRON_LAMP.holder(color), DatagenUtil.GLASS_TEXTURES.get(color), ironMaterial);
            fancyLanternModel(blockModels, BCBlocks.FANCY_GOLD_LANTERN.holder(color), DatagenUtil.CANDLE_TEXTURES.get(color), BCUtil.bcLoc("block/gold_chain"), goldMaterial);
            fancyLanternModel(blockModels, BCBlocks.FANCY_IRON_LANTERN.holder(color), DatagenUtil.CANDLE_TEXTURES.get(color), BCUtil.mcLoc("block/iron_chain"), ironMaterial);
            DatagenUtil.horizontalBlock(blockModels, BCBlocks.TYPEWRITER.holder(color), TypewriterBlock.PAPER, typewriter, BCModelTemplates.color(BCUtil.mcLoc("block/" + color.getSerializedName() + "_terracotta")), false);
        }
        fancyLanternModel(blockModels, BCBlocks.SOUL_FANCY_GOLD_LANTERN, BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"), BCUtil.bcLoc("block/gold_chain"), goldMaterial);
        fancyLanternModel(blockModels, BCBlocks.SOUL_FANCY_IRON_LANTERN, BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"), BCUtil.mcLoc("block/iron_chain"), ironMaterial);
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.CLIPBOARD, BCModelTemplates.CLIPBOARD);
        DatagenUtil.simpleBlock(blockModels, BCBlocks.COOKIE_JAR, CookieJarBlock.OPEN, open -> open ? BCModelTemplates.COOKIE_JAR_OPEN : BCModelTemplates.COOKIE_JAR);
        DatagenUtil.simpleBlock(blockModels, BCBlocks.DESK_BELL, BCModelTemplates.DESK_BELL);
        DatagenUtil.simpleBlock(blockModels, BCBlocks.DINNER_PLATE, BCModelTemplates.DINNER_PLATE);
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.DISC_RACK, BCModelTemplates.DISC_RACK);
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.WALL_DISC_RACK, BCModelTemplates.WALL_DISC_RACK);
        DatagenUtil.doubleHighHorizontalBlockModel(blockModels, BCBlocks.IRON_FANCY_ARMOR_STAND, BCModelTemplates.IRON_FANCY_ARMOR_STAND_BOTTOM, BCModelTemplates.IRON_FANCY_ARMOR_STAND_TOP, new TextureMapping(), true);
        blockModels.createAxisAlignedPillarBlockCustomModel(BCBlocks.GOLD_CHAIN.get(), BlockModelGenerators.plainVariant(TexturedModel.CHAIN.create(BCBlocks.GOLD_CHAIN.get(), blockModels.modelOutput)));
        blockModels.createLantern(BCBlocks.GOLD_LANTERN.get());
        blockModels.createLantern(BCBlocks.GOLD_SOUL_LANTERN.get());
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.PRINTING_TABLE, BCModelTemplates.PRINTING_TABLE, false);
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.IRON_PRINTING_TABLE, BCModelTemplates.IRON_PRINTING_TABLE, false);
        DatagenUtil.horizontalBlock(blockModels, BCBlocks.SWORD_PEDESTAL, BCModelTemplates.SWORD_PEDESTAL, false);
        for (TableBlock.Type type : TableBlock.Type.values()) {
            ModelTemplate template = BCModelTemplates.TABLE_CLOTH.get(type);
            for (DyeColor color : DyeColor.values()) {
                TextureMapping textureMapping = BCModelTemplates.color(DatagenUtil.WOOL_TEXTURES.get(color));
                template.create(BCUtil.bcLoc("block/color/" + color.getSerializedName()), textureMapping, blockModels.modelOutput);
            }
        }
    }

    private static void fancyLanternModel(BlockModelGenerators blockModels, Supplier<FancyLanternBlock> block, ResourceLocation candle, ResourceLocation chain, ResourceLocation material) {
        DatagenUtil.fancyLightBlockModel(blockModels, block, BCModelTemplates.FANCY_LANTERN_STANDING, BCModelTemplates.FANCY_LANTERN_HANGING, BCModelTemplates.FANCY_LANTERN_WALL, BCModelTemplates.lanternMaterial(candle, chain, material), false);
    }

    private static void fancyLampModel(BlockModelGenerators blockModels, Supplier<FancyLampBlock> block, ResourceLocation color, ResourceLocation material) {
        DatagenUtil.fancyLightBlockModel(blockModels, block, BCModelTemplates.FANCY_LAMP_STANDING, BCModelTemplates.FANCY_LAMP_HANGING, BCModelTemplates.FANCY_LAMP_WALL, BCModelTemplates.lampMaterial(color, material), false);
    }
}
