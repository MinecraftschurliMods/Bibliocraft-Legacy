package com.github.minecraftschurlimods.bibliocraft.content.lockandkey;

import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class LockAndKeyItem extends Item {
    public LockAndKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.isSecondaryUseActive()) return super.useOn(context);
        BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        Player player = context.getPlayer();
        if (blockEntity == null || player == null) return super.useOn(context);
        ItemStack stack = context.getItemInHand();
        if (blockEntity instanceof BaseContainerBlockEntity container) {
            if (hasNoCustomName(player, stack, container.getDisplayName())) return InteractionResult.FAIL;
            if (container.lockKey == LockCode.NO_LOCK) {
                container.lockKey = newLockCode(context);
                container.setChanged();
            } else if (container.lockKey.unlocksWith(stack)) {
                container.lockKey = LockCode.NO_LOCK;
                container.setChanged();
            }
        } else if (blockEntity instanceof BeaconBlockEntity beacon) {
            if (hasNoCustomName(player, stack, beacon.getDisplayName())) return InteractionResult.FAIL;
            if (beacon.lockKey == LockCode.NO_LOCK) {
                beacon.lockKey = newLockCode(context);
                beacon.setChanged();
            } else if (beacon.lockKey.unlocksWith(stack)) {
                beacon.lockKey = LockCode.NO_LOCK;
                beacon.setChanged();
            }
        } else if (blockEntity instanceof BCBlockEntity bcbe) {
            if (hasNoCustomName(player, stack, bcbe instanceof Nameable nameable ? nameable.getDisplayName() : bcbe.getBlockState().getBlock().getName()))
                return InteractionResult.FAIL;
            if (bcbe.getLockKey() == LockCode.NO_LOCK) {
                bcbe.setLockKey(newLockCode(context));
            } else if (bcbe.getLockKey().unlocksWith(stack)) {
                bcbe.setLockKey(LockCode.NO_LOCK);
            }
        }
        return super.useOn(context);
    }

    private LockCode newLockCode(UseOnContext context) {
        return new LockCode(context.getItemInHand().getOrDefault(DataComponents.CUSTOM_NAME, Component.empty()).getString());
    }

    private boolean hasNoCustomName(Player player, ItemStack stack, Component displayName) {
        if (stack.has(DataComponents.CUSTOM_NAME)) return false;
        player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_NO_CUSTOM_NAME, displayName), true);
        player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }
}
