package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Collection;

/**
 * Utility class holding various helper methods specifically for datagen.
 */
@SuppressWarnings("unused")
public final class DatagenUtil {

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
