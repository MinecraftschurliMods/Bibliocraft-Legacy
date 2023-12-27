package com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand;

import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Helper entity for rendering the contents of a {@link FancyArmorStandBlockEntity}. Defers item querying and rotations to the block entity.
 */
public class FancyArmorStandEntity extends ArmorStand {
    private FancyArmorStandBlockEntity blockEntity;

    public FancyArmorStandEntity(EntityType<? extends ArmorStand> entityType, Level level) {
        super(entityType, level);
    }

    public FancyArmorStandEntity(Level level, FancyArmorStandBlockEntity blockEntity) {
        this(BCEntities.FANCY_ARMOR_STAND.get(), level);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public float getYHeadRot() {
        return blockEntity != null ? blockEntity.getBlockState().getValue(FancyArmorStandBlock.FACING).getOpposite().toYRot() : super.getYHeadRot();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return slot.isArmor() && blockEntity != null ? blockEntity.getItem(3 - slot.getIndex()) : super.getItemBySlot(slot);
    }
}
