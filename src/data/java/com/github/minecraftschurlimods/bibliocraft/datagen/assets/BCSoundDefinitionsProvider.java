package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.stream.IntStream;

public class BCSoundDefinitionsProvider extends SoundDefinitionsProvider {
    public BCSoundDefinitionsProvider(PackOutput output) {
        super(output, BibliocraftApi.MOD_ID);
    }

    @Override
    public void registerSounds() {
        add(BCSoundEvents.CLOCK_CHIME, "clock/chime", "clock.chime");
        add(BCSoundEvents.CLOCK_TICK, "clock/tick_", 5, "clock.tick");
        add(BCSoundEvents.CLOCK_TOCK, "clock/tock_", 5, "clock.tock");
        add(BCSoundEvents.DESK_BELL, "desk_bell/", 4, "desk_bell");
        add(BCSoundEvents.DISPLAY_CASE_CLOSE, "display_case/close", "display_case.close");
        add(BCSoundEvents.DISPLAY_CASE_OPEN, "display_case/open", "display_case.open");
        add(BCSoundEvents.TAPE_MEASURE_CLOSE, "tape_measure/close", "tape_measure.close");
        add(BCSoundEvents.TAPE_MEASURE_OPEN, "tape_measure/open", "tape_measure.open");
        add(BCSoundEvents.TYPEWRITER_ADD_PAPER, "typewriter/add_paper", "typewriter.add_paper");
        add(BCSoundEvents.TYPEWRITER_CHIME, "typewriter/chime", "typewriter.chime");
        add(BCSoundEvents.TYPEWRITER_TAKE_PAGE, "typewriter/take_page", "typewriter.take_page");
        add(BCSoundEvents.TYPEWRITER_TYPE, "typewriter/type", "typewriter.type");
        add(BCSoundEvents.TYPEWRITER_TYPING, "typewriter/typing_", 5, "typewriter.typing");
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
