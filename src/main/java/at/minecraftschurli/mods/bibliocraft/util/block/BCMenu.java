package at.minecraftschurli.mods.bibliocraft.util.block;

import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.slot.BCSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/// Abstract superclass for all menus of this mod.
///
/// @param <T> The block entity this menu is associated with.
@SuppressWarnings("SameParameterValue")
public abstract class BCMenu<T extends BCMenuBlockEntity> extends AbstractContainerMenu {
    protected final T blockEntity;

    /// @param type        The [MenuType] to use.
    /// @param id          The id of the menu, provided by the game.
    /// @param inventory   The [Inventory] to use, provided by the game.
    /// @param blockEntity The block entity associated with the menu.
    public BCMenu(MenuType<?> type, int id, Inventory inventory, T blockEntity) {
        super(type, id);
        this.blockEntity = blockEntity;
        addSlots(inventory);
    }

    /// @param type      The [MenuType] to use.
    /// @param id        The id of the menu, provided by the game.
    /// @param inventory The [Inventory] to use, provided by the game.
    /// @param data      The [FriendlyByteBuf] to read the block entity data from.
    @SuppressWarnings("unchecked")
    public BCMenu(MenuType<?> type, int id, Inventory inventory, FriendlyByteBuf data) {
        this(type, id, inventory, (T) BCUtil.nonNull(inventory.player.level().getBlockEntity(data.readBlockPos())));
    }

    /// Adds slots to this menu.
    ///
    /// @param inventory The player inventory to use.
    protected abstract void addSlots(Inventory inventory);

    /// @return The block entity this menu is associated with.
    public T getBlockEntity() {
        return blockEntity;
    }

    protected void addItemHandlerSlot(int index, int x, int y) {
        addSlot(new BCSlot(blockEntity, index, x, y));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int slotCount = blockEntity.getContainerSize();
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        if (index < slotCount) { // If slot is a BE slot
            // Try moving to the hotbar or inventory
            if (!moveItemStackTo(slot.getItem(), slotCount, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 9) { // If slot is a hotbar slot
            // Try moving to the BE
            if (!moveItemStackTo(stack, 0, slotCount, false))
                return ItemStack.EMPTY;
            // Try moving to the inventory
            if (!moveItemStackTo(stack, slotCount + 9, slotCount + 36, false))
                return ItemStack.EMPTY;
        } else if (index < slotCount + 36) { // If slot is an inventory slot
            // Try moving to the BE
            if (!moveItemStackTo(stack, 0, slotCount, false))
                return ItemStack.EMPTY;
            // Try moving to the hotbar
            if (!moveItemStackTo(stack, slotCount, slotCount + 9, false))
                return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    /// @return The block entity's display name.
    public Component getDisplayName() {
        return blockEntity.getDisplayName();
    }

    /// Helper method for adding the player inventory slots.
    ///
    /// @param inventory The player inventory to add slots for.
    /// @param x         The x position of the inventory.
    /// @param y         The y position of the inventory.
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
}
