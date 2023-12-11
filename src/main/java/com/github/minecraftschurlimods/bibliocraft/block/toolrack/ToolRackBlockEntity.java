package com.github.minecraftschurlimods.bibliocraft.block.toolrack;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ToolRackBlockEntity extends BCBlockEntity implements MenuProvider {
    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TOOL_RACK.get(), 4, title("tool_rack"), pos, state);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ToolRackMenu(id, inventory, this);
    }
}