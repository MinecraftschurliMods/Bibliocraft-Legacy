package com.github.minecraftschurlimods.bibliocraft.content.seat;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeBlockItem;
import com.github.minecraftschurlimods.bibliocraft.util.init.ColoredWoodTypeDeferredHolder;
import net.minecraft.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class SeatBackItem extends ColoredWoodTypeBlockItem {
    @SuppressWarnings("CodeBlock2Expr")
    public static final Map<SeatBackBlock, Map<SeatBackType, Supplier<SeatBackItem>>> BLOCK_MAP = Collections.unmodifiableMap(Util.make(new HashMap<>(), map -> {
        BCBlocks.SEAT_BACK.map().forEach((wood, coloredHolder) -> coloredHolder.map().forEach((color, holder) -> {
            map.put(holder.get(), Map.of(
                    SeatBackType.SMALL, BCItems.SMALL_SEAT_BACK.holder(wood, color),
                    SeatBackType.RAISED, BCItems.RAISED_SEAT_BACK.holder(wood, color),
                    SeatBackType.FLAT, BCItems.FLAT_SEAT_BACK.holder(wood, color),
                    SeatBackType.TALL, BCItems.TALL_SEAT_BACK.holder(wood, color),
                    SeatBackType.FANCY, BCItems.FANCY_SEAT_BACK.holder(wood, color)
            ));
        }));
    }));
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

