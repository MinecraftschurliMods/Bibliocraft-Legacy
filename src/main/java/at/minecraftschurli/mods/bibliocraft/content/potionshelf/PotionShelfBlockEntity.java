package at.minecraftschurli.mods.bibliocraft.content.potionshelf;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PotionShelfBlockEntity extends BCMenuBlockEntity {
    public PotionShelfBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.POTION_SHELF.get(), 12, defaultName("potion_shelf"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PotionShelfMenu(id, inventory, this);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.POTION_SHELF_POTIONS);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
