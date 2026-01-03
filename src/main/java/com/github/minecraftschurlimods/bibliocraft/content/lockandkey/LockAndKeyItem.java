package com.github.minecraftschurlimods.bibliocraft.content.lockandkey;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.lockandkey.LockAndKeyBehavior;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class LockAndKeyItem extends Item {
    public LockAndKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.isSecondaryUseActive()) return super.useOn(context);
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        Player player = context.getPlayer();
        if (blockEntity == null || player == null) return super.useOn(context);
        ItemStack stack = context.getItemInHand();
        LockAndKeyBehavior<BlockEntity> behavior = BibliocraftApi.getLockAndKeyBehaviors().get(blockEntity);
        if (behavior == null) return super.useOn(context);
        Component name = behavior.getDisplayName(blockEntity);
        if (!stack.has(DataComponents.CUSTOM_NAME)) {
            player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_NO_CUSTOM_NAME_KEY, name), true);
            if (!level.isClientSide()) {
                Vec3 pos = context.getClickLocation();
                level.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.FAIL;
        }
        LockCode lock = behavior.getLockKey(blockEntity);
        if (lock == LockCode.NO_LOCK) {
            behavior.setLockKey(blockEntity, newLockCode(stack));
            blockEntity.setChanged();
            player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_LOCKED_KEY, name), true);
            return InteractionResult.SUCCESS;
        } else if (lock.unlocksWith(stack)) {
            behavior.setLockKey(blockEntity, LockCode.NO_LOCK);
            blockEntity.setChanged();
            player.displayClientMessage(Component.translatable(Translations.LOCK_AND_KEY_UNLOCKED_KEY, name), true);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private LockCode newLockCode(ItemStack stack) {
        Component name = stack.getOrDefault(DataComponents.CUSTOM_NAME, Component.empty());
        return new LockCode(ItemPredicate.Builder
                .item()
                .of(BuiltInRegistries.ITEM, this)
                .withComponents(DataComponentMatchers.Builder
                        .components()
                        .exact(DataComponentExactPredicate.builder()
                                .expect(DataComponents.CUSTOM_NAME, name)
                                .build())
                        .build())
                .build());
    }
}
