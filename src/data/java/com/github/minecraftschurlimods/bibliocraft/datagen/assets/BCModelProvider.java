package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.AbstractFancyLightBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLampBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLanternBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCModelTemplates;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.BlockModelDatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.github.minecraftschurlimods.bibliocraft.util.BlockModelDatagenUtil.*;

public class BCModelProvider extends ModelProvider {
    public BCModelProvider(PackOutput output) {
        super(output, BibliocraftApi.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerItemModels(itemModels);
        registerBlockModels(blockModels);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.concat(BCBlocks.COLORED.stream().flatMap(GroupedHolder::streamHolders), BCBlocks.OTHER.stream());
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.concat(BCItems.COLORED.stream().flatMap(GroupedHolder::streamHolders), BCItems.OTHER.stream());
    }

    private void registerItemModels(ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(BCItems.BIG_BOOK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.copy(BCItems.BIG_BOOK.get(), BCItems.WRITTEN_BIG_BOOK.get());
        itemModels.generateFlatItem(BCItems.LOCK_AND_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.PLUMB_LINE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.STOCKROOM_CATALOG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TAPE_MEASURE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TAPE_REEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BCItems.TYPEWRITER_PAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(BCItems.REDSTONE_BOOK.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(Items.WRITTEN_BOOK)));
        itemModels.itemModelOutput.accept(BCItems.SLOTTED_BOOK.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(Items.WRITTEN_BOOK)));
    }

    private void registerBlockModels(BlockModelGenerators blockModels) {
        ResourceLocation goldMaterial = BCUtil.mcLoc("block/gold_block");
        ResourceLocation ironMaterial = BCUtil.mcLoc("block/iron_block");
        // todo copper (with aging)
        ResourceLocation clearColor = BCUtil.mcLoc("block/glass");
        ResourceLocation candle = BCUtil.mcLoc("block/candle_lit");
        fancyLampModel(blockModels, BCBlocks.CLEAR_FANCY_GOLD_LAMP, clearColor, goldMaterial);
        fancyLampModel(blockModels, BCBlocks.CLEAR_FANCY_IRON_LAMP, clearColor, ironMaterial);
        fancyLanternModel(blockModels, BCBlocks.CLEAR_FANCY_GOLD_LANTERN, candle, BCUtil.bcLoc("block/gold_chain"), goldMaterial);
        fancyLanternModel(blockModels, BCBlocks.CLEAR_FANCY_IRON_LANTERN, candle, BCUtil.mcLoc("block/iron_chain"), ironMaterial);
        fancyLanternModel(blockModels, BCBlocks.SOUL_FANCY_GOLD_LANTERN, BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"), BCUtil.bcLoc("block/gold_chain"), goldMaterial);
        fancyLanternModel(blockModels, BCBlocks.SOUL_FANCY_IRON_LANTERN, BCUtil.modLoc("buzzier_bees", "block/soul_candle_lit"), BCUtil.mcLoc("block/iron_chain"), ironMaterial);
        builder(blockModels, BCBlocks.CLEAR_TYPEWRITER)
                .withModelDispatch(TypewriterBlock.PAPER, paper -> BCModelTemplates.TYPEWRITER[paper], BCModelTemplates.color(BCUtil.mcLoc("block/terracotta")))
                .withHorizontalRotation()
                .withItemModelFromDispatch(TypewriterBlock.PAPER, 0)
                .build();
        for (DyeColor color : DyeColor.values()) {
            for (GroupedModelTemplate<DyeColor> template : COLORED) {
                template.build(blockModels, color);
            }
        }
        blockModels.registerSimpleFlatItemModel(BCBlocks.GOLD_CHAIN.asItem());
        blockModels.createAxisAlignedPillarBlockCustomModel(BCBlocks.GOLD_CHAIN.get(), BlockModelGenerators.plainVariant(TexturedModel.CHAIN.updateTemplate(model -> model.extend().renderType(BCUtil.mcLoc("cutout")).build()).create(BCBlocks.GOLD_CHAIN.get(), blockModels.modelOutput)));
        blockModels.createLantern(BCBlocks.GOLD_LANTERN.get());
        blockModels.createLantern(BCBlocks.GOLD_SOUL_LANTERN.get());
        builder(blockModels, BCBlocks.CLIPBOARD)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .withItemModel()
                .withUVLock()
                .build();
        builder(blockModels, BCBlocks.DESK_BELL)
                .withDefaultExistingModel()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.DINNER_PLATE)
                .withDefaultExistingModel()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.COOKIE_JAR)
                .withModelDispatch(CookieJarBlock.OPEN, BCUtil.bcLoc("block/cookie_jar_open"), BCUtil.bcLoc("block/cookie_jar"))
                .withItemModelFromDispatch(CookieJarBlock.OPEN, false)
                .build();
        builder(blockModels, BCBlocks.SWORD_PEDESTAL)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.DISC_RACK)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.WALL_DISC_RACK)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .build();
        builder(blockModels, BCBlocks.PRINTING_TABLE)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.IRON_PRINTING_TABLE)
                .withDefaultExistingModel()
                .withHorizontalRotation()
                .withItemModel()
                .build();
        builder(blockModels, BCBlocks.IRON_FANCY_ARMOR_STAND)
                .withModelDispatch(BlockStateProperties.DOUBLE_BLOCK_HALF, half -> switch (half) {
                    case LOWER -> BCUtil.bcLoc("block/template/fancy_armor_stand/iron_bottom");
                    case UPPER -> BCUtil.bcLoc("block/template/fancy_armor_stand/iron_top");
                })
                .withHorizontalRotation()
                .withUVLock()
                .withItemModel(BCUtil.bcLoc("block/template/fancy_armor_stand/iron_inventory"))
                .build();
        for (TableBlock.Type type : TableBlock.Type.values()) {
            ModelTemplate template = BCModelTemplates.TABLE_CLOTH.get(type);
            for (DyeColor color : DyeColor.values()) {
                TextureMapping textureMapping = BCModelTemplates.color(BlockModelDatagenUtil.WOOL_TEXTURES.get(color));
                template.create(BCUtil.bcLoc("block/color/" + color.getSerializedName() + "/table" + template.suffix.orElse("")), textureMapping, blockModels.modelOutput);
            }
        }
    }

    private static void fancyLanternModel(BlockModelGenerators blockModels, Supplier<FancyLanternBlock> block, ResourceLocation candle, ResourceLocation chain, ResourceLocation material) {
        builder(blockModels, block)
                .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(
                        BCModelTemplates.FANCY_LANTERN_STANDING,
                        BCModelTemplates.FANCY_LANTERN_HANGING,
                        BCModelTemplates.FANCY_LANTERN_WALL),
                        BCModelTemplates.lanternMaterial(candle, chain, material))
                .withHorizontalRotation()
                .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING)
                .build();
    }

    private static void fancyLampModel(BlockModelGenerators blockModels, Supplier<FancyLampBlock> block, ResourceLocation color, ResourceLocation material) {
        builder(blockModels, block)
                .withModelDispatch(AbstractFancyLightBlock.TYPE, lightBlockTypeDispatch(
                        BCModelTemplates.FANCY_LAMP_STANDING,
                        BCModelTemplates.FANCY_LAMP_HANGING,
                        BCModelTemplates.FANCY_LAMP_WALL),
                        BCModelTemplates.lampMaterial(color, material))
                .withHorizontalRotation()
                .withItemModelFromDispatch(AbstractFancyLightBlock.TYPE, AbstractFancyLightBlock.Type.STANDING)
                .build();
    }
}
