package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WoodTypeDeferredHolder<R, T extends R> {
    private static final List<WoodType> DEFAULT_WOOD_TYPES = List.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.JUNGLE, WoodType.ACACIA, WoodType.DARK_OAK, WoodType.CRIMSON, WoodType.WARPED, WoodType.MANGROVE, WoodType.BAMBOO, WoodType.CHERRY);
    private final Map<WoodType, DeferredHolder<R, T>> map = new HashMap<>();

    public WoodTypeDeferredHolder(DeferredRegister<R> register, String suffix, List<WoodType> types, Function<WoodType, ? extends T> creator) {
        for (WoodType type : types) {
            map.put(type, register.register(type.name() + "_" + suffix, () -> creator.apply(type)));
        }
    }

    public DeferredHolder<R, T> holder(WoodType type) {
        return map.get(type);
    }

    public T get(WoodType type) {
        return map.get(type).get();
    }

    public ResourceLocation id(WoodType type) {
        return map.get(type).getId();
    }
}
