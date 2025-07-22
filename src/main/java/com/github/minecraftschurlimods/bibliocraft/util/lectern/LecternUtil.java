package com.github.minecraftschurlimods.bibliocraft.util.lectern;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Contains helper methods related to lecterns.
 */
public final class LecternUtil {
    /**
     * Called to handle a lectern being right-clicked. Handles putting/taking various Bibliocraft books into/from lecterns.
     *
     * @param level   The {@link Level} context.
     * @param pos     The {@link BlockPos} of the lectern.
     * @param state   The {@link BlockState} of the lectern.
     * @param lectern The {@link LecternBlockEntity} of the lectern.
     * @param player  The {@link Player} using the lectern.
     * @param hand    The {@link InteractionHand} context.
     * @return Whether Bibliocraft has consumed the right click and it should no longer propagate, or not.
     */
    @SuppressWarnings("DataFlowIssue")
    public static boolean handleLecternUse(Level level, BlockPos pos, BlockState state, LecternBlockEntity lectern, Player player, InteractionHand hand) {
        ItemStack book = lectern.getBook();
        if (book.isEmpty()) {
            // this workaround is needed to set the page count correctly
            // if it can be added to vanilla/neo somehow, this can be scrapped
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(ItemTags.LECTERN_BOOKS)) return false;
            if (!stack.has(BCDataComponents.BIG_BOOK_CONTENT) && !stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT) && !stack.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT))
                return false;
            lectern.setBook(stack.consumeAndReturn(1, player));
            LecternBlock.resetBookState(player, level, pos, state, true);
            level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1f, 1f);
            if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
                lectern.pageCount = stack.get(BCDataComponents.BIG_BOOK_CONTENT).pages().size();
            } else if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
                lectern.pageCount = stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT).pages().size();
            } else if (stack.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) {
                lectern.pageCount = 1;
            }
            return true;
        } else if (book.has(BCDataComponents.BIG_BOOK_CONTENT) || book.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT) || book.has(BCDataComponents.STOCKROOM_CATALOG_CONTENT)) {
            if (player.isSecondaryUseActive()) {
                takeLecternBook(player, level, pos);
            } else if (!level.isClientSide() && player instanceof ServerPlayer sp) {
                PacketDistributor.sendToPlayer(sp, new OpenBookInLecternPacket(pos, book));
            }
            return true;
        }
        return false;
    }

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
     *
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
     *
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
            ClientUtil.openStockroomCatalogScreen(stack, player, pos);
        }
    }
}
