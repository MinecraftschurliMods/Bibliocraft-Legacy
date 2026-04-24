package at.minecraftschurli.mods.bibliocraft.content.discrack;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DiscRackBlockEntity extends BCMenuBlockEntity {
    public DiscRackBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.DISC_RACK.get(), 9, defaultName("disc_rack"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new DiscRackMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.DISC_RACK_DISCS);
    }
}
