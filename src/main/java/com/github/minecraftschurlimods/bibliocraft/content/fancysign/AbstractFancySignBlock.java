package com.github.minecraftschurlimods.bibliocraft.content.fancysign;

import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingEntityBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jspecify.annotations.Nullable;

public abstract class AbstractFancySignBlock extends BCFacingEntityBlock {
    public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");
    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");

    public AbstractFancySignBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(UPSIDE_DOWN, false).setValue(WAXED, false).setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UPSIDE_DOWN, WAXED);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FancySignBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(BCTags.Items.FANCY_SIGN_WAX) && !state.getValue(WAXED)) {
            handleWaxing(true, stack, state, level, pos, player, hand);
            return InteractionResult.SUCCESS;
        }
        if (stack.canPerformAction(ItemAbilities.AXE_WAX_OFF) && state.getValue(WAXED)) {
            handleWaxing(false, stack, state, level, pos, player, hand);
            return InteractionResult.SUCCESS;
        }
        Component component = stack.get(DataComponents.CUSTOM_NAME);
        if (component != null) {
            String text = component.getString();
            if ("Dinnerbone".equals(text) || "Grumm".equals(text)) {
                level.setBlockAndUpdate(pos, state.setValue(UPSIDE_DOWN, !state.getValue(UPSIDE_DOWN)));
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private static void handleWaxing(boolean wax, ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        BlockState newState = state.setValue(WAXED, wax);
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
        }
        if (stack.isDamageableItem()) {
            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        } else {
            stack.shrink(1);
        }
        level.setBlockAndUpdate(pos, newState);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
        level.levelEvent(player, wax ? LevelEvent.PARTICLES_AND_SOUND_WAX_ON : LevelEvent.PARTICLES_WAX_OFF, pos, 0);
    }
}
