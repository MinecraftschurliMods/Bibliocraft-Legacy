package com.github.minecraftschurlimods.bibliocraft.block.bookcase;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BookcaseBlock extends BCBlock {
    public BookcaseBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookcaseBlockEntity(pos, state);
    }
}
