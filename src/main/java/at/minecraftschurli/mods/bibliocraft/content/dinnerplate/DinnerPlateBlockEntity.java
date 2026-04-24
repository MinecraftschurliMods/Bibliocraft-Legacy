package at.minecraftschurli.mods.bibliocraft.content.dinnerplate;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DinnerPlateBlockEntity extends BCBlockEntity {
    public DinnerPlateBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.DINNER_PLATE.get(), 1, pos, state);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.get(DataComponents.FOOD) != null;
    }
}
