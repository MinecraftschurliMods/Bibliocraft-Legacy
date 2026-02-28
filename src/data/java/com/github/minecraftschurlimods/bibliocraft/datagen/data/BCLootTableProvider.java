package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class BCLootTableProvider extends LootTableProvider {
    public BCLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(new SubProviderEntry(BCBlockLootProvider::new, LootContextParamSets.BLOCK)), lookupProvider);
    }

    private static final class BCBlockLootProvider extends BlockLootSubProvider {

        private BCBlockLootProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            addDefaultTable(BCBlocks.CLEAR_FANCY_GOLD_LAMP);
            addDefaultTable(BCBlocks.CLEAR_FANCY_IRON_LAMP);
            addDefaultTable(BCBlocks.CLEAR_FANCY_GOLD_LANTERN);
            addDefaultTable(BCBlocks.CLEAR_FANCY_IRON_LANTERN);
            for (WeatheringCopper.WeatherState weatherState : WeatheringCopper.WeatherState.values()) {
                addDefaultTable(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWaxed(weatherState));
                addDefaultTable(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWeathering(weatherState));
                addDefaultTable(BCBlocks.FANCY_COPPER_LAMP.getWaxed(weatherState));
                addDefaultTable(BCBlocks.FANCY_COPPER_LAMP.getWeathering(weatherState));
                addDefaultTable(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWaxed(weatherState));
                addDefaultTable(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWeathering(weatherState));
                addDefaultTable(BCBlocks.FANCY_COPPER_LANTERN.getWaxed(weatherState));
                addDefaultTable(BCBlocks.FANCY_COPPER_LANTERN.getWeathering(weatherState));
            }
            addDefaultTable(BCBlocks.SOUL_FANCY_GOLD_LANTERN);
            addDefaultTable(BCBlocks.SOUL_FANCY_IRON_LANTERN);
            addDefaultTable(BCBlocks.CLEAR_TYPEWRITER);
            addDefaultTable(BCBlocks.FANCY_GOLD_LAMP);
            addDefaultTable(BCBlocks.FANCY_IRON_LAMP);
            addDefaultTable(BCBlocks.FANCY_GOLD_LANTERN);
            addDefaultTable(BCBlocks.FANCY_IRON_LANTERN);
            addDefaultTable(BCBlocks.TYPEWRITER);
            add(BCBlocks.CLIPBOARD.get(), DatagenUtil.createStandardTable(LootItem.lootTableItem(BCBlocks.CLIPBOARD.get()).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(BCDataComponents.CLIPBOARD_CONTENT.get()))));
            addNameableTable(BCBlocks.COOKIE_JAR);
            addDefaultTable(BCBlocks.DESK_BELL);
            addDefaultTable(BCBlocks.DINNER_PLATE);
            addNameableTable(BCBlocks.DISC_RACK);
            add(BCBlocks.WALL_DISC_RACK.get(), DatagenUtil.createNameableTable(BCBlocks.DISC_RACK.get()));
            add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), DatagenUtil.createFancyArmorStandTable(BCBlocks.IRON_FANCY_ARMOR_STAND.get()));
            addDefaultTable(BCBlocks.GOLD_CHAIN);
            addDefaultTable(BCBlocks.GOLD_LANTERN);
            addDefaultTable(BCBlocks.GOLD_SOUL_LANTERN);
            addNameableTable(BCBlocks.PRINTING_TABLE);
            addNameableTable(BCBlocks.IRON_PRINTING_TABLE);
            add(BCBlocks.SWORD_PEDESTAL.get(), DatagenUtil.createStandardTable(LootItem.lootTableItem(BCBlocks.SWORD_PEDESTAL.get()).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.DYED_COLOR))));
        }

        private void addDefaultTable(Block block) {
            add(block, DatagenUtil.createDefaultTable(block));
        }

        private <T> void addDefaultTable(GroupedHolder<T, Block, ? extends Block> blocks) {
            blocks.values().forEach(this::addDefaultTable);
        }

        private void addDefaultTable(DeferredBlock<? extends Block> block) {
            addDefaultTable(block.get());
        }

        private void addNameableTable(Block block) {
            add(block, DatagenUtil.createNameableTable(block));
        }

        private void addNameableTable(DeferredBlock<? extends Block> block) {
            addNameableTable(block.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Stream.concat(
                    BCBlocks.COLORED.stream().flatMap(GroupedHolder::stream),
                    BCBlocks.OTHER.stream().map(DeferredHolder::get)
            ).toList();
        }
    }
}
