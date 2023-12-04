package com.github.minecraftschurlimods.bibliocraft.block;

import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.NetworkHooks;

public abstract class BCInteractibleBlock extends BCBlock {
    public BCInteractibleBlock(Properties properties) {
        super(properties);
    }

    public abstract int lookingAtSlot(BlockState state, BlockHitResult hit);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) level.getBlockEntity(pos), pos);
            return InteractionResult.SUCCESS;
        }
        Direction direction = state.getValue(FACING);
        if (hit.getDirection() != direction && hit.getDirection() != direction.getOpposite())
            return super.use(state, level, pos, player, hand, hit);
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
}
