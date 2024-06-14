package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility class holding various helper methods.
 */
public final class BCUtil {
    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "minecraft" namespace and the given path.
     */
    public static ResourceLocation mcLoc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "forge" namespace and the given path.
     */
    public static ResourceLocation forgeLoc(String path) {
        return new ResourceLocation("forge", path);
    }

    /**
     * @param path The path to use.
     * @return A {@link ResourceLocation} with the "bibliocraft" namespace and the given path.
     */
    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(BibliocraftApi.MOD_ID, path);
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
     * Shorthand to open a menu for the block entity at the given position and return {@link InteractionResult#SUCCESS}.
     *
     * @param player The player to open the menu for.
     * @param level  The level of the block entity.
     * @param pos    The position of the block entity.
     * @return {@link InteractionResult#SUCCESS}
     */
    public static InteractionResult openBEMenu(Player player, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof MenuProvider menu && player instanceof ServerPlayer sp) {
            sp.openMenu(menu, buf -> buf.writeBlockPos(pos));
        }
        return InteractionResult.SUCCESS;
    }

    @NotNull
    public static ItemInteractionResult getItemInteractionResult(InteractionResult interactionResult) {
        return switch (interactionResult) {
            case SUCCESS, SUCCESS_NO_ITEM_USED -> ItemInteractionResult.SUCCESS;
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
            case PASS -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            case FAIL -> ItemInteractionResult.FAIL;
        };
    }
}
