package at.minecraftschurli.mods.bibliocraft.content.toolrack;

import at.minecraftschurli.mods.bibliocraft.init.BCBlockEntities;
import at.minecraftschurli.mods.bibliocraft.init.BCTags;
import at.minecraftschurli.mods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.UnknownNullability;

public class ToolRackBlockEntity extends BCMenuBlockEntity {
    public ToolRackBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TOOL_RACK.get(), 4, 1, defaultName("tool_rack"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ToolRackMenu(id, inventory, this);
    }

    @Override
    public boolean isValid(int slot, ItemResource stack) {
        return stack.is(BCTags.Items.TOOL_RACK_TOOLS);
    }
}
