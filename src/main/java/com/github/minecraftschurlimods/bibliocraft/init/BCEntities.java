package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public interface BCEntities {
    Supplier<EntityType<FancyArmorStandEntity>> FANCY_ARMOR_STAND = BCRegistries.ENTITIES.register("fancy_armor_stand", () -> EntityType.Builder.<FancyArmorStandEntity>of(FancyArmorStandEntity::new, MobCategory.MISC).noSummon().build("fancy_armor_stand"));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
