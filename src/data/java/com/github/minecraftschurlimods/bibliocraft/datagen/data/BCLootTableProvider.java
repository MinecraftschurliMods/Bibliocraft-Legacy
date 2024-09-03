package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
            BibliocraftApi.getDatagenHelper().generateLootTables(this::add);
            add(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get(),    DatagenUtil.createDefaultTable(BCBlocks.CLEAR_FANCY_GOLD_LAMP.get()));
            add(BCBlocks.CLEAR_FANCY_IRON_LAMP.get(),    DatagenUtil.createDefaultTable(BCBlocks.CLEAR_FANCY_IRON_LAMP.get()));
            add(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get(), DatagenUtil.createDefaultTable(BCBlocks.CLEAR_FANCY_GOLD_LANTERN.get()));
            add(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get(), DatagenUtil.createDefaultTable(BCBlocks.CLEAR_FANCY_IRON_LANTERN.get()));
            for (DyeColor color : DyeColor.values()) {
                add(BCBlocks.FANCY_GOLD_LAMP.get(color),    DatagenUtil.createDefaultTable(BCBlocks.FANCY_GOLD_LAMP.get(color)));
                add(BCBlocks.FANCY_IRON_LAMP.get(color),    DatagenUtil.createDefaultTable(BCBlocks.FANCY_IRON_LAMP.get(color)));
                add(BCBlocks.FANCY_GOLD_LANTERN.get(color), DatagenUtil.createDefaultTable(BCBlocks.FANCY_GOLD_LANTERN.get(color)));
                add(BCBlocks.FANCY_IRON_LANTERN.get(color), DatagenUtil.createDefaultTable(BCBlocks.FANCY_IRON_LANTERN.get(color)));
            }
            add(BCBlocks.CLIPBOARD.get(),              DatagenUtil.createStandardTable(LootItem.lootTableItem(BCBlocks.CLIPBOARD.get()).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(BCDataComponents.CLIPBOARD_CONTENT.get()))));
            add(BCBlocks.COOKIE_JAR.get(),             DatagenUtil.createNameableTable(BCBlocks.COOKIE_JAR.get()));
            add(BCBlocks.DESK_BELL.get(),              DatagenUtil.createDefaultTable(BCBlocks.DESK_BELL.get()));
            add(BCBlocks.DINNER_PLATE.get(),           DatagenUtil.createDefaultTable(BCBlocks.DINNER_PLATE.get()));
            add(BCBlocks.DISC_RACK.get(),              DatagenUtil.createNameableTable(BCBlocks.DISC_RACK.get()));
            add(BCBlocks.WALL_DISC_RACK.get(),         DatagenUtil.createNameableTable(BCBlocks.DISC_RACK.get()));
            add(BCBlocks.GOLD_CHAIN.get(),             DatagenUtil.createDefaultTable(BCBlocks.GOLD_CHAIN.get()));
            add(BCBlocks.GOLD_LANTERN.get(),           DatagenUtil.createDefaultTable(BCBlocks.GOLD_LANTERN.get()));
            add(BCBlocks.GOLD_SOUL_LANTERN.get(),      DatagenUtil.createDefaultTable(BCBlocks.GOLD_SOUL_LANTERN.get()));
            add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), DatagenUtil.createFancyArmorStandTable(BCBlocks.IRON_FANCY_ARMOR_STAND.get()));
            add(BCBlocks.SWORD_PEDESTAL.get(),         DatagenUtil.createStandardTable(LootItem.lootTableItem(BCBlocks.SWORD_PEDESTAL.get()).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.DYED_COLOR))));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BCRegistries.BLOCKS.getEntries().stream().map(e -> (Block) e.get()).toList();
        }
    }
}
