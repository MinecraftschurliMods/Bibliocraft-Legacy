package at.minecraftschurli.mods.bibliocraft.init;

import at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand.FancyArmorStandEntity;
import at.minecraftschurli.mods.bibliocraft.content.seat.SeatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface BCEntities {
    // @formatter:off
    Supplier<EntityType<FancyArmorStandEntity>> FANCY_ARMOR_STAND = BCRegistries.ENTITIES.registerEntityType("fancy_armor_stand", FancyArmorStandEntity::new, MobCategory.MISC, EntityType.Builder::noSummon);
    Supplier<EntityType<SeatEntity>>            SEAT              = BCRegistries.ENTITIES.registerEntityType("seat",              SeatEntity::new,            MobCategory.MISC, b -> b.sized(0, 0.875f).noSummon());
    // @formatter:on

    /// Empty method, called by [BCRegistries#init()] to classload this class.
    @ApiStatus.Internal
    static void init() {}
}
