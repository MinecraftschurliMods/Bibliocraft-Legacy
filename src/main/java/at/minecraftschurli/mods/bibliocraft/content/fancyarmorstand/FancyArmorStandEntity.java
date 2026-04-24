package at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand;

import at.minecraftschurli.mods.bibliocraft.init.BCEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

/// Helper entity for rendering the contents of a [FancyArmorStandBlockEntity]. Defers item querying and rotations to the block entity.
public class FancyArmorStandEntity extends ArmorStand {
    @Nullable
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
    protected boolean shouldDropLoot(ServerLevel level) {
        return false;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return slot.isArmor() && blockEntity != null ? blockEntity.getItem(3 - slot.getIndex()) : super.getItemBySlot(slot);
    }
}
