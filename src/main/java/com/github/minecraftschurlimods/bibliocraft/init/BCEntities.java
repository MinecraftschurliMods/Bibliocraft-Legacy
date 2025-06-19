package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandEntity;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public interface BCEntities {
    // @formatter:off
    Supplier<EntityType<FancyArmorStandEntity>> FANCY_ARMOR_STAND = BCRegistries.ENTITIES.register("fancy_armor_stand", () -> EntityType.Builder.<FancyArmorStandEntity>of(FancyArmorStandEntity::new, MobCategory.MISC).noSummon().build("fancy_armor_stand"));
    Supplier<EntityType<SeatEntity>>            SEAT              = BCRegistries.ENTITIES.register("seat",              () -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC).sized(0, 0.875f).noSummon().build("seat"));
    // @formatter:on

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {
    }
}
