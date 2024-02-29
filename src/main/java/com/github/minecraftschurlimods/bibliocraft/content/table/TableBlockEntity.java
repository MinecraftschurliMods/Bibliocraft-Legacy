package com.github.minecraftschurlimods.bibliocraft.content.table;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class TableBlockEntity extends BCBlockEntity {
    public static final ModelProperty<TableBlock.Type> TYPE_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<DyeColor> COLOR_PROPERTY = new ModelProperty<>();

    public TableBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.TABLE.get(), 2, pos, state);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (slot == 1) {
            requestModelDataUpdate();
        }
    }

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        builder.with(TYPE_PROPERTY, getBlockState().getValue(TableBlock.TYPE));
        DyeColor color = TableBlock.getCarpetColor(getItem(1));
        if (color != null) {
            builder.with(COLOR_PROPERTY, color);
        }
        return builder.build();
    }
}
