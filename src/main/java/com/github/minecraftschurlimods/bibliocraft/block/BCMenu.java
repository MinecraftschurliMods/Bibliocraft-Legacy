package com.github.minecraftschurlimods.bibliocraft.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.Objects;

public abstract class BCMenu<T extends BCBlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;

    public BCMenu(MenuType<?> type, int id, Inventory inventory, T blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
        addSlots(inventory, blockEntity);
    }

    @SuppressWarnings("unchecked")
    public BCMenu(MenuType<?> type, int id, Inventory inventory, FriendlyByteBuf data) {
        this(type, id, inventory, (T) Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())));
    }

    protected abstract void addSlots(Inventory inventory, T blockEntity);

    protected void addInventorySlots(Inventory inventory, int x, int y) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, x + i * 18, y + 58));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, i * 9 + j + 9, x + j * 18, y + i * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }
}
