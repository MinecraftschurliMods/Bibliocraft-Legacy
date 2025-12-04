package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandEntity;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public interface BCEntities {
    // @formatter:off
    Supplier<EntityType<FancyArmorStandEntity>> FANCY_ARMOR_STAND = BCRegistries.ENTITIES.registerEntityType("fancy_armor_stand", FancyArmorStandEntity::new, MobCategory.MISC, EntityType.Builder::noSummon);
    Supplier<EntityType<SeatEntity>>            SEAT              = BCRegistries.ENTITIES.registerEntityType("seat",              SeatEntity::new,            MobCategory.MISC, b -> b.sized(0, 0.875f).noSummon());
    // @formatter:on

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {
    }
}
