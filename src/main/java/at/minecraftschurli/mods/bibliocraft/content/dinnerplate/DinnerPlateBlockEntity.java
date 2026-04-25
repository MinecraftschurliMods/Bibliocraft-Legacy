package at.minecraftschurli.mods.bibliocraft.content.dinnerplate;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.UnknownNullability;

public class DinnerPlateBlockEntity extends BCBlockEntity {
    public DinnerPlateBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.DINNER_PLATE.get(), 1, pos, state);
    }

    @Override
    public boolean isValid(int slot, ItemResource stack) {
        return stack.get(DataComponents.FOOD) != null;
    }
}
