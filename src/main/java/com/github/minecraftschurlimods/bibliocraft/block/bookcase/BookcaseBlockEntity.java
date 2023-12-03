package com.github.minecraftschurlimods.bibliocraft.block.bookcase;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookcaseBlockEntity extends BCBlockEntity implements MenuProvider {
    public static final List<ModelProperty<Boolean>> MODEL_PROPERTIES = Util.make(new ArrayList<>(), list -> {
        for (int i = 0; i < 16; i++) {
            list.add(new ModelProperty<>());
        }
    });
    private static final Component TITLE = title("bookcase");

    public BookcaseBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.BOOKCASE.get(), 16, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
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
}
