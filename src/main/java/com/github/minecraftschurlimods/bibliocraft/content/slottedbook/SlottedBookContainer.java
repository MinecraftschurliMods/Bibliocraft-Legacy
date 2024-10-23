package com.github.minecraftschurlimods.bibliocraft.content.slottedbook;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class SlottedBookContainer extends SimpleContainer {
    private final ItemStack stack;

    public SlottedBookContainer(ItemStack stack) {
        super(1);
        this.stack = stack;
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(getItems());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
    }
}
