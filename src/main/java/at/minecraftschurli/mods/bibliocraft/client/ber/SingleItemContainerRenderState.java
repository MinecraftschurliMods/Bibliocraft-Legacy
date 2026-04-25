package at.minecraftschurli.mods.bibliocraft.client.ber;

import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.Nullable;

public class SingleItemContainerRenderState extends BlockEntityRenderState {
    @Nullable
    public ItemStackRenderState itemStackState;

    public void fill(BCBlockEntity blockEntity, ItemDisplayContext displayContext, ItemModelResolver itemModelResolver) {
        ItemStack itemstack = ItemUtil.getStack(blockEntity.getItemHandler(), 0);
        if (itemstack.isEmpty()) {
            return;
        }
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        itemStackState = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(itemStackState, itemstack, displayContext, blockEntity.level(), blockEntity, i);
    }
}
