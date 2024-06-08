package com.github.minecraftschurlimods.bibliocraft.content.discrack;

import com.github.minecraftschurlimods.bibliocraft.util.content.BCFacingInteractibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDiscRackBlock extends BCFacingInteractibleBlock {
    public AbstractDiscRackBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DiscRackBlockEntity(pos, state);
    }
}
