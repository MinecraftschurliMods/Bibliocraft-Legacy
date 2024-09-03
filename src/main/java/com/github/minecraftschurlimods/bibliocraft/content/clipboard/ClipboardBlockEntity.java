package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClipboardBlockEntity extends BlockEntity {
    public ClipboardBlockEntity(BlockPos pos, BlockState blockState) {
        super(BCBlockEntities.CLIPBOARD.get(), pos, blockState);
    }
}
