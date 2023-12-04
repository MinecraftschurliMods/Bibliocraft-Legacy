package com.github.minecraftschurlimods.bibliocraft.block.potionshelf;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PotionShelfBlock extends BCBlock {
    public PotionShelfBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
