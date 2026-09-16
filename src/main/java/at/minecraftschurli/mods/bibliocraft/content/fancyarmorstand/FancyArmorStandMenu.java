package at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand;

import at.minecraftschurli.mods.bibliocraft.init.BCMenus;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class FancyArmorStandMenu extends BCMenu<FancyArmorStandBlockEntity> {
    public FancyArmorStandMenu(int id, Inventory inventory, FancyArmorStandBlockEntity blockEntity) {
        super(BCMenus.FANCY_ARMOR_STAND.get(), id, inventory, blockEntity);
    }

    public FancyArmorStandMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        super(BCMenus.FANCY_ARMOR_STAND.get(), id, inventory, buf);
    }

    @Override
    protected void addSlots(Inventory inventory) {
        BCItemHandler itemHandler = getItemHandler();
        addSlot(new EquipableSlot(itemHandler, 0, 80, 8, EquipmentSlot.HEAD));
        addSlot(new EquipableSlot(itemHandler, 1, 80, 26, EquipmentSlot.CHEST));
        addSlot(new EquipableSlot(itemHandler, 2, 80, 44, EquipmentSlot.LEGS));
        addSlot(new EquipableSlot(itemHandler, 3, 80, 62, EquipmentSlot.FEET));
        addInventorySlots(inventory, 8, 84);
        addSlot(new PlayerEquipableSlot(inventory, 39, 126, 8, EquipmentSlot.HEAD));
        addSlot(new PlayerEquipableSlot(inventory, 38, 126, 26, EquipmentSlot.CHEST));
        addSlot(new PlayerEquipableSlot(inventory, 37, 126, 44, EquipmentSlot.LEGS));
        addSlot(new PlayerEquipableSlot(inventory, 36, 126, 62, EquipmentSlot.FEET));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        int slotCount = getItemHandler().size();
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();
        int start = 0;
        int hotbar = start + slotCount;
        int inventory = hotbar + 9;
        int inventoryArmor = inventory + 27;
        int end = inventoryArmor + 4;
        if (index < hotbar) { // If slot is an armor stand slot
            // Try moving to the inventory armor, or hotbar or inventory
            if (!moveItemStackTo(stack, inventoryArmor, end, true)
                && !moveItemStackTo(stack, hotbar, inventoryArmor, false)
            ) return ItemStack.EMPTY;
        } else if (index >= inventoryArmor) { // If slot is an inventory armor slot
            // Try moving to the armor stand, or hotbar or inventory
            if (!moveItemStackTo(stack, start, hotbar, false)
                && !moveItemStackTo(stack, hotbar, inventoryArmor, false)
            ) return ItemStack.EMPTY;
        } else if (index < inventory) { // If slot is a hotbar slot
            // Try moving to the armor stand, inventory armor, or inventory
            if (!moveItemStackTo(stack, start, hotbar, false)
                && !moveItemStackTo(stack, inventoryArmor, end, true)
                && !moveItemStackTo(stack, inventory, inventoryArmor, false)
            ) return ItemStack.EMPTY;
        } else { // If slot is an inventory slot
            // Try moving to the armor stand, inventory armor, or hotbar
            if (!moveItemStackTo(stack, start, hotbar, false)
                && !moveItemStackTo(stack, inventoryArmor, end, true)
                && !moveItemStackTo(stack, hotbar, inventory, false)
            ) return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return originalStack;
    }

    /// Variant of [Slot] that only accepts items that go in a given [EquipmentSlot]. Uses [ResourceHandlerSlot] as its base class.
    private static class EquipableSlot extends ResourceHandlerSlot {
        private final EquipmentSlot slotType;

        /// @param itemHandler The [BCItemHandler] this slot is accessing.
        /// @param index       The slot index.
        /// @param x           The x position.
        /// @param y           The y position.
        /// @param slotType    The [EquipmentSlot] to limit slot contents with.
        public EquipableSlot(BCItemHandler itemHandler, int index, int x, int y, EquipmentSlot slotType) {
            super(itemHandler, itemHandler::set, index, x, y);
            this.slotType = slotType;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.get(DataComponents.EQUIPPABLE) instanceof Equippable equippable && equippable.slot() == slotType;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    /// Variant of [Slot] that only accepts items that go in a given [EquipmentSlot]. Uses the vanilla [Slot] as its base class.
    private static class PlayerEquipableSlot extends Slot {
        private final EquipmentSlot slotType;

        /// @param container The [Container] this slot is in.
        /// @param index     The slot index.
        /// @param x         The x position.
        /// @param y         The y position.
        /// @param slotType  The [EquipmentSlot] to limit slot contents with.
        public PlayerEquipableSlot(Container container, int index, int x, int y, EquipmentSlot slotType) {
            super(container, index, x, y);
            this.slotType = slotType;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.get(DataComponents.EQUIPPABLE) instanceof Equippable equippable && equippable.slot() == slotType;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
