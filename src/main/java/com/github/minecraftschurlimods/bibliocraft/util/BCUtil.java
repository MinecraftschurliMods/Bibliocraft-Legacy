package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Utility class holding various helper methods.
 */
public final class BCUtil {
    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "minecraft" namespace and the given path.
     */
    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "c" namespace and the given path.
     */
    public static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "bibliocraft" namespace and the given path.
     */
    public static ResourceLocation bcLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, path);
    }

    /**
     * @param namespace The namespace to use.
     * @param path      The path to use.
     * @return A {@link ResourceLocation} with the given namespace and path.
     */
    public static ResourceLocation modLoc(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    /**
     * Extends the given {@link List} to the given size, filling the new spaces with the provided values.
     *
     * @param list The {@link List} to extend.
     * @param size The size the {@link List} should be extended to.
     * @param fill The value to fill the new spots with.
     * @param <T>  The generic type of the list.
     * @return The given {@link List}, extended to the given size.
     */
    public static <T> List<T> extend(List<T> list, int size, T fill) {
        for (int i = list.size(); i < size; i++) {
            list.add(fill);
        }
        return list;
    }

    /**
     * @return A {@link Stream} of all {@link ChatFormatting}s that represent a color.
     */
    public static Stream<ChatFormatting> getChatFormattingColors() {
        return Arrays.stream(ChatFormatting.values()).filter(ChatFormatting::isColor);
    }

    /**
     * Returns a display name for the given position. If there is a nameable block entity at the position, the block entity's name is returned, otherwise the block's name is returned.
     *
     * @param level The {@link Level} to get the display name for.
     * @param pos   The {@link BlockPos} to get the display name for.
     * @return The display name to use for the given position.
     */
    public static Component getNameAtPos(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof Nameable nameable ? nameable.getDisplayName() : level.getBlockState(pos).getBlock().getName();
    }

    /**
     * Returns a display name for the given {@link BlockEntity}. If it is nameable, the block entity's name is returned, otherwise the block's name is returned.
     *
     * @param blockEntity The {@link BlockEntity} to get the display name for.
     * @return The display name to use for the given {@link BlockEntity}.
     */
    public static Component getNameForBE(BlockEntity blockEntity) {
        return blockEntity instanceof Nameable nameable ? nameable.getDisplayName() : blockEntity.getBlockState().getBlock().getName();
    }

    /**
     * Returns the max of multiple ints.
     *
     * @param ints The ints to get the max of.
     * @return The max int in the input.
     * @throws RuntimeException If the input array is empty.
     */
    public static int max(int... ints) {
        return Arrays.stream(ints).max().orElseThrow(RuntimeException::new);
    }

    /**
     * Merges a given collection with the given elements. Does not mutate the original collection.
     *
     * @param collection The collection to merge.
     * @param others     The other elements to merge.
     * @param <T>        The type of the collection.
     * @return A new collection with the contents of the given collection and the given vararg parameter.
     */
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T> collection, T... others) {
        return merge(collection, Arrays.stream(others).toList());
    }

    /**
     * Merges two collections. Does not mutate the original collections.
     *
     * @param collections The collections to merge.
     * @param <T>         The type of the collection.
     * @return A new collection with the contents of the given collections.
     */
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T>... collections) {
        List<T> list = new ArrayList<>();
        for (Collection<T> collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /**
     * Merges two collections, disregarding their types. Does not mutate the original collections.
     *
     * @param collections The collections to merge.
     * @return A new collection with the contents of the given collections.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Collection<T> mergeRaw(Collection... collections) {
        List<T> list = new ArrayList<>();
        for (Collection collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /**
     * Helper to reduce {@link Objects#requireNonNull(Object)} boilerplate.
     * @param t The nullable object.
     * @return The input object, guarded by a non-null check.
     * @param <T> The type of the object.
     */
    public static <T> T nonNull(T t) {
        return Objects.requireNonNull(t);
    }

    /**
     * Shorthand to open a menu for the block entity at the given position.
     *
     * @param player The player to open the menu for.
     * @param level  The level of the block entity.
     * @param pos    The position of the block entity.
     */
    public static void openBEMenu(Player player, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MenuProvider mp && player instanceof ServerPlayer sp) {
            sp.openMenu(mp, buf -> buf.writeBlockPos(pos));
        }
    }

    /**
     * Returns a {@link Comparator} that is reversed, i.e. will sort elements in the reverse order of the original {@link Comparator}.
     *
     * @param comparator The {@link Comparator} comparator to reverse.
     * @param <T>        The generic type of the {@link Comparator}.
     * @return The reversed {@link Comparator}.
     */
    public static <T> Comparator<T> reverseComparator(Comparator<T> comparator) {
        return (a, b) -> -comparator.compare(a, b);
    }

    /**
     * @param vec A {@link Vec3i}.
     * @return The given {@link Vec3i}, represented as a {@link Vec3}.
     */
    public static Vec3 toVec3(Vec3i vec) {
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Attempts to insert the given {@link ItemStack} into a container at the given {@link BlockPos} in the given {@link Level} from the given {@link Direction}, if possible.
     * First, the vanilla way using {@link Container} is checked. If that doesn't work, the NeoForge way using {@link IItemHandler} is checked.
     * @param level The {@link Level} the insertion takes place in.
     * @param pos The {@link BlockPos} the insertion takes place at.
     * @param direction The {@link Direction} from which the insertion happens.
     * @param stack The {@link ItemStack} to be inserted.
     * @param source The source of the insertion. May be null.
     * @return The {@link ItemStack} left after the insertion has been attempted and, if applicable, succeeded.
     * @param <T> The generic type of the source.
     */
    public static <T extends BlockEntity & Container> ItemStack tryInsert(Level level, BlockPos pos, Direction direction, ItemStack stack, @Nullable T source) {
        Container container = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
        if (container != null) return HopperBlockEntity.addItem(source, container, stack, direction.getOpposite());
        IItemHandler cap = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, level.getBlockState(pos), source, direction);
        if (cap == null) {
            List<Entity> list = level.getEntities((Entity) null, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), EntitySelector.ENTITY_STILL_ALIVE);
            if (!list.isEmpty()) {
                Collections.shuffle(list);
                for (Entity entity : list) {
                    cap = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, direction);
                    if (cap != null) break;
                }
            }
        }
        if (cap != null) {
            for (int slot = 0; slot < cap.getSlots() && !stack.isEmpty(); slot++) {
                stack = cap.insertItem(slot, stack, false);
            }
        }
        return stack;
    }
}
