package com.github.minecraftschurlimods.bibliocraft.util.holder;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.datafixers.util.Function3;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Collection;
import java.util.EnumMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public
class CopperSet<W, O> {
    private final EnumMap<WeatheringCopper.WeatherState, W> waxed;
    private final EnumMap<WeatheringCopper.WeatherState, O> weathering;

    private CopperSet(EnumMap<WeatheringCopper.WeatherState, W> waxed, EnumMap<WeatheringCopper.WeatherState, O> weathering) {
        this.waxed = waxed;
        this.weathering = weathering;
    }

    public W getWaxed(WeatheringCopper.WeatherState state) {
        return waxed.get(state);
    }

    public O getWeathering(WeatheringCopper.WeatherState state) {
        return weathering.get(state);
    }

    public Collection<W> getWaxed() {
        return waxed.values();
    }

    public Collection<O> getWeathering() {
        return weathering.values();
    }

    public <T1, T2> BiMap<@NotNull T1, @NotNull T2> waxedMapping(Function<O, T1> weatheringMapper, Function<W, T2> waxedMapper) {
        return ImmutableBiMap.of(
                weatheringMapper.apply(weathering.get(WeatheringCopper.WeatherState.UNAFFECTED)), waxedMapper.apply(waxed.get(WeatheringCopper.WeatherState.UNAFFECTED)),
                weatheringMapper.apply(weathering.get(WeatheringCopper.WeatherState.EXPOSED)), waxedMapper.apply(waxed.get(WeatheringCopper.WeatherState.EXPOSED)),
                weatheringMapper.apply(weathering.get(WeatheringCopper.WeatherState.OXIDIZED)), waxedMapper.apply(waxed.get(WeatheringCopper.WeatherState.OXIDIZED)),
                weatheringMapper.apply(weathering.get(WeatheringCopper.WeatherState.WEATHERED)), waxedMapper.apply(waxed.get(WeatheringCopper.WeatherState.WEATHERED))
        );
    }

    public static <W, O> CopperSet<W, O> forBlocks(
            String name,
            BiFunction<String, Supplier<BlockBehaviour.Properties>, W> waxedBlockFactory,
            Function3<String, WeatheringCopper.WeatherState, Supplier<BlockBehaviour.Properties>, O> blockFactory,
            Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> propertiesFactory
    ) {
        EnumMap<WeatheringCopper.WeatherState, W> waxed = new EnumMap<>(WeatheringCopper.WeatherState.class);
        EnumMap<WeatheringCopper.WeatherState, O> weathering = new EnumMap<>(WeatheringCopper.WeatherState.class);
        for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
            String name1 = state == WeatheringCopper.WeatherState.UNAFFECTED ? name : state.getSerializedName() + "_" + name;
            Supplier<BlockBehaviour.Properties> props = () -> propertiesFactory.apply(state);
            waxed.put(state, waxedBlockFactory.apply("waxed_" + name1, props));
            weathering.put(state, blockFactory.apply(name1, state, props));
        }
        return new CopperSet<>(waxed, weathering);
    }

    public static <WaxedBlock, WaxedItem, OxidizedBlock, OxidizedItem> CopperSet<WaxedItem, OxidizedItem> forBlockItems(CopperSet<WaxedBlock, OxidizedBlock> copperBlockSet, Function<WaxedBlock, WaxedItem> waxedItemFactory, Function<OxidizedBlock, OxidizedItem> weatheringItemFactory) {
        EnumMap<WeatheringCopper.WeatherState, WaxedItem> waxedItems = new EnumMap<>(WeatheringCopper.WeatherState.class);
        EnumMap<WeatheringCopper.WeatherState, OxidizedItem> weatheringItems = new EnumMap<>(WeatheringCopper.WeatherState.class);
        for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
            WaxedBlock waxedBlock = copperBlockSet.getWaxed(state);
            OxidizedBlock weatheringBlock = copperBlockSet.getWeathering(state);
            waxedItems.put(state, waxedItemFactory.apply(waxedBlock));
            weatheringItems.put(state, weatheringItemFactory.apply(weatheringBlock));
        }
        return new CopperSet<>(waxedItems, weatheringItems);
    }
}
