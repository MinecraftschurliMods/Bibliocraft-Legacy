package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeBlockItem;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SeatBackItem extends ColoredWoodTypeBlockItem {
    public final SeatBackType type;

    public SeatBackItem(ColoredWoodTypeDeferredHolder<Block, ? extends Block> holder, BibliocraftWoodType woodType, DyeColor color, SeatBackType type) {
        super(holder, woodType, color);
        this.type = type;
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = Objects.requireNonNull(getBlock().getStateForPlacement(context));
        return canPlace(context, state) ? state.setValue(SeatBackBlock.TYPE, type) : null;
    }

    @Override
    public String getDescriptionId() {
        return super.getOrCreateDescriptionId();
    }
}
