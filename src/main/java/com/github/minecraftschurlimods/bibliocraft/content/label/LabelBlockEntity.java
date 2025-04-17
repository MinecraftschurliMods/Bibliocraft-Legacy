package com.github.minecraftschurlimods.bibliocraft.content.label;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class LabelBlockEntity extends BCMenuBlockEntity {
    public LabelBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.LABEL.get(), 3, defaultName("label"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new LabelMenu(id, inventory, this);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
