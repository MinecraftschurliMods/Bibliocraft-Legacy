package com.github.minecraftschurlimods.bibliocraft.util.content;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Abstract superclass for rotatable entity blocks that have in-world interactions.
 */
@SuppressWarnings("deprecation")
public abstract class BCFacingInteractibleBlock extends BCFacingEntityBlock {
    public BCFacingInteractibleBlock(Properties properties) {
        super(properties);
    }

    /**
     * Determines what slot a player is currently considered to be looking at.
     *
     * @param state The state of the block.
     * @param hit   The hit result of the player looking at the block.
     * @return The slot the player is currently looking at, or -1 if they are not looking at a slot.
     */
    public abstract int lookingAtSlot(BlockState state, BlockHitResult hit);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) return BCUtil.openBEMenu(player, level, pos);
        if (!canAccessFromDirection(state, hit.getDirection())) return super.use(state, level, pos, player, hand, hit);
        ItemStack stack = player.getItemInHand(hand);
        int slot = lookingAtSlot(state, hit);
        if (slot != -1) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BCBlockEntity bcbe) {
                ItemStack slotStack = bcbe.getItem(slot);
                if ((!stack.isEmpty() && bcbe.canPlaceItem(slot, stack)) || !slotStack.isEmpty()) {
                    bcbe.setItem(slot, stack);
                    player.setItemInHand(hand, slotStack);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    /**
     * @param state     The state to check.
     * @param direction The direction to check.
     * @return Whether the player can access this block's inventory from the given direction.
     */
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        Direction facing = state.getValue(FACING);
        return facing == direction || facing == direction.getOpposite();
    }
}
