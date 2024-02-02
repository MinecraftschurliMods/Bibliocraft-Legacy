package com.github.minecraftschurlimods.bibliocraft;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.Objects;

public final class EventHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        private static void constructMod(FMLConstructModEvent event) {
            event.enqueueWork(() -> {
                BibliocraftWoodType.postRegister();
                BCRegistries.init(Objects.requireNonNull(ModList.get().getModContainerById(Bibliocraft.MOD_ID).orElseThrow().getEventBus()));
            });
        }

        @SubscribeEvent
        private static void entityAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(BCEntities.FANCY_ARMOR_STAND.get(), LivingEntity.createLivingAttributes().build());
        }
    }
}
