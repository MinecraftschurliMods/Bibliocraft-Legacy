package com.github.minecraftschurlimods.bibliocraft.content.fancyworkbench;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FancyWorkbenchBlockEntity extends BCMenuBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(17, ItemStack.EMPTY);

    public FancyWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_WORKBENCH.get(), 18, defaultName("fancy_workbench"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FancyWorkbenchMenu(id, inventory, this);
    }
}
