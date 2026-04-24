package at.minecraftschurli.mods.bibliocraft.content.clipboard;

import at.minecraftschurli.mods.bibliocraft.init.BCBlocks;
import at.minecraftschurli.mods.bibliocraft.util.ClientUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ClipboardItem extends BlockItem {
    public ClipboardItem(Properties properties) {
        super(BCBlocks.CLIPBOARD.get(), properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            ClientUtil.openClipboardScreen(stack, hand);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer() != null && context.getPlayer().isSecondaryUseActive() ? super.useOn(context) : InteractionResult.PASS;
    }
}
