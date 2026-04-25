package at.minecraftschurli.mods.bibliocraft.client.ber;

import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class MultiItemContainerRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] items;
    
    public void fill(BCBlockEntity blockEntity, ItemDisplayContext displayContext, ItemModelResolver itemModelResolver) {
        BCItemHandler itemHandler = blockEntity.getItemHandler();
        items = new ItemStackRenderState[itemHandler.size()];
        int i = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        for (int j = 0; j < items.length; j++) {
            ItemStack item = ItemUtil.getStack(itemHandler, j);
            if (item.isEmpty()) continue;
            ItemStackRenderState itemstackrenderstate = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemstackrenderstate, item, displayContext, blockEntity.level(), blockEntity, i + j);
            items[j] = itemstackrenderstate;
        }
    }
}
