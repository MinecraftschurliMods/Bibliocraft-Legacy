package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.stream.IntStream;

public class BCSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public BCSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, BibliocraftApi.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(BCSoundEvents.CLOCK_CHIME, "clock/chime", "clock.chime");
        add(BCSoundEvents.CLOCK_TICK, "clock/tick_", 5, "clock.tick");
        add(BCSoundEvents.CLOCK_TOCK, "clock/tock_", 5, "clock.tock");
        add(BCSoundEvents.DESK_BELL, "desk_bell/", 4, "desk_bell");
        add(BCSoundEvents.TAPE_MEASURE_CLOSE, "tape_measure/close", "tape_measure.close");
        add(BCSoundEvents.TAPE_MEASURE_OPEN, "tape_measure/open", "tape_measure.open");
    }

    private void add(Holder<SoundEvent> sound, String path, String subtitle) {
        add(sound.value(), SoundDefinition.definition()
                .with(sound(BCUtil.bcLoc(path)))
                .subtitle("subtitles." + BibliocraftApi.MOD_ID + "." + subtitle));
    }

    private void add(Holder<SoundEvent> sound, String path, int fileCount, String subtitle) {
        add(sound.value(), SoundDefinition.definition()
                .with(IntStream.range(0, fileCount).mapToObj(e -> sound(BCUtil.bcLoc(path + e))).toArray(SoundDefinition.Sound[]::new))
                .subtitle("subtitles." + BibliocraftApi.MOD_ID + "." + subtitle));
    }
}
