package com.github.minecraftschurlimods.bibliocraft.content.plumbline;

import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class PlumbLineItem extends Item {
    public PlumbLineItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction face = context.getClickedFace();
        if (face == Direction.UP || context.getPlayer() == null) return super.useOn(context);
        BlockPos pos = context.getClickedPos().offset(face.getNormal());
        BlockState state = context.getLevel().getBlockState(pos);
        int count = 0;
        while (state.isAir() || state.getCollisionShape(context.getLevel(), pos).isEmpty()) {
            count++;
            pos = pos.below();
            state = context.getLevel().getBlockState(pos);
        }
        if (count <= 2) return super.useOn(context);
        context.getPlayer().displayClientMessage(Component.translatable(Translations.PLUMB_LINE_DISTANCE_KEY, count), true);
        return InteractionResult.SUCCESS;
    }
}
