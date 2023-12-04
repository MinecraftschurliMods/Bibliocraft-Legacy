package com.github.minecraftschurlimods.bibliocraft.block.toolrack;

import com.github.minecraftschurlimods.bibliocraft.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ToolRackMenu extends BCMenu<ToolRackBlockEntity> {
    public ToolRackMenu(int id, Inventory inventory, ToolRackBlockEntity blockEntity) {
        super(BCMenuTypes.TOOL_RACK.get(), id, inventory, blockEntity);
    }

    public ToolRackMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(BCMenuTypes.TOOL_RACK.get(), id, inventory, data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return null;
    }

    @Override
    protected void addSlots(Inventory inventory, ToolRackBlockEntity blockEntity) {
        TagKey<Item> tag = BCTags.Items.POTION_SHELF_POTIONS;
        addSlot(new TagLimitedSlot(blockEntity, 0, 53, 15, tag));
        addSlot(new TagLimitedSlot(blockEntity, 1, 107, 15, tag));
        addSlot(new TagLimitedSlot(blockEntity, 2, 53, 53, tag));
        addSlot(new TagLimitedSlot(blockEntity, 3, 107, 53, tag));
        addInventorySlots(inventory, 8, 84);
    }
}
