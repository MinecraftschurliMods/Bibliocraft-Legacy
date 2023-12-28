package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import com.github.minecraftschurlimods.bibliocraft.util.init.WoodTypeDeferredHolder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
            forEach(BCBlocks.BOOKCASE, this::createNameableBlockEntityTable);
            forEach(BCBlocks.DISPLAY_CASE, block -> standardTable(block, LootItem.lootTableItem(block)
                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("color", "display.color"))
            ));
            forEach(BCBlocks.WALL_DISPLAY_CASE, block -> standardTable(block, LootItem.lootTableItem(block)
                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("color", "display.color"))
            ));
            forEach(BCBlocks.FANCY_ARMOR_STAND, this::createFancyArmorStandTable);
            forEach(BCBlocks.LABEL, this::createNameableBlockEntityTable);
            forEach(BCBlocks.POTION_SHELF, this::createNameableBlockEntityTable);
            forEach(BCBlocks.SHELF, this::createNameableBlockEntityTable);
            forEach(BCBlocks.TOOL_RACK, this::createNameableBlockEntityTable);
            add(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), createFancyArmorStandTable(BCBlocks.IRON_FANCY_ARMOR_STAND.get()));
            add(BCBlocks.SWORD_PEDESTAL.get(), standardTable(BCBlocks.SWORD_PEDESTAL.get(), LootItem.lootTableItem(BCBlocks.SWORD_PEDESTAL.get())
                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("color", "display.color"))
            ));
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

        private LootTable.Builder createFancyArmorStandTable(Block block) {
            return standardTable(block, LootItem.lootTableItem(block)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)))
                    .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
            );
        }

        private LootTable.Builder standardTable(Block block, LootPoolSingletonContainer.Builder<?> builder) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(builder)));
        }

        private void forEach(ColoredWoodTypeDeferredHolder<Block, ? extends Block> holder, Function<Block, LootTable.Builder> tableFactory) {
            holder.values().forEach(e -> add(e, tableFactory.apply(e)));
        }

        private void forEach(WoodTypeDeferredHolder<Block, ? extends Block> holder, Function<Block, LootTable.Builder> tableFactory) {
            holder.values().forEach(e -> add(e, tableFactory.apply(e)));
        }
    }
}
