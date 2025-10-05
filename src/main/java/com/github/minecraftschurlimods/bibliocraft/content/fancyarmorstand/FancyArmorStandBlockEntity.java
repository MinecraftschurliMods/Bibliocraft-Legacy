package com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancyArmorStandBlockEntity extends BCMenuBlockEntity {
    private FancyArmorStandEntity entity;

    public FancyArmorStandBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_ARMOR_STAND.get(), 4, defaultName("fancy_armor_stand"), pos, state);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        entity = new FancyArmorStandEntity(level, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FancyArmorStandMenu(id, inventory, this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        entity.setRemoved(Entity.RemovalReason.KILLED);
        entity = null;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.isEmpty() || (stack.get(DataComponents.EQUIPPABLE) instanceof Equippable equippable && equippable.slot().isArmor() && equippable.slot().getIndex() == 3 - index && super.canPlaceItem(index, stack));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    /**
     * @return The {@link FancyArmorStandEntity} used for actually displaying the armor.
     */
    @Nullable
    public FancyArmorStandEntity getDisplayEntity() {
        return entity;
    }
}
