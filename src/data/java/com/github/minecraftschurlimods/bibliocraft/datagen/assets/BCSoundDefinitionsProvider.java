package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class BCSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public BCSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, BibliocraftApi.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(BCSoundEvents.CLOCK_CHIME.value(), SoundDefinition.definition().with(modSound("clock_chime")));
        add(BCSoundEvents.CLOCK_TICK.value(), SoundDefinition.definition().with(
                modSound("clock_tick_1"),
                modSound("clock_tick_2"),
                modSound("clock_tick_3"),
                modSound("clock_tick_4"),
                modSound("clock_tick_5")
        ));
        add(BCSoundEvents.CLOCK_TOCK.value(), SoundDefinition.definition().with(
                modSound("clock_tock_1"),
                modSound("clock_tock_2"),
                modSound("clock_tock_3"),
                modSound("clock_tock_4"),
                modSound("clock_tock_5")
        ));
        add(BCSoundEvents.DESK_BELL.value(), SoundDefinition.definition().with(
                modSound("desk_bell_1"),
                modSound("desk_bell_2"),
                modSound("desk_bell_3"),
                modSound("desk_bell_4")
        ));
        add(BCSoundEvents.TAPE_MEASURE_CLOSE.value(), SoundDefinition.definition().with(modSound("tape_measure_close")));
        add(BCSoundEvents.TAPE_MEASURE_OPEN.value(), SoundDefinition.definition().with(modSound("tape_measure_open")));
    }
    
    private SoundDefinition.Sound modSound(String name) {
        return sound(BCUtil.modLoc(name));
    }
}
