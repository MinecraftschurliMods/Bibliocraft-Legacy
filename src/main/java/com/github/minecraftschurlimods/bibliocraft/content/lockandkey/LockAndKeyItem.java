package com.github.minecraftschurlimods.bibliocraft.content.lockandkey;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.LockAndKeyBehavior;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
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
        LockAndKeyBehavior<BlockEntity> behavior = BibliocraftApi.getLockAndKeyBehaviors().get(blockEntity);
        if (behavior == null) return super.useOn(context);
        Component name = behavior.nameGetter().apply(blockEntity);
        if (hasNoCustomName(player, stack, name)) return InteractionResult.FAIL;
        LockCode lock = behavior.lockGetter().apply(blockEntity);
        if (lock == LockCode.NO_LOCK) {
            behavior.lockSetter().accept(blockEntity, newLockCode(stack));
            blockEntity.setChanged();
            player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_LOCKED, name), true);
            return InteractionResult.SUCCESS;
        } else if (lock.unlocksWith(stack)) {
            behavior.lockSetter().accept(blockEntity, LockCode.NO_LOCK);
            blockEntity.setChanged();
            player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_UNLOCKED, name), true);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private LockCode newLockCode(ItemStack stack) {
        return new LockCode(stack.getOrDefault(DataComponents.CUSTOM_NAME, Component.empty()).getString());
    }

    private boolean hasNoCustomName(Player player, ItemStack stack, Component displayName) {
        if (stack.has(DataComponents.CUSTOM_NAME)) return false;
        player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_NO_CUSTOM_NAME, displayName), true);
        player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }
}
