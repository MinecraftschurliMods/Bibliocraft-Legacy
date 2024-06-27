package com.github.minecraftschurlimods.bibliocraft.content.bookcase;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.List;

public class BookcaseBlockEntity extends BCMenuBlockEntity {
    public static final List<ModelProperty<Boolean>> MODEL_PROPERTIES = Util.make(new ArrayList<>(), list -> {
        for (int i = 0; i < 16; i++) {
            list.add(new ModelProperty<>());
        }
    });

    public BookcaseBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.BOOKCASE.get(), 16, defaultName("bookcase"), pos, state);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        requestModelDataUpdate();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new BookcaseMenu(id, inventory, this);
    }

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        for (int i = 0; i < MODEL_PROPERTIES.size(); i++) {
            builder.with(MODEL_PROPERTIES.get(i), !items.getStackInSlot(i).isEmpty());
        }
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
