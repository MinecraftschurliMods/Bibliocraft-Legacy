package com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancyArmorStandBlockEntity extends BCBlockEntity implements MenuProvider {
    private FancyArmorStandEntity entity;

    public FancyArmorStandBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_ARMOR_STAND.get(), 4, title("fancy_armor_stand"), pos, state);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        entity = new FancyArmorStandEntity(level, this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
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
        return stack.isEmpty() || (stack.getItem() instanceof Equipable equipable && equipable.getEquipmentSlot().isArmor() && equipable.getEquipmentSlot().getIndex() == 3 - index && super.canPlaceItem(index, stack));
    }

    @Nullable
    public FancyArmorStandEntity getDisplayEntity() {
        return entity;
    }
}
