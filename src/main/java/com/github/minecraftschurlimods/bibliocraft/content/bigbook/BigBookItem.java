package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

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

    @Override
    public Component getName(ItemStack stack) {
        WrittenBigBookContent content = stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT);
        if (content != null) {
            String title = content.title();
            if (!StringUtil.isBlank(title)) {
                return Component.literal(title);
            }
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        WrittenBigBookContent content = stack.get(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT);
        if (content != null) {
            String author = content.author();
            if (!StringUtil.isBlank(author)) {
                tooltipComponents.add(Component.translatable("book.byAuthor", author).withStyle(ChatFormatting.GRAY));
            }
            tooltipComponents.add(Component.translatable("book.generation." + content.generation()).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
