package at.minecraftschurli.mods.bibliocraft.content.displaycase;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayCaseBlockEntity extends BCBlockEntity {
    public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.DISPLAY_CASE.get(), 1, 1, pos, state);
    }
}
