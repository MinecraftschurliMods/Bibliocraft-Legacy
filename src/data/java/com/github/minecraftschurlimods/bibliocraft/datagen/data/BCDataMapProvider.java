package com.github.minecraftschurlimods.bibliocraft.datagen.data;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

import java.util.concurrent.CompletableFuture;

public class BCDataMapProvider extends DataMapProvider {
    public BCDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        Builder<Oxidizable, Block> oxidizable = builder(NeoForgeDataMaps.OXIDIZABLES);
        Builder<Waxable, Block> waxables = builder(NeoForgeDataMaps.WAXABLES);
        for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
            waxables.add(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWeathering(state), new Waxable(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWaxed(state).get()), false);
            waxables.add(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWeathering(state), new Waxable(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWaxed(state).get()), false);
            for (DyeColor color : DyeColor.values()) {
                waxables.add(BCBlocks.FANCY_COPPER_LAMP.getWeathering(state).holder(color), new Waxable(BCBlocks.FANCY_COPPER_LAMP.getWaxed(state).get(color)), false);
                waxables.add(BCBlocks.FANCY_COPPER_LANTERN.getWeathering(state).holder(color), new Waxable(BCBlocks.FANCY_COPPER_LANTERN.getWaxed(state).get(color)), false);
            }
            WeatheringCopper.WeatherState next = state.next();
            if (next == state) {
                continue;
            }
            oxidizable.add(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWeathering(state), new Oxidizable(BCBlocks.CLEAR_FANCY_COPPER_LAMP.getWeathering(next).get()), false);
            oxidizable.add(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWeathering(state), new Oxidizable(BCBlocks.CLEAR_FANCY_COPPER_LANTERN.getWeathering(next).get()), false);
            for (DyeColor color : DyeColor.values()) {
                oxidizable.add(BCBlocks.FANCY_COPPER_LAMP.getWeathering(state).holder(color), new Oxidizable(BCBlocks.FANCY_COPPER_LAMP.getWeathering(next).get(color)), false);
                oxidizable.add(BCBlocks.FANCY_COPPER_LANTERN.getWeathering(state).holder(color), new Oxidizable(BCBlocks.FANCY_COPPER_LANTERN.getWeathering(next).get(color)), false);
            }
        }
        oxidizable.build();
        waxables.build();
    }
}
