package com.github.minecraftschurlimods.bibliocraft.util.lectern;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Contains helper methods related to lecterns.
 */
public final class LecternUtil {
    /**
     * Removes the book from the lectern at the given location and adds it to the {@link Player}'s inventory.
     *
     * @param player The {@link Player} to add the lectern's book to.
     * @param level  The {@link Level} of the lectern.
     * @param pos    The {@link BlockPos} of the lectern.
     */
    public static void takeLecternBook(Player player, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        ItemStack stack = lectern.getBook();
        lectern.setBook(ItemStack.EMPTY);
        LecternBlock.resetBookState(player, level, pos, level.getBlockState(pos), false);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    /**
     * Runs the given {@link Consumer} if a {@link LecternBlockEntity} is found at the given location.
     * @param level    The {@link Level} of the lectern.
     * @param pos      The {@link BlockPos} of the lectern.
     * @param consumer The {@link Consumer} to run.
     */
    public static void tryGetLecternAndRun(Level level, BlockPos pos, Consumer<LecternBlockEntity> consumer) {
        if (!level.getBlockState(pos).hasProperty(LecternBlock.HAS_BOOK) || !level.getBlockState(pos).getValue(LecternBlock.HAS_BOOK))
            return;
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        consumer.accept(lectern);
    }

    /**
     * Runs the given {@link Function} and returns its result if a {@link LecternBlockEntity} is found at the given location.
     * @param level    The {@link Level} of the lectern.
     * @param pos      The {@link BlockPos} of the lectern.
     * @param function The {@link Function} to run.
     */
    @Nullable
    public static <T> T tryGetLecternAndApply(Level level, BlockPos pos, Function<LecternBlockEntity, T> function) {
        if (!level.getBlockState(pos).hasProperty(LecternBlock.HAS_BOOK) || !level.getBlockState(pos).getValue(LecternBlock.HAS_BOOK))
            return null;
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return null;
        return function.apply(lectern);
    }

    /**
     * Opens a screen for the given {@link ItemStack} on the client. The {@link ItemStack} is assumed to be inside a lectern.
     *
     * @param stack  The owning {@link ItemStack} of the screen.
     * @param player The owning {@link Player} of the screen.
     * @param pos    The {@link BlockPos} of the lectern.
     */
    public static void openScreenForLectern(ItemStack stack, Player player, BlockPos pos) {
        if (stack.has(BCDataComponents.BIG_BOOK_CONTENT) || stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
            ClientUtil.openBigBookScreen(stack, player, pos);
        } else if (stack.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) {
            ClientUtil.openStockroomCatalogScreen(stack, pos);
        }
    }
}
