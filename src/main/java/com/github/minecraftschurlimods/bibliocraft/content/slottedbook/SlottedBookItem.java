package com.github.minecraftschurlimods.bibliocraft.content.slottedbook;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SlottedBookItem extends Item {
    public SlottedBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide()) return InteractionResultHolder.success(player.getItemInHand(usedHand));
        if (player instanceof ServerPlayer sp) {
            sp.openMenu(new SimpleMenuProvider((id, inv, p) -> new SlottedBookMenu(id, inv, usedHand), getDescription()), buf -> buf.writeEnum(usedHand));
        }
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }
}
