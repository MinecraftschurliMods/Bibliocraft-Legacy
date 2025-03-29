package com.github.minecraftschurlimods.bibliocraft.api.lockandkey;

import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Holds all information Bibliocraft needs to make the Lock and Key item work on a block entity.
 * @param <T> The type of the {@link BlockEntity}.
 */
public interface LockAndKeyBehavior<T extends BlockEntity> {

    /**
     * Returns the {@link BlockEntity}'s {@link LockCode}.
     * 
     * @param blockEntity the {@link BlockEntity}
     * @return the {@link BlockEntity}'s {@link LockCode}.
     */
    LockCode getLockKey(T blockEntity);

    /**
     * Sets the {@link BlockEntity}'s {@link LockCode}.
     * 
     * @param blockEntity the {@link BlockEntity}
     * @param lock the {@link LockCode} to set
     */
    void setLockKey(T blockEntity, LockCode lock);

    /**
     * Returns the {@link BlockEntity}'s display name.
     * 
     * @param blockEntity the {@link BlockEntity}
     * @return the {@link BlockEntity}'s display name.
     */
    Component getDisplayName(T blockEntity);

    record Simple<T extends BlockEntity>(
            Function<T, LockCode> lockGetter,
            BiConsumer<T, LockCode> lockSetter,
            Function<T, Component> nameGetter
    ) implements LockAndKeyBehavior<T> {
        @Override
        public LockCode getLockKey(T blockEntity) {
            return lockGetter.apply(blockEntity);
        }

        @Override
        public void setLockKey(T blockEntity, LockCode lock) {
            lockSetter.accept(blockEntity, lock);
        }

        @Override
        public Component getDisplayName(T blockEntity) {
            return nameGetter.apply(blockEntity);
        }
    }
}
