package at.minecraftschurli.mods.bibliocraft.util;

import at.minecraftschurli.mods.bibliocraft.api.BibliocraftApi;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.timeline.Timelines;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ContainerOrHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/// Utility class holding various helper methods.
public final class BCUtil {
    private static final Int2IntMap XP_LEVEL_CACHE = Util.make(new Int2IntOpenHashMap(), map -> {
        for (int i = 0; i <= 30; i++) {
            map.put(i, calculateExperienceForLevel(i));
        }
    });

    /// @param path The path to use.
    /// @return A [Identifier] with the "minecraft" namespace and the given path.
    public static Identifier mcLoc(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    /// @param path The path to use.
    /// @return A [Identifier] with the "c" namespace and the given path.
    public static Identifier cLoc(String path) {
        return Identifier.fromNamespaceAndPath("c", path);
    }

    /// @param path The path to use.
    /// @return A [Identifier] with the "bibliocraft" namespace and the given path.
    public static Identifier bcLoc(String path) {
        return Identifier.fromNamespaceAndPath(BibliocraftApi.MOD_ID, path);
    }

    /// @param namespace The namespace to use.
    /// @param path      The path to use.
    /// @return A [Identifier] with the given namespace and path.
    public static Identifier modLoc(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    /// Extends the given [List] to the given size, filling the new spaces with the provided values.
    ///
    /// @param list The [List] to extend.
    /// @param size The size the [List] should be extended to.
    /// @param fill The value to fill the new spots with.
    /// @param <T>  The generic type of the list.
    /// @return The given [List], extended to the given size.
    public static <T> List<T> extend(List<T> list, int size, T fill) {
        for (int i = list.size(); i < size; i++) {
            list.add(fill);
        }
        return list;
    }

    /// @return A [Stream] of all [ChatFormatting]s that represent a color.
    public static Stream<ChatFormatting> getChatFormattingColors() {
        return Arrays.stream(ChatFormatting.values()).filter(ChatFormatting::isColor);
    }

    /// Returns the duration of a day in the given [Level].
    ///
    /// @param level The [Level] to get the day duration for.
    /// @return The duration of a day in the given [Level].
    public static int getDayDuration(Level level) {
        return level.registryAccess().get(Timelines.OVERWORLD_DAY).flatMap(timeline -> timeline.value().periodTicks()).orElse(0);
    }

    /// Returns the time of day in the given [Level].
    ///
    /// @param level The [Level] to get the day time for.
    /// @return The time of day in the given [Level].
    public static int getDayTime(Level level) {
        ClockManager clockManager = level.clockManager();
        return level.registryAccess().get(Timelines.OVERWORLD_DAY).map(timeline -> (int) timeline.value().getCurrentTicks(clockManager)).orElse(0);
    }

    /// Looks up the total amount of experience represented by a certain level.
    ///
    /// @param level The experience level to look up the experience amount for.
    /// @return The total amount of experience represented by the given level.
    public static int getExperienceForLevel(int level) {
        if (!XP_LEVEL_CACHE.containsKey(level)) {
            XP_LEVEL_CACHE.put(level, calculateExperienceForLevel(level));
        }
        return XP_LEVEL_CACHE.get(level);
    }

    /// Looks up the level for a corresponding experience value.
    ///
    /// @param experience The experience to look up the level amount for.
    /// @return The level represented by the given amount of experience.
    public static int getLevelForExperience(int experience) {
        int i = 0;
        while (true) {
            int value = getExperienceForLevel(i);
            if (value == experience) return i;
            if (value > experience) return i - 1;
            i++;
        }
    }

    /// Calculates the total amount of experience represented by the given level.
    /// See <a href="https://minecraft.wiki/w/Experience#Leveling_up">the Minecraft Wiki article</a> for more information.
    ///
    /// @param level The experience level to calculate the experience amount for.
    /// @return The total amount of experience represented by the given level.
    public static int calculateExperienceForLevel(int level) {
        if (level <= 16) return level * level + 6 * level;
        if (level <= 31) return (int) (2.5 * level * level - 40.5 * level + 360);
        return (int) (4.5 * level * level + 162.5 * level + 2220);
    }

    /// Returns the [ResourceHandler<ItemResource>] at the given [BlockPos] in the given [Level], if available.
    ///
    /// @param level     The [Level] to query.
    /// @param pos       The [BlockPos] to query.
    /// @param direction The [Direction] to pass in.
    @Nullable
    public static ResourceHandler<ItemResource> getItemHandler(Level level, BlockPos pos, @Nullable Direction direction) {
        ContainerOrHandler containerOrHandler = HopperBlockEntity.getContainerOrHandlerAt(level, pos, direction);
        if (containerOrHandler.isEmpty()) return null;
        Container container = containerOrHandler.container();
        return container == null ? nonNull(containerOrHandler.itemHandler()) : VanillaContainerWrapper.of(container);
    }

    /// Returns a display name for the given position. If there is a nameable block entity at the position, the block entity's name is returned, otherwise the block's name is returned.
    ///
    /// @param level The [Level] to get the display name for.
    /// @param pos   The [BlockPos] to get the display name for.
    /// @return The display name to use for the given position.
    public static Component getNameAtPos(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof Nameable nameable ? nameable.getDisplayName() : level.getBlockState(pos).getBlock().getName();
    }

    /// Returns a display name for the given [BlockEntity]. If it is nameable, the block entity's name is returned, otherwise the block's name is returned.
    ///
    /// @param blockEntity The [BlockEntity] to get the display name for.
    /// @return The display name to use for the given [BlockEntity].
    public static Component getNameForBE(BlockEntity blockEntity) {
        return blockEntity instanceof Nameable nameable ? nameable.getDisplayName() : blockEntity.getBlockState().getBlock().getName();
    }

    /// Returns the max of multiple ints.
    ///
    /// @param ints The ints to get the max of.
    /// @return The max int in the input.
    /// @throws IllegalArgumentException If the input array is empty.
    public static int max(int... ints) {
        return Arrays.stream(ints).max().orElseThrow(IllegalArgumentException::new);
    }

    /// Merges a given collection with the given elements. Does not mutate the original collection.
    ///
    /// @param collection The collection to merge.
    /// @param others     The other elements to merge.
    /// @param <T>        The type of the collection.
    /// @return A new collection with the contents of the given collection and the given vararg parameter.
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T> collection, T... others) {
        return merge(collection, Arrays.stream(others).toList());
    }

    /// Merges two collections. Does not mutate the original collections.
    ///
    /// @param collections The collections to merge.
    /// @param <T>         The type of the collection.
    /// @return A new collection with the contents of the given collections.
    @SafeVarargs
    public static <T> Collection<T> merge(Collection<T>... collections) {
        List<T> list = new ArrayList<>();
        for (Collection<T> collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /// Merges two collections, disregarding their types. Does not mutate the original collections.
    ///
    /// @param collections The collections to merge.
    /// @return A new collection with the contents of the given collections.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Collection<T> mergeRaw(Collection... collections) {
        List<T> list = new ArrayList<>();
        for (Collection collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    /// Helper to reduce [Objects#requireNonNull(Object)] boilerplate.
    ///
    /// @param t   The nullable object.
    /// @param <T> The type of the object.
    /// @return The input object, guarded by a non-null check.
    @Contract("null -> fail")
    public static <T> T nonNull(@Nullable T t) {
        return Objects.requireNonNull(t);
    }

    /// Shorthand to open a menu for the block entity at the given position.
    ///
    /// @param player The player to open the menu for.
    /// @param level  The level of the block entity.
    /// @param pos    The position of the block entity.
    public static void openBEMenu(Player player, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MenuProvider mp && player instanceof ServerPlayer sp) {
            sp.openMenu(mp, buf -> buf.writeBlockPos(pos));
        }
    }

    /// Returns a [Comparator] that is reversed, i.e. will sort elements in the reverse order of the original [Comparator].
    ///
    /// @param comparator The [Comparator] comparator to reverse.
    /// @param <T>        The generic type of the [Comparator].
    /// @return The reversed [Comparator].
    public static <T> Comparator<T> reverseComparator(Comparator<T> comparator) {
        return (a, b) -> -comparator.compare(a, b);
    }

    /// @param vec A [Vec3i].
    /// @return The given [Vec3i], represented as a [Vec3].
    public static Vec3 toVec3(Vec3i vec) {
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }

    /// Attempts to insert the given [ItemStack] into a container at the given [BlockPos] in the given [Level] from the given [Direction], if possible.
    ///
    /// @param level     The [Level] the insertion takes place in.
    /// @param pos       The [BlockPos] the insertion takes place at.
    /// @param direction The [Direction] from which the insertion happens.
    /// @param stack     The [ItemStack] to be inserted.
    /// @param source    The source of the insertion. May be null.
    public static void tryInsert(Level level, BlockPos pos, Direction direction, ItemStack stack, @Nullable BCBlockEntity source) {
        ResourceHandler<ItemResource> handler = getItemHandler(level, pos, direction);
        if (handler == null || ResourceHandlerUtil.isFull(handler)) return;
        try (Transaction transaction = Transaction.openRoot()) {
            if (handler.insert(ItemResource.of(stack), 1, transaction) == 1) {
                transaction.commit();
                stack.shrink(1);
            }
        }
    }

    /// Swap the given item stack with the contents of the given slot in the given {@link BCItemHandler},
    /// the itemSetter is used to set the extracted {@link ItemStack} to where the stack is coming from.
    public static boolean swapItem(ItemStack stack, Consumer<ItemStack> itemSetter, BCItemHandler itemHandler, int slot) {
        int amount = itemHandler.getAmountAsInt(slot);
        ItemResource resource = itemHandler.getResource(slot);
        if (stack.isEmpty() && resource.isEmpty()) {
            return false;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            ItemStack extracted = ItemStack.EMPTY;
            if (!resource.isEmpty()) {
                int extract = itemHandler.extract(slot, resource, amount, transaction);
                if (amount != extract) {
                    return false;
                }
                extracted = resource.toStack(extract);
            }
            if (!stack.isEmpty()) {
                int insert = itemHandler.insert(slot, ItemResource.of(stack), stack.count(), transaction);
                if (insert != stack.count()) {
                    return false;
                }
            }
            itemSetter.accept(extracted);
            transaction.commit();
        }
        return true;
    }
}
