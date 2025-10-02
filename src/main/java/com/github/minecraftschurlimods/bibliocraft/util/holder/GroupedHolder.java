package com.github.minecraftschurlimods.bibliocraft.util.holder;

import com.mojang.datafixers.util.Function3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class GroupedHolder<K, R, T extends R> implements GroupingDeferredHolder<R, T> {
    private final Map<K, DeferredHolder<R, T>> map = new LinkedHashMap<>();
    private final String name;

    protected GroupedHolder(Function<K, String> prefix, String name, BiFunction<String, K, DeferredHolder<R, T>> creator, Iterable<K> keys) {
        this.name = name;
        for (K key : keys) {
            this.map.put(key, creator.apply(prefix.apply(key) + name, key));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public DeferredHolder<R, T> holder(K key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("No holder for key " + key);
        }
        return map.get(key);
    }

    public T get(K key) {
        DeferredHolder<R, T> holder = holder(key);
        return holder.get();
    }

    @Override
    public Collection<DeferredHolder<R, T>> holders() {
        return map.values();
    }

    @Override
    public Stream<DeferredHolder<R, T>> streamHolders() {
        return holders().stream();
    }

    @Override
    public Collection<R> values() {
        return stream().toList();
    }

    @Override
    public Collection<ResourceLocation> ids() {
        return map.values().stream().map(DeferredHolder::getId).toList();
    }

    public @Unmodifiable Collection<K> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public @Unmodifiable Map<K, DeferredHolder<R, T>> map() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Stream<R> stream() {
        return streamHolders().map(DeferredHolder::get);
    }

    public static <K, T extends Item> GroupedHolder<K, Item, T> forItem(DeferredRegister.Items register, Function<K, String> prefix, String name, BiFunction<K, Item.Properties, T> factory, Function<K, UnaryOperator<Item.Properties>> properties, Iterable<K> keys) {
        return new GroupedHolder<>(prefix, name, (s,k) -> register.registerItem(s, p -> factory.apply(k, p), properties.apply(k)), keys);
    }

    public static <K, T extends Block> GroupedHolder<K, Block, T> forBlock(DeferredRegister.Blocks register, Function<K, String> prefix, String name, BiFunction<K, BlockBehaviour.Properties, T> factory, Function<K, UnaryOperator<BlockBehaviour.Properties>> properties, Iterable<K> keys) {
        return new GroupedHolder<>(prefix, name, (s,k) -> register.registerBlock(s, p -> factory.apply(k, p), properties.apply(k)), keys);
    }

    public static <K1, K2, T extends Item> Nested<K1, K2, Item, T> forItemNested(DeferredRegister.Items register, BiFunction<K1, K2, String> prefix, String name, Function3<K1, K2, Item.Properties, T> factory, BiFunction<K1, K2, UnaryOperator<Item.Properties>> properties, Iterable<K1> keys1, Iterable<K2> keys2) {
        return new Nested<>(name, k1 -> new GroupedHolder<>(k2 -> prefix.apply(k1, k2), name, (s,k2) -> register.registerItem(s, p -> factory.apply(k1, k2, p), properties.apply(k1, k2)), keys2), keys1);
    }

    public static <K1, K2, T extends Block> Nested<K1, K2, Block, T> forBlockNested(DeferredRegister.Blocks register, BiFunction<K1, K2, String> prefix, String name, Function3<K1, K2, BlockBehaviour.Properties, T> factory, BiFunction<K1, K2, UnaryOperator<BlockBehaviour.Properties>> properties, Iterable<K1> keys1, Iterable<K2> keys2) {
        return new Nested<>(name, k1 -> new GroupedHolder<>(k2 -> prefix.apply(k1, k2), name, (s,k2) -> register.registerBlock(s, p -> factory.apply(k1, k2, p), properties.apply(k1, k2)), keys2), keys1);
    }

    public static class Nested<K1, K2, R, T extends R> implements GroupingDeferredHolder<R, T> {
        private final Map<K1, GroupedHolder<K2, R, T>> map = new LinkedHashMap<>();
        private final String name;

        protected Nested(String name, Function<K1, GroupedHolder<K2, R, T>> creator, Iterable<K1> keys) {
            this.name = name;
            for (K1 key : keys) {
                this.map.put(key, creator.apply(key));
            }
        }

        public String getName() {
            return name;
        }

        public GroupedHolder<K2, R, T> group(K1 key) {
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException("No group for key " + key);
            }
            return map.get(key);
        }

        public Stream<DeferredHolder<R, T>> streamGroup(K1 key) {
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException("No group for key " + key);
            }
            return map.get(key).holders().stream();
        }

        public Collection<GroupedHolder<K2, R, T>> groups() {
            return map.values();
        }

        @Override
        public Collection<DeferredHolder<R, T>> holders() {
            return streamHolders().toList();
        }

        @Override
        public Stream<DeferredHolder<R, T>> streamHolders() {
            return map.values().stream().flatMap(GroupedHolder::streamHolders);
        }

        @Override
        public Collection<R> values() {
            return stream().toList();
        }

        @Override
        public Collection<ResourceLocation> ids() {
            return streamHolders().map(DeferredHolder::getId).toList();
        }

        @Override
        public Stream<R> stream() {
            return map.values().stream().flatMap(GroupedHolder::stream);
        }

        public DeferredHolder<R, T> holder(K1 key1, K2 key2) {
            return group(key1).holder(key2);
        }

        public T get(K1 key1, K2 key2) {
            return group(key1).get(key2);
        }

        public Collection<K2> subKeys(K1 key) {
            return group(key).keys();
        }

        public @Unmodifiable Map<K1, GroupedHolder<K2, R, T>> map() {
            return Collections.unmodifiableMap(map);
        }
    }
}
