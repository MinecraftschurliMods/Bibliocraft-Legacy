package at.minecraftschurli.mods.bibliocraft.content.label;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class LabelBlockEntity extends BCMenuBlockEntity {
    public LabelBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.LABEL.get(), 3, 1, defaultName("label"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new LabelMenu(id, inventory, this);
    }
}
