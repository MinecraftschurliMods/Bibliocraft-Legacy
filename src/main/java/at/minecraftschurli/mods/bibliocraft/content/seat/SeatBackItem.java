package at.minecraftschurli.mods.bibliocraft.content.seat;

import at.minecraftschurli.mods.bibliocraft.api.woodtype.BibliocraftWoodType;
import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.init.BCItems;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.block.ColoredWoodTypeBlockItem;
import at.minecraftschurli.mods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    public SeatBackItem(GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, ? extends Block> holder, BibliocraftWoodType woodType, DyeColor color, SeatBackType type, Properties properties) {
        super(holder, woodType, color, properties);
        this.type = type;
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = BCUtil.nonNull(getBlock().getStateForPlacement(context));
        return canPlace(context, state) ? state.setValue(SeatBackBlock.TYPE, type) : null;
    }
}

