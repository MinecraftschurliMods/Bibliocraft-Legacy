package com.github.minecraftschurlimods.bibliocraft.content.typewriter;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TypewriterBlockEntity extends BCBlockEntity {
    private static final int INPUT = 0;
    private static final int OUTPUT = 1;

    public TypewriterBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TYPEWRITER.get(), 2, pos, state);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == INPUT && stack.is(BCTags.Items.TYPEWRITER_PAPER);
    }
}
