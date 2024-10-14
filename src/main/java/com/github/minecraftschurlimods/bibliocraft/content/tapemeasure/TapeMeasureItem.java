package com.github.minecraftschurlimods.bibliocraft.content.tapemeasure;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class TapeMeasureItem extends Item {
    public TapeMeasureItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        if (stack.has(BCDataComponents.STORED_POSITION)) {
            BlockPos distance = context.getClickedPos().subtract(stack.getOrDefault(BCDataComponents.STORED_POSITION, StoredPosition.DEFAULT).position());
            player.displayClientMessage(Component.translatable(Translations.TAPE_MEASURE_DISTANCE, distance.distSqr(BlockPos.ZERO), Math.abs(distance.getX()), Math.abs(distance.getY()), Math.abs(distance.getZ())), true);
            stack.remove(BCDataComponents.STORED_POSITION);
        } else {
            stack.set(BCDataComponents.STORED_POSITION, new StoredPosition(pos));
        }
        return InteractionResult.SUCCESS;
    }
}
