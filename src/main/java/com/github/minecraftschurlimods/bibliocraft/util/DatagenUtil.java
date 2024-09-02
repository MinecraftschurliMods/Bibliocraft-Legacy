package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.content.fancylight.AbstractFancyLightBlock;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class holding various helper methods specifically for datagen.
 */
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
     * @param provider Your mod's {@link BlockStateProvider}.
     * @param block    A {@link Supplier} for the {@link Block} to add the model for.
     * @param name     The name of the model file.
     * @param parent   The parent id of the model file.
     * @param texture  The texture to apply.
     */
    public static void horizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, String name, ResourceLocation parent, ResourceLocation texture) {
        horizontalBlockModel(provider, block, $ -> provider.models().withExistingParent(name, parent).texture("texture", texture), true);
    }

    /**
     * Adds a block with horizontal rotations and a parent model.
     *
     * @param provider Your mod's {@link BlockStateProvider}.
     * @param block    A {@link Supplier} for the {@link Block} to add the model for.
     * @param name     The name of the model file.
     * @param parent   The parent id of the model file.
     * @param texture  The texture to apply.
     * @param uvLock   Whether to UV-lock the models or not.
     */
    public static void horizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, String name, ResourceLocation parent, ResourceLocation texture, boolean uvLock) {
        horizontalBlockModel(provider, block, $ -> provider.models().withExistingParent(name, parent).texture("texture", texture), uvLock);
    }

    /**
     * Adds a block with horizontal rotations. Enables UV-locking.
     *
     * @param provider      Your mod's {@link BlockStateProvider}.
     * @param block         A {@link Supplier} for the {@link Block} to add the model for.
     * @param modelFunction A {@link Function} determining which {@link ModelFile} to use for which {@link BlockState}.
     */
    public static void horizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, Function<BlockState, ModelFile> modelFunction) {
        horizontalBlockModel(provider, block, modelFunction, true);
    }

    /**
     * Adds a block with horizontal rotations.
     *
     * @param provider      Your mod's {@link BlockStateProvider}.
     * @param block         A {@link Supplier} for the {@link Block} to add the model for.
     * @param modelFunction A {@link Function} determining which {@link ModelFile} to use for which {@link BlockState}.
     * @param uvLock        Whether to UV-lock the block models or not.
     */
    public static void horizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, Function<BlockState, ModelFile> modelFunction, boolean uvLock) {
        provider.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(modelFunction.apply(state))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .uvLock(uvLock)
                .build());
    }

    /**
     * Adds a double-high block with a bottom and top model file.
     *
     * @param provider Your mod's {@link BlockStateProvider}.
     * @param block    The block to add the model for.
     * @param bottom   The bottom model file.
     * @param top      The top model file.
     * @param uvLock   Whether to UV-lock the models or not.
     */
    public static void doubleHighHorizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, ModelFile bottom, ModelFile top, boolean uvLock) {
        horizontalBlockModel(provider, block, state -> state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? bottom : top, uvLock);
    }

    /**
     * Adds a block with an open/closed property.
     *
     * @param provider Your mod's {@link BlockStateProvider}.
     * @param block    The block to add the model for.
     * @param open     The open model file.
     * @param closed   The closed model file.
     * @param uvLock   Whether to UV-lock the models or not.
     */
    public static void openClosedHorizontalBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, ModelFile open, ModelFile closed, boolean uvLock) {
        horizontalBlockModel(provider, block, state -> state.getValue(BlockStateProperties.OPEN) ? open : closed, uvLock);
    }

    /**
     * Adds a block with a fancy light type property.
     *
     * @param provider Your mod's {@link BlockStateProvider}.
     * @param block    The block to add the model for.
     * @param standing The standing model file.
     * @param hanging  The hanging model file.
     * @param wall     The wall model file.
     * @param uvLock   Whether to UV-lock the models or not.
     */
    public static void fancyLightBlockModel(BlockStateProvider provider, Supplier<? extends Block> block, ModelFile standing, ModelFile hanging, ModelFile wall, boolean uvLock) {
        horizontalBlockModel(provider, block, state -> switch (state.getValue(AbstractFancyLightBlock.TYPE)) {
            case STANDING -> standing;
            case HANGING -> hanging;
            case WALL -> wall;
        }, uvLock);
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
        return createStandardTable(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
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
                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
    }

    /**
     * Adds all elements of the given collection to the {@link IntrinsicHolderTagsProvider.IntrinsicTagAppender}.
     *
     * @param collection The collection containing the elements to add.
     * @param tag        The given {@link IntrinsicHolderTagsProvider.IntrinsicTagAppender}, obtainable through {@link TagsProvider#tag(TagKey)}.
     * @param <T>        The type of the collection elements.
     */
    public static <T> void addAll(Collection<? extends T> collection, IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> tag) {
        collection.forEach(tag::add);
    }

    /**
     * Adds all elements of the given collection to the {@link TagsProvider.TagAppender}
     *
     * @param collection The collection containing the elements to add.
     * @param tag        The given {@link TagsProvider.TagAppender}, obtainable through {@link TagsProvider#tag(TagKey)}.
     * @param registry   The {@link Registry} associated with the collection elements.
     * @param <T>        The type of the collection elements.
     */
    @SuppressWarnings("DataFlowIssue")
    public static <T> void addAll(Collection<? extends T> collection, TagsProvider.TagAppender<T> tag, Registry<T> registry) {
        collection.stream().map(e -> ResourceKey.create(registry.key(), registry.getKey(e))).forEach(tag::add);
    }
}
