package com.github.minecraftschurlimods.bibliocraft.content.toolrack;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ToolRackBlockEntity extends BCMenuBlockEntity {
    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TOOL_RACK.get(), 4, defaultName("tool_rack"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ToolRackMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.TOOL_RACK_TOOLS);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
