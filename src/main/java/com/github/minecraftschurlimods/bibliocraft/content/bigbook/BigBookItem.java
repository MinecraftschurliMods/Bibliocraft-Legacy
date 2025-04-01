package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BigBookItem extends Item {
    private final boolean writable;

    public BigBookItem(boolean writable) {
        super(writable ? new Properties() : new Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
        this.writable = writable;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientUtil.openBigBookScreen(stack, writable);
        }
        return InteractionResultHolder.success(stack);
    }
}
