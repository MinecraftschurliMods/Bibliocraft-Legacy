package com.github.minecraftschurlimods.bibliocraft.content.fancyworkbench;

import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyWorkbenchMenu extends BCMenu<FancyWorkbenchBlockEntity> implements CraftingContainer {
    private final ResultContainer resultSlots = new ResultContainer();

    public FancyWorkbenchMenu(int id, Inventory inventory, FancyWorkbenchBlockEntity blockEntity) {
        super(BCMenus.FANCY_WORKBENCH.get(), id, inventory, blockEntity);
    }

    public FancyWorkbenchMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenus.FANCY_WORKBENCH.get(), id, inventory, data);
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return getItems().stream().limit(9).noneMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return getItems().get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(getItems(), slot, amount);
        if (!stack.isEmpty()) {
            setChanged();
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(getItems(), slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }

    @Override
    public void setChanged() {
        blockEntity.setChanged();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < 9; i++) {
            getItems().set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void fillStackedContents(StackedContents contents) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = getItem(i);
            contents.accountSimpleStack(stack);
        }
    }

    @Override
    protected void addSlots(Inventory inventory) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                addSlot(new Slot(this, x + y * 3 + 1, 30 + x * 18, 17 + y * 18));
            }
        }
        addSlot(new ResultSlot(inventory.player, this, resultSlots, 10, 124, 35));
        for (int i = 0; i < 8; i++) {
            addSlot(new Slot(this, i + 10, 17 + i * 18, 81));
        }
        addInventorySlots(inventory, 8, 109);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index); //TODO
    }
}
