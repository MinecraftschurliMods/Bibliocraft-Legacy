package at.minecraftschurli.mods.bibliocraft.content.shelf;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ShelfBlockEntity extends BCMenuBlockEntity {
    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.SHELF.get(), 4, defaultName("shelf"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ShelfMenu(id, inventory, this);
    }
}
