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

    public ItemStack getCarpet() {
        return getItem(1);
    }

    public void setCarpet(ItemStack stack) {
        setItem(1, stack);
    }

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        builder.with(TYPE_PROPERTY, getBlockState().getValue(TableBlock.TYPE));
        if (getCarpet().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof WoolCarpetBlock carpet) {
            builder.with(COLOR_PROPERTY, carpet.getColor());
        }
        return builder.build();
    }
}
