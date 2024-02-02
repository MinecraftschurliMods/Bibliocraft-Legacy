package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftDatagenHelper;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.DatagenUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BCLootTableProvider extends LootTableProvider {
    public BCLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new SubProviderEntry(BCBlockLootProvider::new, LootContextParamSets.BLOCK)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
    }

    private static final class BCBlockLootProvider extends BlockLootSubProvider {
        private final List<Block> blocks = new ArrayList<>();

        private BCBlockLootProvider() {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS);
        }

        @Override
        protected void generate() {
            BibliocraftDatagenHelper.get().generateLootTables(this::add);
            add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), DatagenUtil.createFancyArmorStandTable(BCBlocks.IRON_FANCY_ARMOR_STAND.get()));
            add(BCBlocks.SWORD_PEDESTAL.get(), DatagenUtil.createStandardTable(LootItem.lootTableItem(BCBlocks.SWORD_PEDESTAL.get()).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("color", "display.color"))));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return blocks;
        }

        @Override
        protected void add(Block block, LootTable.Builder builder) {
            super.add(block, builder);
            blocks.add(block);
        }
    }
}
