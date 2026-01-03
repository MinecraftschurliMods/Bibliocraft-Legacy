package com.github.minecraftschurlimods.bibliocraft.api.datagen;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * An adaptation of {@link net.minecraft.data.loot.LootTableProvider} and {@link net.minecraft.data.loot.BlockLootSubProvider} that is optimized to Bibliocraft's needs.
 * Among other features, this class eliminates the sub provider abstraction layer and natively supports data load conditions.
 * If you are an addon developer, you should rarely need to interact with this class outside of the two {@code add()} methods.
 */
public abstract class BlockLootTableProvider implements DataProvider {
    private static final Codec<Optional<WithConditions<LootTable>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(LootTable.DIRECT_CODEC);
    private final CompletableFuture<HolderLookup.Provider> registries;
    private final PackOutput.PathProvider pathProvider;
    private final Map<ResourceKey<LootTable>, WithConditionsBuilder<LootTable.Builder>> map = new HashMap<>();

    /**
     * @param output     The {@link PackOutput} to use.
     * @param registries The {@link HolderLookup.Provider} to use.
     */
    public BlockLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.LOOT_TABLE);
        this.registries = registries;
    }

    /**
     * Override this method to add your loot tables.
     */
    protected abstract void generate();

    @Override
    public String getName() {
        return "Loot Tables";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(provider -> run(output, provider));
    }

    private CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider provider) {
        generate();
        Map<RandomSupport.Seed128bit, Identifier> seeds = new Object2ObjectOpenHashMap<>();
        return CompletableFuture.allOf(map.entrySet().stream().map(entry -> {
            Identifier location = entry.getKey().identifier();
            Identifier sequence = seeds.put(RandomSequence.seedForKey(location), location);
            if (sequence != null) {
                Util.logAndPauseIfInIde("Loot table random sequence seed collision on " + sequence + " and " + location);
            }
            WithConditions<LootTable> conditional = entry.getValue()
                    .map(builder -> builder.setRandomSequence(location).setParamSet(LootContextParamSets.BLOCK).build())
                    .build();
            return conditional.conditions().isEmpty()
                    ? DataProvider.saveStable(output, provider, LootTable.DIRECT_CODEC, conditional.carrier(), pathProvider.json(location))
                    : DataProvider.saveStable(output, provider, CONDITIONAL_CODEC, Optional.of(conditional), pathProvider.json(location));
        }).toArray(CompletableFuture[]::new));
    }

    /**
     * Adds a loot table for a block.
     *
     * @param block   The block to add the loot table for.
     * @param builder The builder from which to generate the loot table.
     */
    public void add(Block block, WithConditionsBuilder<LootTable.Builder> builder) {
        block.getLootTable().ifPresent(key -> map.put(key, builder));
    }

    /**
     * Adds a loot table for a block.
     *
     * @param block   The block to add the loot table for.
     * @param factory A function for the builder from which to generate the loot table.
     */
    public void add(Block block, Function<Block, WithConditionsBuilder<LootTable.Builder>> factory) {
        add(block, factory.apply(block));
    }

    /**
     * @param table The loot table builder to wrap.
     * @return The given loot table, wrapped as a {@link WithConditionsBuilder}.
     */
    public static WithConditionsBuilder<LootTable.Builder> wrapLootTable(LootTable.Builder table) {
        return new WithConditionsBuilder<LootTable.Builder>().withCarrier(table);
    }

    /**
     * A variant of {@link WithConditions.Builder} that has a {@link WithConditionsBuilder#map(Function) map} operation and does no validation on whether there are actually conditions added to the builder.
     *
     * @param <T> The wrapped builder's type.
     */
    public static class WithConditionsBuilder<T> extends WithConditions.Builder<T> {
        private final List<ICondition> conditions = new ArrayList<>();
        private T carrier;

        /**
         * Constructs a new {@link WithConditionsBuilder} using the provided existing list of conditions.
         *
         * @param conditions The existing list of conditions to use.
         */
        public WithConditionsBuilder(List<ICondition> conditions) {
            this.conditions.addAll(conditions);
        }

        /**
         * Constructs a new {@link WithConditionsBuilder} using the provided existing list of conditions.
         */
        public WithConditionsBuilder() {
            this(new ArrayList<>());
        }

        /**
         * Transforms this {@code WithConditionsBuilder<T>} to a {@code WithConditionsBuilder<N>} using the provided mapper.
         *
         * @param mapper The function to use for transforming.
         * @param <N>    The new generic type of the {@link WithConditionsBuilder}.
         * @return A transformed variant of this {@link WithConditionsBuilder}.
         */
        public <N> WithConditionsBuilder<N> map(Function<T, N> mapper) {
            return new WithConditionsBuilder<N>(conditions).withCarrier(mapper.apply(carrier));
        }

        /**
         * Adds a condition to the builder.
         *
         * @param conditions The condition to add to the builder.
         * @return This builder, for chaining.
         */
        public WithConditionsBuilder<T> addCondition(Collection<ICondition> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        /**
         * Adds one or multiple conditions to the builder.
         *
         * @param conditions The condition(s) to add to the builder.
         * @return This builder, for chaining.
         */
        public WithConditionsBuilder<T> addCondition(ICondition... conditions) {
            this.conditions.addAll(List.of(conditions));
            return this;
        }

        /**
         * Sets the carrier of the conditions, i.e. the underlying object the conditions will be applied to.
         *
         * @param carrier The carrier to set.
         * @return This builder, for chaining.
         */
        public WithConditionsBuilder<T> withCarrier(T carrier) {
            this.carrier = carrier;
            return this;
        }

        /**
         * @return A {@link WithConditions} constructed from this builder.
         */
        public WithConditions<T> build() {
            Validate.notNull(carrier, "You need to supply a carrier to create a WithConditions");
            return new WithConditions<>(conditions, carrier);
        }
    }
}
