package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.apiimpl.BibliocraftDatagenHelperImpl;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.AbstractFancyLightBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class holding various helper methods specifically for datagen.
 */
@SuppressWarnings("unused")
public final class DatagenUtil {
    public static final Map<DyeColor, ResourceLocation> CANDLE_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_candle_lit"))));
    public static final Map<DyeColor, ResourceLocation> GLASS_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_stained_glass"))));
    public static final Map<DyeColor, ResourceLocation> WOOL_TEXTURES = Util.make(new HashMap<>(), map -> Arrays.stream(DyeColor.values()).forEach(color -> map.put(color, BCUtil.mcLoc("block/" + color.getName() + "_wool"))));

    /**
     * @param s The string to create a translation for.
     * @return A translated form of the given string.
     */
    public static String toTranslation(String s) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (char c : s.toCharArray()) {
            if (c == '_') {
                builder.append(' ');
                first = true;
            } else if (first) {
                builder.append(Character.toUpperCase(c));
                first = false;
            } else {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    /**
     * Adds a block with horizontal rotations and a parent model. Enables UV-locking.
     *
     * @param generators     Your mod's {@link BlockModelGenerators}.
     * @param block          A {@link Supplier} for the {@link Block} to add the model for.
     * @param template       The template for the model file.
     * @param textureMapping The texture to apply.
     */
    public static void horizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, ModelTemplate template, TextureMapping textureMapping) {
        horizontalBlockModel(generators, block, template, textureMapping, true);
    }

    /**
     * Adds a block with horizontal rotations and a parent model. Enables UV-locking.
     *
     * @param generators     Your mod's {@link BlockModelGenerators}.
     * @param block          A {@link Supplier} for the {@link Block} to add the model for.
     * @param template       The template for the model file.
     * @param textureMapping The texture to apply.
     * @param uvLock         Whether to UV-lock the models or not.
     */
    public static void horizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, ModelTemplate template, TextureMapping textureMapping, boolean uvLock) {
        Block b = block.get();
        ResourceLocation modelLoc = template.create(b, textureMapping, generators.modelOutput);
        generators.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(b, BlockModelGenerators.plainVariant(modelLoc))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
                .with(uvLock ? BlockModelGenerators.UV_LOCK : BlockModelGenerators.NOP));
    }

    /**
     * Adds a block with horizontal rotations and a parent model. Enables UV-locking.
     *
     * @param generators     Your mod's {@link BlockModelGenerators}.
     * @param block          A {@link Supplier} for the {@link Block} to add the model for.
     * @param propertyDispatch A {@link PropertyDispatch} for the {@link BlockState} to add the model for.
     * @param uvLock         Whether to UV-lock the models or not.
     */
    public static void horizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, PropertyDispatch<MultiVariant> propertyDispatch, boolean uvLock) {
        Block b = block.get();
        generators.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(b)
                .with(propertyDispatch)
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
                .with(uvLock ? BlockModelGenerators.UV_LOCK : BlockModelGenerators.NOP));
    }

    /**
     * Adds a block with horizontal rotations and a parent model. Enables UV-locking.
     *
     * @param generators       Your mod's {@link BlockModelGenerators}.
     * @param block            A {@link Supplier} for the {@link Block} to add the model for.
     * @param property         The property to base the model selection on.
     * @param templateFunction A {@link Function} that takes the property value and returns the {@link ModelTemplate} to use.
     * @param textureMapping   The texture to apply.
     * @param uvLock           Whether to UV-lock the models or not.
     */
    public static <T extends Comparable<T>> void horizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, Property<T> property, Function<T, ModelTemplate> templateFunction, TextureMapping textureMapping, boolean uvLock) {
        Block b = block.get();
        Map<T, MultiVariant> variantsCache = new HashMap<>();
        Function<T, MultiVariant> factory = v -> BlockModelGenerators.plainVariant(templateFunction.apply(v).create(b, textureMapping, generators.modelOutput));
        horizontalBlockModel(
                generators,
                block,
                PropertyDispatch
                        .initial(property)
                        .generate(t -> variantsCache.computeIfAbsent(t, factory)),
                uvLock);
    }

    /**
     * Adds a double-high block with a bottom and top model file.
     *
     * @param generators    Your mod's {@link BlockModelGenerators}.
     * @param block          The block to add the model for.
     * @param bottom         The bottom model file.
     * @param top            The top model file.
     * @param textureMapping The texture to apply.
     * @param uvLock         Whether to UV-lock the models or not.
     */
    public static void doubleHighHorizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, ModelTemplate bottom, ModelTemplate top, TextureMapping textureMapping, boolean uvLock) {
        Block b = block.get();
        ResourceLocation bottomModelLoc = bottom.create(b, textureMapping, generators.modelOutput);
        ResourceLocation topModelLoc = top.create(b, textureMapping, generators.modelOutput);
        horizontalBlockModel(
                generators,
                block,
                PropertyDispatch.initial(BlockStateProperties.DOUBLE_BLOCK_HALF)
                        .select(DoubleBlockHalf.LOWER, BlockModelGenerators.plainVariant(bottomModelLoc))
                        .select(DoubleBlockHalf.UPPER, BlockModelGenerators.plainVariant(topModelLoc)),
                uvLock);
    }

    /**
     * Adds a block with an open/closed property.
     *
     * @param generators Your mod's {@link BlockModelGenerators}.
     * @param block      The block to add the model for.
     * @param open       The open model file.
     * @param closed     The closed model file.
     * @param uvLock     Whether to UV-lock the models or not.
     */
    public static void openClosedHorizontalBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, ModelTemplate open, ModelTemplate closed, TextureMapping textureMapping, boolean uvLock) {
        Block b = block.get();
        ResourceLocation openModelLoc = open.create(b, textureMapping, generators.modelOutput);
        ResourceLocation closedModelLoc = closed.create(b, textureMapping, generators.modelOutput);
        horizontalBlockModel(
                generators,
                block,
                PropertyDispatch.initial(BlockStateProperties.OPEN)
                        .select(true, BlockModelGenerators.plainVariant(openModelLoc))
                        .select(false, BlockModelGenerators.plainVariant(closedModelLoc)),
                uvLock);
    }

    /**
     * Adds a block with a fancy light type property.
     *
     * @param generators     Your mod's {@link BlockModelGenerators}.
     * @param block          The block to add the model for.
     * @param standing       The standing model file.
     * @param hanging        The hanging model file.
     * @param wall           The wall model file.
     * @param textureMapping The texture to apply.
     * @param uvLock         Whether to UV-lock the models or not.
     */
    public static void fancyLightBlockModel(BlockModelGenerators generators, Supplier<? extends Block> block, ModelTemplate standing, ModelTemplate hanging, ModelTemplate wall, TextureMapping textureMapping, boolean uvLock) {
        horizontalBlockModel(generators, block, AbstractFancyLightBlock.TYPE, type -> switch (type) {
            case AbstractFancyLightBlock.Type.STANDING -> standing;
            case AbstractFancyLightBlock.Type.HANGING -> hanging;
            case AbstractFancyLightBlock.Type.WALL -> wall;
        }, textureMapping, uvLock);
    }

    /**
     * Adds a fancy lamp model.
     *
     * @param generators   Your mod's {@link BlockModelGenerators}.
     * @param block        The block to add the model for.
     * @param folderPrefix The folder prefix of the model.
     * @param material     The material of the lamp. E.g. gold, iron.
     * @param texture      The glass texture to use.
     */
    public static void fancyLampModel(BlockModelGenerators generators, Supplier<? extends Block> block, String folderPrefix, String material, ResourceLocation texture) {
        fancyLightBlockModel(generators, block,
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lamp_standing", BCUtil.bcLoc("block/template/fancy_lamp/standing_" + material)).texture("color", texture),
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lamp_hanging", BCUtil.bcLoc("block/template/fancy_lamp/hanging_" + material)).texture("color", texture),
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lamp_wall", BCUtil.bcLoc("block/template/fancy_lamp/wall_" + material)).texture("color", texture),
                false);
    }

    /**
     * Adds a fancy lantern model.
     *
     * @param generators   Your mod's {@link BlockModelGenerators}.
     * @param block        The block to add the model for.
     * @param folderPrefix The folder prefix of the model.
     * @param material     The material of the lantern. E.g. gold, iron.
     * @param texture      The candle texture to use.
     */
    public static void fancyLanternModel(BlockModelGenerators generators, Supplier<? extends Block> block, String folderPrefix, String material, ResourceLocation texture) {
        fancyLightBlockModel(generators, block,
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lantern_standing", BCUtil.bcLoc("block/template/fancy_lantern/standing_" + material)).texture("color", texture),
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lantern_hanging", BCUtil.bcLoc("block/template/fancy_lantern/hanging_" + material)).texture("color", texture),
                provider.models().withExistingParent(folderPrefix + "fancy_" + material + "_lantern_wall", BCUtil.bcLoc("block/template/fancy_lantern/wall_" + material)).texture("color", texture),
                false);
    }

    /**
     * Creates a standard loot table with the given entry builder.
     *
     * @param builder The entry builder to use.
     * @return A standard loot table with the given entry builder.
     */
    public static LootTable.Builder createStandardTable(LootPoolSingletonContainer.Builder<?> builder) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(builder).when(ExplosionCondition.survivesExplosion()));
    }

    /**
     * Creates a standard loot table that drops the given block.
     *
     * @param block The block to drop.
     * @return A standard loot table that drops the given block.
     */
    public static LootTable.Builder createDefaultTable(Block block) {
        return createStandardTable(LootItem.lootTableItem(block));
    }

    /**
     * Creates a standard loot table that drops the given nameable block.
     *
     * @param block The block to drop.
     * @return A standard loot table that drops the given nameable block.
     */
    public static LootTable.Builder createNameableTable(Block block) {
        return createStandardTable(LootItem.lootTableItem(block)
                .apply(CopyNameFunction.copyName(new CopyNameFunction.Source(LootContextParams.BLOCK_ENTITY))));
    }

    /**
     * Creates a loot table for a fancy armor stand.
     *
     * @param block The block to create the loot table for.
     * @return A loot table for a fancy armor stand, dropping the given block.
     */
    public static LootTable.Builder createFancyArmorStandTable(Block block) {
        return createStandardTable(LootItem.lootTableItem(block)
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)))
                .apply(CopyNameFunction.copyName(new CopyNameFunction.Source(LootContextParams.BLOCK_ENTITY))));
    }

    /**
     * Creates a loot table for a grandfather clock.
     *
     * @param block The block to create the loot table for.
     * @return A loot table for a grandfather clock, dropping the given block.
     */
    public static LootTable.Builder createGrandfatherClockTable(Block block) {
        return createStandardTable(LootItem.lootTableItem(block)
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))));
    }

    /**
     * Adds all elements of the given collection to the {@link TagAppender}
     *
     * @param collection The collection containing the elements to add.
     * @param tag        The given {@link TagAppender}, obtainable through {@link TagsProvider#tag(TagKey)}.
     * @param <T>        The type of the collection elements.
     */
    public static <T> void addAll(Collection<? extends T> collection, TagAppender<T, T> tag) {
        collection.forEach(tag::add);
    }

    /**
     * Adds all elements of the given collection to the {@link TagAppender}
     *
     * @param collection The collection containing the elements to add.
     * @param tag        The given {@link TagAppender}, obtainable through {@link TagsProvider#tag(TagKey)}.
     * @param <T>        The type of the collection elements.
     */
    public static <T> void addAllOptional(Collection<? extends T> collection, TagAppender<T, T> tag) {
        collection.forEach(tag::addOptional);
    }
}
