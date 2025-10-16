package com.github.minecraftschurlimods.bibliocraft.content.bookcase;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;

public class BookcaseBlockEntity extends BCMenuBlockEntity {
    private static final int SLOTS = 16;
    public static final ModelProperty<Short> BOOKS = new ModelProperty<>();

    public BookcaseBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.BOOKCASE.get(), SLOTS, defaultName("bookcase"), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new BookcaseMenu(id, inventory, this);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        requestModelDataUpdate();
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        level().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        requestModelDataUpdate();
    }

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        short books = 0;
        for (int i = 0; i < SLOTS; i++) {
            books |= (short) ((getItem(i).isEmpty() ? 0 : 1) << i);
        }
        builder.with(BOOKS, books);
        return builder.build();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.is(BCTags.Items.BOOKCASE_BOOKS);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        requestModelDataUpdate();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
