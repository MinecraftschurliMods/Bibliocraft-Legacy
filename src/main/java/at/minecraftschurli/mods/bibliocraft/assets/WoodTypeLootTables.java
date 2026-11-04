package at.minecraftschurli.mods.bibliocraft.assets;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.content.seat.SeatBackBlock;
import at.minecraftschurli.mods.bibliocraft.content.seat.SeatBackType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.util.DatagenUtil;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/// Loot tables for the Bibliocraft blocks of the given wood types.
final class WoodTypeLootTables extends LootTableProvider {
    WoodTypeLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, List<BibliocraftWoodType> woodTypes) {
        super(
            output,
            Set.of(),
            List.of(new SubProviderEntry(reg -> new BlockLootTables(reg, woodTypes), LootContextParamSets.BLOCK)),
            registries);
    }

    private static class BlockLootTables extends BlockLootSubProvider {
        private final List<BibliocraftWoodType> woodTypes;

        protected BlockLootTables(HolderLookup.Provider registries, List<BibliocraftWoodType> woodTypes) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
            this.woodTypes = woodTypes;
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Stream.concat(
                BCBlocks.WOODEN.stream().flatMap(block -> woodTypes.stream().map(block::holder)),
                BCBlocks.WOODEN_COLORED.stream().flatMap(block -> woodTypes.stream().flatMap(block::streamGroup))
            ).map(Holder::value).toList();
        }

        @Override
        protected void generate() {
            for (BibliocraftWoodType woodType : woodTypes) {
                add(BCBlocks.BOOKCASE.get(woodType), DatagenUtil::createNameableTable);
                add(BCBlocks.FANCY_ARMOR_STAND.get(woodType), DatagenUtil::createFancyArmorStandTable);
                add(BCBlocks.FANCY_CLOCK.get(woodType), DatagenUtil::createDefaultTable);
                add(BCBlocks.WALL_FANCY_CLOCK.get(woodType), block17 -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_CLOCK.get(woodType)));
                add(BCBlocks.FANCY_CRAFTER.get(woodType), DatagenUtil::createNameableTable);
                add(BCBlocks.FANCY_SIGN.get(woodType), DatagenUtil::createDefaultTable);
                add(BCBlocks.WALL_FANCY_SIGN.get(woodType), block13 -> DatagenUtil.createDefaultTable(BCBlocks.FANCY_SIGN.get(woodType)));
                add(BCBlocks.GRANDFATHER_CLOCK.get(woodType), DatagenUtil::createGrandfatherClockTable);
                add(BCBlocks.LABEL.get(woodType), DatagenUtil::createNameableTable);
                add(BCBlocks.POTION_SHELF.get(woodType), DatagenUtil::createNameableTable);
                add(BCBlocks.SHELF.get(woodType), DatagenUtil::createNameableTable);
                add(BCBlocks.TABLE.get(woodType), DatagenUtil::createDefaultTable);
                add(BCBlocks.TOOL_RACK.get(woodType), DatagenUtil::createNameableTable);
                for (DyeColor color : DyeColor.values()) {
                    add(BCBlocks.DISPLAY_CASE.get(woodType, color), DatagenUtil::createDefaultTable);
                    add(BCBlocks.WALL_DISPLAY_CASE.get(woodType, color), block4 -> DatagenUtil.createDefaultTable(BCBlocks.DISPLAY_CASE.get(woodType, color)));
                    add(BCBlocks.SEAT.get(woodType, color), DatagenUtil::createDefaultTable);
                    add(BCBlocks.SEAT_BACK.get(woodType, color), block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(ExplosionCondition.survivesExplosion())
                                .add(LootItem.lootTableItem(BCItems.SMALL_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.SMALL))))
                                .add(LootItem.lootTableItem(BCItems.RAISED_SEAT_BACK.get(woodType, color)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.RAISED))))
                                .add(LootItem.lootTableItem(BCItems.FLAT_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FLAT))))
                                .add(LootItem.lootTableItem(BCItems.TALL_SEAT_BACK.get(woodType, color))  .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.TALL))))
                                .add(LootItem.lootTableItem(BCItems.FANCY_SEAT_BACK.get(woodType, color)) .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SeatBackBlock.TYPE, SeatBackType.FANCY))))));
                }
            }
        }

    }
}
